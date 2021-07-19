package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Pole;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Pole}.
 */
public interface PoleService {
    /**
     * Save a pole.
     *
     * @param pole the entity to save.
     * @return the persisted entity.
     */
    Pole save(Pole pole);

    /**
     * Partially updates a pole.
     *
     * @param pole the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pole> partialUpdate(Pole pole);

    /**
     * Get all the poles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pole> findAll(Pageable pageable);

    /**
     * Get the "id" pole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pole> findOne(Long id);

    /**
     * Delete the "id" pole.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
