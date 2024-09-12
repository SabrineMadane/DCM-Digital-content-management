package com.myapp.sabrine.web.rest;

import static com.myapp.sabrine.domain.EntrepriseAsserts.*;
import static com.myapp.sabrine.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.sabrine.IntegrationTest;
import com.myapp.sabrine.domain.Entreprise;
import com.myapp.sabrine.repository.EntrepriseRepository;
import com.myapp.sabrine.service.dto.EntrepriseDTO;
import com.myapp.sabrine.service.mapper.EntrepriseMapper;
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
 * Integration tests for the {@link EntrepriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntrepriseResourceIT {

    private static final String DEFAULT_SECTEUR = "AAAAAAAAAA";
    private static final String UPDATED_SECTEUR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PRESENCE_AU_MAROC = false;
    private static final Boolean UPDATED_PRESENCE_AU_MAROC = true;

    private static final String DEFAULT_FOCUS = "AAAAAAAAAA";
    private static final String UPDATED_FOCUS = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_PROFIL_LINKED_IN = "AAAAAAAAAA";
    private static final String UPDATED_PROFIL_LINKED_IN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entreprises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private EntrepriseMapper entrepriseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntrepriseMockMvc;

    private Entreprise entreprise;

    private Entreprise insertedEntreprise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprise createEntity() {
        return new Entreprise()
            .secteur(DEFAULT_SECTEUR)
            .presenceAuMaroc(DEFAULT_PRESENCE_AU_MAROC)
            .focus(DEFAULT_FOCUS)
            .logo(DEFAULT_LOGO)
            .profilLinkedIn(DEFAULT_PROFIL_LINKED_IN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprise createUpdatedEntity() {
        return new Entreprise()
            .secteur(UPDATED_SECTEUR)
            .presenceAuMaroc(UPDATED_PRESENCE_AU_MAROC)
            .focus(UPDATED_FOCUS)
            .logo(UPDATED_LOGO)
            .profilLinkedIn(UPDATED_PROFIL_LINKED_IN);
    }

    @BeforeEach
    public void initTest() {
        entreprise = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEntreprise != null) {
            entrepriseRepository.delete(insertedEntreprise);
            insertedEntreprise = null;
        }
    }

    @Test
    @Transactional
    void createEntreprise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);
        var returnedEntrepriseDTO = om.readValue(
            restEntrepriseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EntrepriseDTO.class
        );

        // Validate the Entreprise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntreprise = entrepriseMapper.toEntity(returnedEntrepriseDTO);
        assertEntrepriseUpdatableFieldsEquals(returnedEntreprise, getPersistedEntreprise(returnedEntreprise));

        insertedEntreprise = returnedEntreprise;
    }

    @Test
    @Transactional
    void createEntrepriseWithExistingId() throws Exception {
        // Create the Entreprise with an existing ID
        entreprise.setId(1L);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntrepriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSecteurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entreprise.setSecteur(null);

        // Create the Entreprise, which fails.
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        restEntrepriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPresenceAuMarocIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entreprise.setPresenceAuMaroc(null);

        // Create the Entreprise, which fails.
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        restEntrepriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEntreprises() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].secteur").value(hasItem(DEFAULT_SECTEUR)))
            .andExpect(jsonPath("$.[*].presenceAuMaroc").value(hasItem(DEFAULT_PRESENCE_AU_MAROC.booleanValue())))
            .andExpect(jsonPath("$.[*].focus").value(hasItem(DEFAULT_FOCUS)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].profilLinkedIn").value(hasItem(DEFAULT_PROFIL_LINKED_IN)));
    }

    @Test
    @Transactional
    void getEntreprise() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get the entreprise
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL_ID, entreprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entreprise.getId().intValue()))
            .andExpect(jsonPath("$.secteur").value(DEFAULT_SECTEUR))
            .andExpect(jsonPath("$.presenceAuMaroc").value(DEFAULT_PRESENCE_AU_MAROC.booleanValue()))
            .andExpect(jsonPath("$.focus").value(DEFAULT_FOCUS))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.profilLinkedIn").value(DEFAULT_PROFIL_LINKED_IN));
    }

    @Test
    @Transactional
    void getNonExistingEntreprise() throws Exception {
        // Get the entreprise
        restEntrepriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntreprise() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entreprise
        Entreprise updatedEntreprise = entrepriseRepository.findById(entreprise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEntreprise are not directly saved in db
        em.detach(updatedEntreprise);
        updatedEntreprise
            .secteur(UPDATED_SECTEUR)
            .presenceAuMaroc(UPDATED_PRESENCE_AU_MAROC)
            .focus(UPDATED_FOCUS)
            .logo(UPDATED_LOGO)
            .profilLinkedIn(UPDATED_PROFIL_LINKED_IN);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(updatedEntreprise);

        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntrepriseToMatchAllProperties(updatedEntreprise);
    }

    @Test
    @Transactional
    void putNonExistingEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntrepriseWithPatch() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entreprise using partial update
        Entreprise partialUpdatedEntreprise = new Entreprise();
        partialUpdatedEntreprise.setId(entreprise.getId());

        partialUpdatedEntreprise.secteur(UPDATED_SECTEUR).focus(UPDATED_FOCUS).logo(UPDATED_LOGO).profilLinkedIn(UPDATED_PROFIL_LINKED_IN);

        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntreprise))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntrepriseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntreprise, entreprise),
            getPersistedEntreprise(entreprise)
        );
    }

    @Test
    @Transactional
    void fullUpdateEntrepriseWithPatch() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entreprise using partial update
        Entreprise partialUpdatedEntreprise = new Entreprise();
        partialUpdatedEntreprise.setId(entreprise.getId());

        partialUpdatedEntreprise
            .secteur(UPDATED_SECTEUR)
            .presenceAuMaroc(UPDATED_PRESENCE_AU_MAROC)
            .focus(UPDATED_FOCUS)
            .logo(UPDATED_LOGO)
            .profilLinkedIn(UPDATED_PROFIL_LINKED_IN);

        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntreprise))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntrepriseUpdatableFieldsEquals(partialUpdatedEntreprise, getPersistedEntreprise(partialUpdatedEntreprise));
    }

    @Test
    @Transactional
    void patchNonExistingEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntreprise() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entreprise
        restEntrepriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, entreprise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entrepriseRepository.count();
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

    protected Entreprise getPersistedEntreprise(Entreprise entreprise) {
        return entrepriseRepository.findById(entreprise.getId()).orElseThrow();
    }

    protected void assertPersistedEntrepriseToMatchAllProperties(Entreprise expectedEntreprise) {
        assertEntrepriseAllPropertiesEquals(expectedEntreprise, getPersistedEntreprise(expectedEntreprise));
    }

    protected void assertPersistedEntrepriseToMatchUpdatableProperties(Entreprise expectedEntreprise) {
        assertEntrepriseAllUpdatablePropertiesEquals(expectedEntreprise, getPersistedEntreprise(expectedEntreprise));
    }
}
