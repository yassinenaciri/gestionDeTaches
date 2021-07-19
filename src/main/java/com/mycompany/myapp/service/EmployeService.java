package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Employe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Employe}.
 */
public interface EmployeService {
    /**
     * Save a employe.
     *
     * @param employe the entity to save.
     * @return the persisted entity.
     */
    Employe save(Employe employe);

    /**
     * Partially updates a employe.
     *
     * @param employe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Employe> partialUpdate(Employe employe);

    /**
     * Get all the employes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Employe> findAll(Pageable pageable);

    /**
     * Get the "id" employe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Employe> findOne(Long id);

    /**
     * Delete the "id" employe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
