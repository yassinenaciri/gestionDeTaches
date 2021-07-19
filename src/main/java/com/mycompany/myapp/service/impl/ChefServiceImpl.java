package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Chef;
import com.mycompany.myapp.repository.ChefRepository;
import com.mycompany.myapp.service.ChefService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Chef}.
 */
@Service
@Transactional
public class ChefServiceImpl implements ChefService {

    private final Logger log = LoggerFactory.getLogger(ChefServiceImpl.class);

    private final ChefRepository chefRepository;

    public ChefServiceImpl(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    @Override
    public Chef save(Chef chef) {
        log.debug("Request to save Chef : {}", chef);
        return chefRepository.save(chef);
    }

    @Override
    public Optional<Chef> partialUpdate(Chef chef) {
        log.debug("Request to partially update Chef : {}", chef);

        return chefRepository
            .findById(chef.getId())
            .map(
                existingChef -> {
                    if (chef.getNomComplet() != null) {
                        existingChef.setNomComplet(chef.getNomComplet());
                    }
                    if (chef.getRole() != null) {
                        existingChef.setRole(chef.getRole());
                    }

                    return existingChef;
                }
            )
            .map(chefRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Chef> findAll(Pageable pageable) {
        log.debug("Request to get all Chefs");
        return chefRepository.findAll(pageable);
    }

    /**
     *  Get all the chefs where Service is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Chef> findAllWhereServiceIsNull() {
        log.debug("Request to get all chefs where Service is null");
        return StreamSupport
            .stream(chefRepository.findAll().spliterator(), false)
            .filter(chef -> chef.getService() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the chefs where Pole is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Chef> findAllWherePoleIsNull() {
        log.debug("Request to get all chefs where Pole is null");
        return StreamSupport
            .stream(chefRepository.findAll().spliterator(), false)
            .filter(chef -> chef.getPole() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the chefs where Direction is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Chef> findAllWhereDirectionIsNull() {
        log.debug("Request to get all chefs where Direction is null");
        return StreamSupport
            .stream(chefRepository.findAll().spliterator(), false)
            .filter(chef -> chef.getDirection() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the chefs where Division is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Chef> findAllWhereDivisionIsNull() {
        log.debug("Request to get all chefs where Division is null");
        return StreamSupport
            .stream(chefRepository.findAll().spliterator(), false)
            .filter(chef -> chef.getDivision() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Chef> findOne(Long id) {
        log.debug("Request to get Chef : {}", id);
        return chefRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chef : {}", id);
        chefRepository.deleteById(id);
    }
}
