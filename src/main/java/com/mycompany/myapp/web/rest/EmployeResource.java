package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Classement;
import com.mycompany.myapp.domain.Employe;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.EmployeRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.EmployeService;
import com.mycompany.myapp.service.UserService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Employe}.
 */
@RestController
@RequestMapping("/api")
public class EmployeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeResource.class);

    private static final String ENTITY_NAME = "employe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeService employeService;

    private final EmployeRepository employeRepository;

    private final UserService userService;

    public EmployeResource(EmployeService employeService, EmployeRepository employeRepository, UserService userService) {
        this.employeService = employeService;
        this.employeRepository = employeRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /employes} : Create a new employe.
     *
     * @param employe the employe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employe, or with status {@code 400 (Bad Request)} if the employe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employes")
    public ResponseEntity<Employe> createEmploye(@Valid @RequestBody Employe employe) throws URISyntaxException {
        log.debug("REST request to save Employe : {}", employe);
        if (employe.getId() != null) {
            throw new BadRequestAlertException("A new employe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Employe result = employeService.save(employe);
        User user = employe.getCompte();
        if (user != null) {
            userService.setRole(user.getId(), AuthoritiesConstants.CADRE);
        }
        return ResponseEntity
            .created(new URI("/api/employes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employes/:id} : Updates an existing employe.
     *
     * @param id the id of the employe to save.
     * @param employe the employe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employe,
     * or with status {@code 400 (Bad Request)} if the employe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employes/{id}")
    public ResponseEntity<Employe> updateEmploye(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Employe employe
    ) throws URISyntaxException {
        log.debug("REST request to update Employe : {}, {}", id, employe);
        if (employe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Employe result = employeService.save(employe);
        User user = employe.getCompte();
        if (user != null) {
            userService.setRole(user.getId(), AuthoritiesConstants.CADRE);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employes/:id} : Partial updates given fields of an existing employe, field will ignore if it is null
     *
     * @param id the id of the employe to save.
     * @param employe the employe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employe,
     * or with status {@code 400 (Bad Request)} if the employe is not valid,
     * or with status {@code 404 (Not Found)} if the employe is not found,
     * or with status {@code 500 (Internal Server Error)} if the employe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Employe> partialUpdateEmploye(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Employe employe
    ) throws URISyntaxException {
        log.debug("REST request to partial update Employe partially : {}, {}", id, employe);
        if (employe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Employe> result = employeService.partialUpdate(employe);
        User user = employe.getCompte();
        if (user != null) {
            userService.setRole(user.getId(), AuthoritiesConstants.CADRE);
        }

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employe.getId().toString())
        );
    }

    /**
     * {@code GET  /employes} : get all the employes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employes in body.
     */
    @GetMapping("/employes")
    public ResponseEntity<List<Employe>> getAllEmployes(Pageable pageable) {
        log.debug("REST request to get a page of Employes");
        Page<Employe> page = employeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employes/:id} : get the "id" employe.
     *
     * @param id the id of the employe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employes/{id}")
    public ResponseEntity<Employe> getEmploye(@PathVariable Long id) {
        log.debug("REST request to get Employe : {}", id);
        Optional<Employe> employe = employeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employe);
    }

    @GetMapping("/employes/classement")
    public Classement[] getClassement() {
        return employeService.findClassementByService();
    }

    /**
     * {@code DELETE  /employes/:id} : delete the "id" employe.
     *
     * @param id the id of the employe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employes/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        log.debug("REST request to delete Employe : {}", id);
        employeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
