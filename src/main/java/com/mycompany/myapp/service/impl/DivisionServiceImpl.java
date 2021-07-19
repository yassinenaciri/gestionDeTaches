package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Division;
import com.mycompany.myapp.repository.DivisionRepository;
import com.mycompany.myapp.service.DivisionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Division}.
 */
@Service
@Transactional
public class DivisionServiceImpl implements DivisionService {

    private final Logger log = LoggerFactory.getLogger(DivisionServiceImpl.class);

    private final DivisionRepository divisionRepository;

    public DivisionServiceImpl(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    @Override
    public Division save(Division division) {
        log.debug("Request to save Division : {}", division);
        return divisionRepository.save(division);
    }

    @Override
    public Optional<Division> partialUpdate(Division division) {
        log.debug("Request to partially update Division : {}", division);

        return divisionRepository
            .findById(division.getId())
            .map(
                existingDivision -> {
                    if (division.getNomDivision() != null) {
                        existingDivision.setNomDivision(division.getNomDivision());
                    }

                    return existingDivision;
                }
            )
            .map(divisionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Division> findAll(Pageable pageable) {
        log.debug("Request to get all Divisions");
        return divisionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Division> findOne(Long id) {
        log.debug("Request to get Division : {}", id);
        return divisionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Division : {}", id);
        divisionRepository.deleteById(id);
    }
}
