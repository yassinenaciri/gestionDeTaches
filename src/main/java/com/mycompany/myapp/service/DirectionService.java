package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Direction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Direction}.
 */
public interface DirectionService {
    /**
     * Save a direction.
     *
     * @param direction the entity to save.
     * @return the persisted entity.
     */
    Direction save(Direction direction);

    /**
     * Partially updates a direction.
     *
     * @param direction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Direction> partialUpdate(Direction direction);

    /**
     * Get all the directions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Direction> findAll(Pageable pageable);

    /**
     * Get the "id" direction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Direction> findOne(Long id);

    /**
     * Delete the "id" direction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
