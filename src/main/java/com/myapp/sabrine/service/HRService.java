package com.myapp.sabrine.service;

import com.myapp.sabrine.domain.HR;
import com.myapp.sabrine.repository.HRRepository;
import com.myapp.sabrine.service.dto.HRDTO;
import com.myapp.sabrine.service.mapper.HRMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.myapp.sabrine.domain.HR}.
 */
@Service
@Transactional
public class HRService {

    private static final Logger LOG = LoggerFactory.getLogger(HRService.class);

    private final HRRepository hRRepository;

    private final HRMapper hRMapper;

    public HRService(HRRepository hRRepository, HRMapper hRMapper) {
        this.hRRepository = hRRepository;
        this.hRMapper = hRMapper;
    }

    /**
     * Save a hR.
     *
     * @param hRDTO the entity to save.
     * @return the persisted entity.
     */
    public HRDTO save(HRDTO hRDTO) {
        LOG.debug("Request to save HR : {}", hRDTO);
        HR hR = hRMapper.toEntity(hRDTO);
        hR = hRRepository.save(hR);
        return hRMapper.toDto(hR);
    }

    /**
     * Update a hR.
     *
     * @param hRDTO the entity to save.
     * @return the persisted entity.
     */
    public HRDTO update(HRDTO hRDTO) {
        LOG.debug("Request to update HR : {}", hRDTO);
        HR hR = hRMapper.toEntity(hRDTO);
        hR = hRRepository.save(hR);
        return hRMapper.toDto(hR);
    }

    /**
     * Partially update a hR.
     *
     * @param hRDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HRDTO> partialUpdate(HRDTO hRDTO) {
        LOG.debug("Request to partially update HR : {}", hRDTO);

        return hRRepository
            .findById(hRDTO.getId())
            .map(existingHR -> {
                hRMapper.partialUpdate(existingHR, hRDTO);

                return existingHR;
            })
            .map(hRRepository::save)
            .map(hRMapper::toDto);
    }

    /**
     * Get all the hRS.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HRDTO> findAll() {
        LOG.debug("Request to get all HRS");
        return hRRepository.findAll().stream().map(hRMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hR by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HRDTO> findOne(Long id) {
        LOG.debug("Request to get HR : {}", id);
        return hRRepository.findById(id).map(hRMapper::toDto);
    }

    /**
     * Delete the hR by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HR : {}", id);
        hRRepository.deleteById(id);
    }
}
