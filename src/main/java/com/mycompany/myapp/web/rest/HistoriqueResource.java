package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Historique;
import com.mycompany.myapp.repository.HistoriqueRepository;
import com.mycompany.myapp.service.HistoriqueService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Historique}.
 */
@RestController
@RequestMapping("/api")
public class HistoriqueResource {

    private final Logger log = LoggerFactory.getLogger(HistoriqueResource.class);

    private static final String ENTITY_NAME = "historique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueService historiqueService;

    private final HistoriqueRepository historiqueRepository;

    public HistoriqueResource(HistoriqueService historiqueService, HistoriqueRepository historiqueRepository) {
        this.historiqueService = historiqueService;
        this.historiqueRepository = historiqueRepository;
    }

    /**
     * {@code POST  /historiques} : Create a new historique.
     *
     * @param historique the historique to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historique, or with status {@code 400 (Bad Request)} if the historique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historiques")
    public ResponseEntity<Historique> createHistorique(@Valid @RequestBody Historique historique) throws URISyntaxException {
        log.debug("REST request to save Historique : {}", historique);
        if (historique.getId() != null) {
            throw new BadRequestAlertException("A new historique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Historique result = historiqueService.save(historique);
        return ResponseEntity
            .created(new URI("/api/historiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historiques/:id} : Updates an existing historique.
     *
     * @param id the id of the historique to save.
     * @param historique the historique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historique,
     * or with status {@code 400 (Bad Request)} if the historique is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historiques/{id}")
    public ResponseEntity<Historique> updateHistorique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Historique historique
    ) throws URISyntaxException {
        log.debug("REST request to update Historique : {}, {}", id, historique);
        if (historique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Historique result = historiqueService.save(historique);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historique.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /historiques/:id} : Partial updates given fields of an existing historique, field will ignore if it is null
     *
     * @param id the id of the historique to save.
     * @param historique the historique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historique,
     * or with status {@code 400 (Bad Request)} if the historique is not valid,
     * or with status {@code 404 (Not Found)} if the historique is not found,
     * or with status {@code 500 (Internal Server Error)} if the historique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historiques/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Historique> partialUpdateHistorique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Historique historique
    ) throws URISyntaxException {
        log.debug("REST request to partial update Historique partially : {}, {}", id, historique);
        if (historique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Historique> result = historiqueService.partialUpdate(historique);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historique.getId().toString())
        );
    }

    /**
     * {@code GET  /historiques} : get all the historiques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiques in body.
     */
    @GetMapping("/historiques")
    public ResponseEntity<List<Historique>> getAllHistoriques(Pageable pageable) {
        log.debug("REST request to get a page of Historiques");
        Page<Historique> page = historiqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historiques/:id} : get the "id" historique.
     *
     * @param id the id of the historique to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historique, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historiques/{id}")
    public ResponseEntity<Historique> getHistorique(@PathVariable Long id) {
        log.debug("REST request to get Historique : {}", id);
        Optional<Historique> historique = historiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historique);
    }

    /**
     * {@code DELETE  /historiques/:id} : delete the "id" historique.
     *
     * @param id the id of the historique to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historiques/{id}")
    public ResponseEntity<Void> deleteHistorique(@PathVariable Long id) {
        log.debug("REST request to delete Historique : {}", id);
        historiqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
