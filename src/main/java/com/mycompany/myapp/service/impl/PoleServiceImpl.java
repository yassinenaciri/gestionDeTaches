package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Pole;
import com.mycompany.myapp.repository.PoleRepository;
import com.mycompany.myapp.service.PoleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pole}.
 */
@Service
@Transactional
public class PoleServiceImpl implements PoleService {

    private final Logger log = LoggerFactory.getLogger(PoleServiceImpl.class);

    private final PoleRepository poleRepository;

    public PoleServiceImpl(PoleRepository poleRepository) {
        this.poleRepository = poleRepository;
    }

    @Override
    public Pole save(Pole pole) {
        log.debug("Request to save Pole : {}", pole);
        return poleRepository.save(pole);
    }

    @Override
    public Optional<Pole> partialUpdate(Pole pole) {
        log.debug("Request to partially update Pole : {}", pole);

        return poleRepository
            .findById(pole.getId())
            .map(
                existingPole -> {
                    if (pole.getNomPole() != null) {
                        existingPole.setNomPole(pole.getNomPole());
                    }

                    return existingPole;
                }
            )
            .map(poleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pole> findAll(Pageable pageable) {
        log.debug("Request to get all Poles");
        return poleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pole> findOne(Long id) {
        log.debug("Request to get Pole : {}", id);
        return poleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pole : {}", id);
        poleRepository.deleteById(id);
    }
}
