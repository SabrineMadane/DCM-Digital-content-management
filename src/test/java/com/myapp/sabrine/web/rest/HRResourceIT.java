package com.myapp.sabrine.web.rest;

import static com.myapp.sabrine.domain.HRAsserts.*;
import static com.myapp.sabrine.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.sabrine.IntegrationTest;
import com.myapp.sabrine.domain.HR;
import com.myapp.sabrine.repository.HRRepository;
import com.myapp.sabrine.service.dto.HRDTO;
import com.myapp.sabrine.service.mapper.HRMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HRResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HRResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HRRepository hRRepository;

    @Autowired
    private HRMapper hRMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHRMockMvc;

    private HR hR;

    private HR insertedHR;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HR createEntity() {
        return new HR().nom(DEFAULT_NOM).prenom(DEFAULT_PRENOM).email(DEFAULT_EMAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HR createUpdatedEntity() {
        return new HR().nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        hR = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHR != null) {
            hRRepository.delete(insertedHR);
            insertedHR = null;
        }
    }

    @Test
    @Transactional
    void createHR() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HR
        HRDTO hRDTO = hRMapper.toDto(hR);
        var returnedHRDTO = om.readValue(
            restHRMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HRDTO.class
        );

        // Validate the HR in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHR = hRMapper.toEntity(returnedHRDTO);
        assertHRUpdatableFieldsEquals(returnedHR, getPersistedHR(returnedHR));

        insertedHR = returnedHR;
    }

    @Test
    @Transactional
    void createHRWithExistingId() throws Exception {
        // Create the HR with an existing ID
        hR.setId(1L);
        HRDTO hRDTO = hRMapper.toDto(hR);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHRMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hR.setNom(null);

        // Create the HR, which fails.
        HRDTO hRDTO = hRMapper.toDto(hR);

        restHRMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hR.setPrenom(null);

        // Create the HR, which fails.
        HRDTO hRDTO = hRMapper.toDto(hR);

        restHRMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hR.setEmail(null);

        // Create the HR, which fails.
        HRDTO hRDTO = hRMapper.toDto(hR);

        restHRMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHRS() throws Exception {
        // Initialize the database
        insertedHR = hRRepository.saveAndFlush(hR);

        // Get all the hRList
        restHRMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hR.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getHR() throws Exception {
        // Initialize the database
        insertedHR = hRRepository.saveAndFlush(hR);

        // Get the hR
        restHRMockMvc
            .perform(get(ENTITY_API_URL_ID, hR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hR.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingHR() throws Exception {
        // Get the hR
        restHRMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHR() throws Exception {
        // Initialize the database
        insertedHR = hRRepository.saveAndFlush(hR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hR
        HR updatedHR = hRRepository.findById(hR.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHR are not directly saved in db
        em.detach(updatedHR);
        updatedHR.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL);
        HRDTO hRDTO = hRMapper.toDto(updatedHR);

        restHRMockMvc
            .perform(put(ENTITY_API_URL_ID, hRDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isOk());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHRToMatchAllProperties(updatedHR);
    }

    @Test
    @Transactional
    void putNonExistingHR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hR.setId(longCount.incrementAndGet());

        // Create the HR
        HRDTO hRDTO = hRMapper.toDto(hR);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHRMockMvc
            .perform(put(ENTITY_API_URL_ID, hRDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hR.setId(longCount.incrementAndGet());

        // Create the HR
        HRDTO hRDTO = hRMapper.toDto(hR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hR.setId(longCount.incrementAndGet());

        // Create the HR
        HRDTO hRDTO = hRMapper.toDto(hR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHRMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHRWithPatch() throws Exception {
        // Initialize the database
        insertedHR = hRRepository.saveAndFlush(hR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hR using partial update
        HR partialUpdatedHR = new HR();
        partialUpdatedHR.setId(hR.getId());

        partialUpdatedHR.nom(UPDATED_NOM).email(UPDATED_EMAIL);

        restHRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHR.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHR))
            )
            .andExpect(status().isOk());

        // Validate the HR in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHRUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHR, hR), getPersistedHR(hR));
    }

    @Test
    @Transactional
    void fullUpdateHRWithPatch() throws Exception {
        // Initialize the database
        insertedHR = hRRepository.saveAndFlush(hR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hR using partial update
        HR partialUpdatedHR = new HR();
        partialUpdatedHR.setId(hR.getId());

        partialUpdatedHR.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL);

        restHRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHR.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHR))
            )
            .andExpect(status().isOk());

        // Validate the HR in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHRUpdatableFieldsEquals(partialUpdatedHR, getPersistedHR(partialUpdatedHR));
    }

    @Test
    @Transactional
    void patchNonExistingHR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hR.setId(longCount.incrementAndGet());

        // Create the HR
        HRDTO hRDTO = hRMapper.toDto(hR);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hRDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hR.setId(longCount.incrementAndGet());

        // Create the HR
        HRDTO hRDTO = hRMapper.toDto(hR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hR.setId(longCount.incrementAndGet());

        // Create the HR
        HRDTO hRDTO = hRMapper.toDto(hR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHRMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hRDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHR() throws Exception {
        // Initialize the database
        insertedHR = hRRepository.saveAndFlush(hR);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hR
        restHRMockMvc.perform(delete(ENTITY_API_URL_ID, hR.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hRRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected HR getPersistedHR(HR hR) {
        return hRRepository.findById(hR.getId()).orElseThrow();
    }

    protected void assertPersistedHRToMatchAllProperties(HR expectedHR) {
        assertHRAllPropertiesEquals(expectedHR, getPersistedHR(expectedHR));
    }

    protected void assertPersistedHRToMatchUpdatableProperties(HR expectedHR) {
        assertHRAllUpdatablePropertiesEquals(expectedHR, getPersistedHR(expectedHR));
    }
}
