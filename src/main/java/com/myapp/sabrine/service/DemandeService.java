package com.myapp.sabrine.service;

import com.myapp.sabrine.domain.Demande;
import com.myapp.sabrine.repository.DemandeRepository;
import com.myapp.sabrine.service.dto.DemandeDTO;
import com.myapp.sabrine.service.mapper.DemandeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.myapp.sabrine.domain.Demande}.
 */
@Service
@Transactional
public class DemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(DemandeService.class);

    private final DemandeRepository demandeRepository;

    private final DemandeMapper demandeMapper;

    public DemandeService(DemandeRepository demandeRepository, DemandeMapper demandeMapper) {
        this.demandeRepository = demandeRepository;
        this.demandeMapper = demandeMapper;
    }

    /**
     * Save a demande.
     *
     * @param demandeDTO the entity to save.
     * @return the persisted entity.
     */
    public DemandeDTO save(DemandeDTO demandeDTO) {
        LOG.debug("Request to save Demande : {}", demandeDTO);
        Demande demande = demandeMapper.toEntity(demandeDTO);
        demande = demandeRepository.save(demande);
        return demandeMapper.toDto(demande);
    }

    /**
     * Update a demande.
     *
     * @param demandeDTO the entity to save.
     * @return the persisted entity.
     */
    public DemandeDTO update(DemandeDTO demandeDTO) {
        LOG.debug("Request to update Demande : {}", demandeDTO);
        Demande demande = demandeMapper.toEntity(demandeDTO);
        demande = demandeRepository.save(demande);
        return demandeMapper.toDto(demande);
    }

    /**
     * Partially update a demande.
     *
     * @param demandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DemandeDTO> partialUpdate(DemandeDTO demandeDTO) {
        LOG.debug("Request to partially update Demande : {}", demandeDTO);

        return demandeRepository
            .findById(demandeDTO.getId())
            .map(existingDemande -> {
                demandeMapper.partialUpdate(existingDemande, demandeDTO);

                return existingDemande;
            })
            .map(demandeRepository::save)
            .map(demandeMapper::toDto);
    }

    /**
     * Get all the demandes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DemandeDTO> findAll() {
        LOG.debug("Request to get all Demandes");
        return demandeRepository.findAll().stream().map(demandeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one demande by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DemandeDTO> findOne(Long id) {
        LOG.debug("Request to get Demande : {}", id);
        return demandeRepository.findById(id).map(demandeMapper::toDto);
    }

    /**
     * Delete the demande by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Demande : {}", id);
        demandeRepository.deleteById(id);
    }
}
