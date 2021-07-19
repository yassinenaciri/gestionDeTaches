package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Chef;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Chef}.
 */
public interface ChefService {
    /**
     * Save a chef.
     *
     * @param chef the entity to save.
     * @return the persisted entity.
     */
    Chef save(Chef chef);

    /**
     * Partially updates a chef.
     *
     * @param chef the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Chef> partialUpdate(Chef chef);

    /**
     * Get all the chefs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Chef> findAll(Pageable pageable);
    /**
     * Get all the Chef where Service is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Chef> findAllWhereServiceIsNull();
    /**
     * Get all the Chef where Pole is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Chef> findAllWherePoleIsNull();
    /**
     * Get all the Chef where Direction is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Chef> findAllWhereDirectionIsNull();
    /**
     * Get all the Chef where Division is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Chef> findAllWhereDivisionIsNull();

    /**
     * Get the "id" chef.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Chef> findOne(Long id);

    /**
     * Delete the "id" chef.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
