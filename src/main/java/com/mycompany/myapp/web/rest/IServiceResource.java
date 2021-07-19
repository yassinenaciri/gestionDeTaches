package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.IService;
import com.mycompany.myapp.repository.IServiceRepository;
import com.mycompany.myapp.service.IServiceService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.IService}.
 */
@RestController
@RequestMapping("/api")
public class IServiceResource {

    private final Logger log = LoggerFactory.getLogger(IServiceResource.class);

    private static final String ENTITY_NAME = "iService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IServiceService iServiceService;

    private final IServiceRepository iServiceRepository;

    public IServiceResource(IServiceService iServiceService, IServiceRepository iServiceRepository) {
        this.iServiceService = iServiceService;
        this.iServiceRepository = iServiceRepository;
    }

    /**
     * {@code POST  /i-services} : Create a new iService.
     *
     * @param iService the iService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iService, or with status {@code 400 (Bad Request)} if the iService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-services")
    public ResponseEntity<IService> createIService(@Valid @RequestBody IService iService) throws URISyntaxException {
        log.debug("REST request to save IService : {}", iService);
        if (iService.getId() != null) {
            throw new BadRequestAlertException("A new iService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IService result = iServiceService.save(iService);
        return ResponseEntity
            .created(new URI("/api/i-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /i-services/:id} : Updates an existing iService.
     *
     * @param id the id of the iService to save.
     * @param iService the iService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iService,
     * or with status {@code 400 (Bad Request)} if the iService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-services/{id}")
    public ResponseEntity<IService> updateIService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IService iService
    ) throws URISyntaxException {
        log.debug("REST request to update IService : {}, {}", id, iService);
        if (iService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IService result = iServiceService.save(iService);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iService.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /i-services/:id} : Partial updates given fields of an existing iService, field will ignore if it is null
     *
     * @param id the id of the iService to save.
     * @param iService the iService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iService,
     * or with status {@code 400 (Bad Request)} if the iService is not valid,
     * or with status {@code 404 (Not Found)} if the iService is not found,
     * or with status {@code 500 (Internal Server Error)} if the iService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-services/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IService> partialUpdateIService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IService iService
    ) throws URISyntaxException {
        log.debug("REST request to partial update IService partially : {}, {}", id, iService);
        if (iService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IService> result = iServiceService.partialUpdate(iService);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iService.getId().toString())
        );
    }

    /**
     * {@code GET  /i-services} : get all the iServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iServices in body.
     */
    @GetMapping("/i-services")
    public ResponseEntity<List<IService>> getAllIServices(Pageable pageable) {
        log.debug("REST request to get a page of IServices");
        Page<IService> page = iServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /i-services/:id} : get the "id" iService.
     *
     * @param id the id of the iService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-services/{id}")
    public ResponseEntity<IService> getIService(@PathVariable Long id) {
        log.debug("REST request to get IService : {}", id);
        Optional<IService> iService = iServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iService);
    }

    /**
     * {@code DELETE  /i-services/:id} : delete the "id" iService.
     *
     * @param id the id of the iService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-services/{id}")
    public ResponseEntity<Void> deleteIService(@PathVariable Long id) {
        log.debug("REST request to delete IService : {}", id);
        iServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
