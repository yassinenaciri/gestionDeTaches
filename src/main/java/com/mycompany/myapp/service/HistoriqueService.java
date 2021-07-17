package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Historique;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Historique}.
 */
public interface HistoriqueService {
    /**
     * Save a historique.
     *
     * @param historique the entity to save.
     * @return the persisted entity.
     */
    Historique save(Historique historique);

    /**
     * Partially updates a historique.
     *
     * @param historique the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Historique> partialUpdate(Historique historique);

    /**
     * Get all the historiques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Historique> findAll(Pageable pageable);

    /**
     * Get the "id" historique.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Historique> findOne(Long id);

    /**
     * Delete the "id" historique.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
