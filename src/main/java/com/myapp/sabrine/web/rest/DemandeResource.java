package com.myapp.sabrine.web.rest;

import com.myapp.sabrine.repository.DemandeRepository;
import com.myapp.sabrine.service.DemandeService;
import com.myapp.sabrine.service.dto.DemandeDTO;
import com.myapp.sabrine.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.sabrine.domain.Demande}.
 */
@RestController
@RequestMapping("/api/demandes")
public class DemandeResource {

    private static final Logger LOG = LoggerFactory.getLogger(DemandeResource.class);

    private static final String ENTITY_NAME = "demande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeService demandeService;

    private final DemandeRepository demandeRepository;

    public DemandeResource(DemandeService demandeService, DemandeRepository demandeRepository) {
        this.demandeService = demandeService;
        this.demandeRepository = demandeRepository;
    }

    /**
     * {@code POST  /demandes} : Create a new demande.
     *
     * @param demandeDTO the demandeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeDTO, or with status {@code 400 (Bad Request)} if the demande has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DemandeDTO> createDemande(@Valid @RequestBody DemandeDTO demandeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Demande : {}", demandeDTO);
        if (demandeDTO.getId() != null) {
            throw new BadRequestAlertException("A new demande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        demandeDTO = demandeService.save(demandeDTO);
        return ResponseEntity.created(new URI("/api/demandes/" + demandeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, demandeDTO.getId().toString()))
            .body(demandeDTO);
    }

    /**
     * {@code PUT  /demandes/:id} : Updates an existing demande.
     *
     * @param id the id of the demandeDTO to save.
     * @param demandeDTO the demandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeDTO,
     * or with status {@code 400 (Bad Request)} if the demandeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DemandeDTO> updateDemande(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemandeDTO demandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Demande : {}, {}", id, demandeDTO);
        if (demandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        demandeDTO = demandeService.update(demandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeDTO.getId().toString()))
            .body(demandeDTO);
    }

    /**
     * {@code PATCH  /demandes/:id} : Partial updates given fields of an existing demande, field will ignore if it is null
     *
     * @param id the id of the demandeDTO to save.
     * @param demandeDTO the demandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeDTO,
     * or with status {@code 400 (Bad Request)} if the demandeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the demandeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeDTO> partialUpdateDemande(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemandeDTO demandeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Demande partially : {}, {}", id, demandeDTO);
        if (demandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeDTO> result = demandeService.partialUpdate(demandeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /demandes} : get all the demandes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandes in body.
     */
    @GetMapping("")
    public List<DemandeDTO> getAllDemandes() {
        LOG.debug("REST request to get all Demandes");
        return demandeService.findAll();
    }

    /**
     * {@code GET  /demandes/:id} : get the "id" demande.
     *
     * @param id the id of the demandeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DemandeDTO> getDemande(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Demande : {}", id);
        Optional<DemandeDTO> demandeDTO = demandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandeDTO);
    }

    /**
     * {@code DELETE  /demandes/:id} : delete the "id" demande.
     *
     * @param id the id of the demandeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Demande : {}", id);
        demandeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
