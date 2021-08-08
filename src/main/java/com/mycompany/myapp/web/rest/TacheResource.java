package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Stats;
import com.mycompany.myapp.domain.Tache;
import com.mycompany.myapp.domain.enumeration.Etat;
import com.mycompany.myapp.repository.TacheRepository;
import com.mycompany.myapp.service.TacheService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Tache}.
 */
@RestController
@RequestMapping("/api")
public class TacheResource {

    private final Logger log = LoggerFactory.getLogger(TacheResource.class);

    private static final String ENTITY_NAME = "tache";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TacheService tacheService;

    private final TacheRepository tacheRepository;

    public TacheResource(TacheService tacheService, TacheRepository tacheRepository) {
        this.tacheService = tacheService;
        this.tacheRepository = tacheRepository;
    }

    /**
     * {@code POST  /taches} : Create a new tache.
     *
     * @param tache the tache to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tache, or with status {@code 400 (Bad Request)} if the tache has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/taches")
    public ResponseEntity<Tache> createTache(@Valid @RequestBody Tache tache) throws URISyntaxException {
        log.debug("REST request to save Tache : {}", tache);
        if (tache.getId() != null) {
            throw new BadRequestAlertException("A new tache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tache result = tacheService.save(tache);
        return ResponseEntity
            .created(new URI("/api/taches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /taches/:id} : Updates an existing tache.
     *
     * @param id the id of the tache to save.
     * @param tache the tache to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tache,
     * or with status {@code 400 (Bad Request)} if the tache is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tache couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/taches/{id}")
    public ResponseEntity<Tache> updateTache(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Tache tache)
        throws URISyntaxException {
        log.debug("REST request to update Tache : {}, {}", id, tache);
        if (tache.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tache.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tache result = tacheService.save(tache);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tache.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /taches/:id} : Partial updates given fields of an existing tache, field will ignore if it is null
     *
     * @param id the id of the tache to save.
     * @param tache the tache to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tache,
     * or with status {@code 400 (Bad Request)} if the tache is not valid,
     * or with status {@code 404 (Not Found)} if the tache is not found,
     * or with status {@code 500 (Internal Server Error)} if the tache couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/taches/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Tache> partialUpdateTache(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tache tache
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tache partially : {}, {}", id, tache);
        if (tache.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tache.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tache> result = tacheService.partialUpdate(tache);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tache.getId().toString())
        );
    }

    /**
     * {@code GET  /taches} : get all the taches.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taches in body.
     */
    @GetMapping("/taches")
    public ResponseEntity<List<Tache>> getAllTaches(Pageable pageable, @RequestParam(required = false) String filter) {
        log.debug("REST request to get a page of Taches");
        Page<Tache> page;
        if (filter != null) {
            page = tacheService.findByFiltre(pageable, filter);
        } else {
            page = tacheService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/taches/getList")
    public Tache[] getListAllTaches(@RequestParam String filter) {
        log.debug("REST request to get a List of Taches");
        return tacheService.findListByFiltre(filter);
    }

    /**
     * {@code GET  /taches/:id} : get the "id" tache.
     *
     * @param id the id of the tache to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tache, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/taches/{id}")
    public ResponseEntity<Tache> getTache(@PathVariable Long id) {
        log.debug("REST request to get Tache : {}", id);
        Optional<Tache> tache = tacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tache);
    }

    @GetMapping("/taches/stats")
    public Stats getStats(@RequestParam(required = false) String id) {
        log.debug("REST request to get Stats : {}" + id);
        Stats stats;

        stats = tacheService.findStatsForUser();

        return (stats);
    }

    @GetMapping("/taches/stats/employe/{id}")
    public Stats getStatsForEmploye(@PathVariable Long id) {
        log.debug("REST request to get Stats : {}" + id);
        Stats stats;
        stats = tacheService.findStatsByEmploye(id);

        return (stats);
    }

    /**
     * {@code DELETE  /taches/:id} : delete the "id" tache.
     *
     * @param id the id of the tache to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/taches/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable Long id) {
        log.debug("REST request to delete Tache : {}", id);
        tacheService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/taches/updateEtat/{id}")
    public ResponseEntity<Tache> updateEtat(@PathVariable Long id, @RequestBody String nouveauEtat) {
        nouveauEtat = nouveauEtat.substring(0, nouveauEtat.length() - 1);
        Tache result = tacheService.updateEtat(id, nouveauEtat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }
}
