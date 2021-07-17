package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Division;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Division}.
 */
public interface DivisionService {
    /**
     * Save a division.
     *
     * @param division the entity to save.
     * @return the persisted entity.
     */
    Division save(Division division);

    /**
     * Partially updates a division.
     *
     * @param division the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Division> partialUpdate(Division division);

    /**
     * Get all the divisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Division> findAll(Pageable pageable);

    /**
     * Get the "id" division.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Division> findOne(Long id);

    /**
     * Delete the "id" division.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
