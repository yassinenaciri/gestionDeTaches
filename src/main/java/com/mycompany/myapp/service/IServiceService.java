package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.IService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link IService}.
 */
public interface IServiceService {
    /**
     * Save a iService.
     *
     * @param iService the entity to save.
     * @return the persisted entity.
     */
    IService save(IService iService);

    /**
     * Partially updates a iService.
     *
     * @param iService the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IService> partialUpdate(IService iService);

    /**
     * Get all the iServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IService> findAll(Pageable pageable);

    /**
     * Get the "id" iService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IService> findOne(Long id);

    /**
     * Delete the "id" iService.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
