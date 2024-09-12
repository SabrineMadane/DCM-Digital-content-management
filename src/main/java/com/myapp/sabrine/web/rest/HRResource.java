package com.myapp.sabrine.web.rest;

import com.myapp.sabrine.repository.HRRepository;
import com.myapp.sabrine.service.HRService;
import com.myapp.sabrine.service.dto.HRDTO;
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
 * REST controller for managing {@link com.myapp.sabrine.domain.HR}.
 */
@RestController
@RequestMapping("/api/hrs")
public class HRResource {

    private static final Logger LOG = LoggerFactory.getLogger(HRResource.class);

    private static final String ENTITY_NAME = "hR";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HRService hRService;

    private final HRRepository hRRepository;

    public HRResource(HRService hRService, HRRepository hRRepository) {
        this.hRService = hRService;
        this.hRRepository = hRRepository;
    }

    /**
     * {@code POST  /hrs} : Create a new hR.
     *
     * @param hRDTO the hRDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hRDTO, or with status {@code 400 (Bad Request)} if the hR has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HRDTO> createHR(@Valid @RequestBody HRDTO hRDTO) throws URISyntaxException {
        LOG.debug("REST request to save HR : {}", hRDTO);
        if (hRDTO.getId() != null) {
            throw new BadRequestAlertException("A new hR cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hRDTO = hRService.save(hRDTO);
        return ResponseEntity.created(new URI("/api/hrs/" + hRDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, hRDTO.getId().toString()))
            .body(hRDTO);
    }

    /**
     * {@code PUT  /hrs/:id} : Updates an existing hR.
     *
     * @param id the id of the hRDTO to save.
     * @param hRDTO the hRDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hRDTO,
     * or with status {@code 400 (Bad Request)} if the hRDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hRDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HRDTO> updateHR(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody HRDTO hRDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update HR : {}, {}", id, hRDTO);
        if (hRDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hRDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hRDTO = hRService.update(hRDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hRDTO.getId().toString()))
            .body(hRDTO);
    }

    /**
     * {@code PATCH  /hrs/:id} : Partial updates given fields of an existing hR, field will ignore if it is null
     *
     * @param id the id of the hRDTO to save.
     * @param hRDTO the hRDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hRDTO,
     * or with status {@code 400 (Bad Request)} if the hRDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hRDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hRDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HRDTO> partialUpdateHR(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HRDTO hRDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HR partially : {}, {}", id, hRDTO);
        if (hRDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hRDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HRDTO> result = hRService.partialUpdate(hRDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hRDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hrs} : get all the hRS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hRS in body.
     */
    @GetMapping("")
    public List<HRDTO> getAllHRS() {
        LOG.debug("REST request to get all HRS");
        return hRService.findAll();
    }

    /**
     * {@code GET  /hrs/:id} : get the "id" hR.
     *
     * @param id the id of the hRDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hRDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HRDTO> getHR(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HR : {}", id);
        Optional<HRDTO> hRDTO = hRService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hRDTO);
    }

    /**
     * {@code DELETE  /hrs/:id} : delete the "id" hR.
     *
     * @param id the id of the hRDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHR(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HR : {}", id);
        hRService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
