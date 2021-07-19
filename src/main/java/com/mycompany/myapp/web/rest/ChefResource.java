package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Chef;
import com.mycompany.myapp.repository.ChefRepository;
import com.mycompany.myapp.service.ChefService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Chef}.
 */
@RestController
@RequestMapping("/api")
public class ChefResource {

    private final Logger log = LoggerFactory.getLogger(ChefResource.class);

    private static final String ENTITY_NAME = "chef";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChefService chefService;

    private final ChefRepository chefRepository;

    public ChefResource(ChefService chefService, ChefRepository chefRepository) {
        this.chefService = chefService;
        this.chefRepository = chefRepository;
    }

    /**
     * {@code POST  /chefs} : Create a new chef.
     *
     * @param chef the chef to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chef, or with status {@code 400 (Bad Request)} if the chef has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chefs")
    public ResponseEntity<Chef> createChef(@Valid @RequestBody Chef chef) throws URISyntaxException {
        log.debug("REST request to save Chef : {}", chef);
        if (chef.getId() != null) {
            throw new BadRequestAlertException("A new chef cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Chef result = chefService.save(chef);
        return ResponseEntity
            .created(new URI("/api/chefs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chefs/:id} : Updates an existing chef.
     *
     * @param id the id of the chef to save.
     * @param chef the chef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chef,
     * or with status {@code 400 (Bad Request)} if the chef is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chefs/{id}")
    public ResponseEntity<Chef> updateChef(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Chef chef)
        throws URISyntaxException {
        log.debug("REST request to update Chef : {}, {}", id, chef);
        if (chef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Chef result = chefService.save(chef);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chef.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chefs/:id} : Partial updates given fields of an existing chef, field will ignore if it is null
     *
     * @param id the id of the chef to save.
     * @param chef the chef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chef,
     * or with status {@code 400 (Bad Request)} if the chef is not valid,
     * or with status {@code 404 (Not Found)} if the chef is not found,
     * or with status {@code 500 (Internal Server Error)} if the chef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chefs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Chef> partialUpdateChef(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Chef chef
    ) throws URISyntaxException {
        log.debug("REST request to partial update Chef partially : {}, {}", id, chef);
        if (chef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Chef> result = chefService.partialUpdate(chef);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chef.getId().toString())
        );
    }

    /**
     * {@code GET  /chefs} : get all the chefs.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chefs in body.
     */
    @GetMapping("/chefs")
    public ResponseEntity<List<Chef>> getAllChefs(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("service-is-null".equals(filter)) {
            log.debug("REST request to get all Chefs where service is null");
            return new ResponseEntity<>(chefService.findAllWhereServiceIsNull(), HttpStatus.OK);
        }

        if ("pole-is-null".equals(filter)) {
            log.debug("REST request to get all Chefs where pole is null");
            return new ResponseEntity<>(chefService.findAllWherePoleIsNull(), HttpStatus.OK);
        }

        if ("direction-is-null".equals(filter)) {
            log.debug("REST request to get all Chefs where direction is null");
            return new ResponseEntity<>(chefService.findAllWhereDirectionIsNull(), HttpStatus.OK);
        }

        if ("division-is-null".equals(filter)) {
            log.debug("REST request to get all Chefs where division is null");
            return new ResponseEntity<>(chefService.findAllWhereDivisionIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Chefs");
        Page<Chef> page = chefService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chefs/:id} : get the "id" chef.
     *
     * @param id the id of the chef to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chef, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chefs/{id}")
    public ResponseEntity<Chef> getChef(@PathVariable Long id) {
        log.debug("REST request to get Chef : {}", id);
        Optional<Chef> chef = chefService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chef);
    }

    /**
     * {@code DELETE  /chefs/:id} : delete the "id" chef.
     *
     * @param id the id of the chef to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chefs/{id}")
    public ResponseEntity<Void> deleteChef(@PathVariable Long id) {
        log.debug("REST request to delete Chef : {}", id);
        chefService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
