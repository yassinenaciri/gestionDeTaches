package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Tache;
import com.mycompany.myapp.repository.TacheRepository;
import com.mycompany.myapp.service.TacheService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tache}.
 */
@Service
@Transactional
public class TacheServiceImpl implements TacheService {

    private final Logger log = LoggerFactory.getLogger(TacheServiceImpl.class);

    private final TacheRepository tacheRepository;

    public TacheServiceImpl(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    @Override
    public Tache save(Tache tache) {
        log.debug("Request to save Tache : {}", tache);
        return tacheRepository.save(tache);
    }

    @Override
    public Optional<Tache> partialUpdate(Tache tache) {
        log.debug("Request to partially update Tache : {}", tache);

        return tacheRepository
            .findById(tache.getId())
            .map(
                existingTache -> {
                    if (tache.getNom() != null) {
                        existingTache.setNom(tache.getNom());
                    }
                    if (tache.getDureEstime() != null) {
                        existingTache.setDureEstime(tache.getDureEstime());
                    }
                    if (tache.getDescription() != null) {
                        existingTache.setDescription(tache.getDescription());
                    }
                    if (tache.getEtat() != null) {
                        existingTache.setEtat(tache.getEtat());
                    }

                    return existingTache;
                }
            )
            .map(tacheRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tache> findAll(Pageable pageable) {
        log.debug("Request to get all Taches");
        return tacheRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tache> findOne(Long id) {
        log.debug("Request to get Tache : {}", id);
        return tacheRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tache : {}", id);
        tacheRepository.deleteById(id);
    }
}
