package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Chef;
import com.mycompany.myapp.domain.Direction;
import com.mycompany.myapp.domain.Pole;
import com.mycompany.myapp.repository.PoleRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoleResourceIT {

    private static final String DEFAULT_NOM_POLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_POLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/poles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoleRepository poleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoleMockMvc;

    private Pole pole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pole createEntity(EntityManager em) {
        Pole pole = new Pole().nomPole(DEFAULT_NOM_POLE);
        // Add required entity
        Chef chef;
        if (TestUtil.findAll(em, Chef.class).isEmpty()) {
            chef = ChefResourceIT.createEntity(em);
            em.persist(chef);
            em.flush();
        } else {
            chef = TestUtil.findAll(em, Chef.class).get(0);
        }
        pole.setChef(chef);
        // Add required entity
        Direction direction;
        if (TestUtil.findAll(em, Direction.class).isEmpty()) {
            direction = DirectionResourceIT.createEntity(em);
            em.persist(direction);
            em.flush();
        } else {
            direction = TestUtil.findAll(em, Direction.class).get(0);
        }
        pole.setDirection(direction);
        return pole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pole createUpdatedEntity(EntityManager em) {
        Pole pole = new Pole().nomPole(UPDATED_NOM_POLE);
        // Add required entity
        Chef chef;
        if (TestUtil.findAll(em, Chef.class).isEmpty()) {
            chef = ChefResourceIT.createUpdatedEntity(em);
            em.persist(chef);
            em.flush();
        } else {
            chef = TestUtil.findAll(em, Chef.class).get(0);
        }
        pole.setChef(chef);
        // Add required entity
        Direction direction;
        if (TestUtil.findAll(em, Direction.class).isEmpty()) {
            direction = DirectionResourceIT.createUpdatedEntity(em);
            em.persist(direction);
            em.flush();
        } else {
            direction = TestUtil.findAll(em, Direction.class).get(0);
        }
        pole.setDirection(direction);
        return pole;
    }

    @BeforeEach
    public void initTest() {
        pole = createEntity(em);
    }

    @Test
    @Transactional
    void createPole() throws Exception {
        int databaseSizeBeforeCreate = poleRepository.findAll().size();
        // Create the Pole
        restPoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pole)))
            .andExpect(status().isCreated());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeCreate + 1);
        Pole testPole = poleList.get(poleList.size() - 1);
        assertThat(testPole.getNomPole()).isEqualTo(DEFAULT_NOM_POLE);
    }

    @Test
    @Transactional
    void createPoleWithExistingId() throws Exception {
        // Create the Pole with an existing ID
        pole.setId(1L);

        int databaseSizeBeforeCreate = poleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pole)))
            .andExpect(status().isBadRequest());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = poleRepository.findAll().size();
        // set the field null
        pole.setNomPole(null);

        // Create the Pole, which fails.

        restPoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pole)))
            .andExpect(status().isBadRequest());

        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPoles() throws Exception {
        // Initialize the database
        poleRepository.saveAndFlush(pole);

        // Get all the poleList
        restPoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pole.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPole").value(hasItem(DEFAULT_NOM_POLE)));
    }

    @Test
    @Transactional
    void getPole() throws Exception {
        // Initialize the database
        poleRepository.saveAndFlush(pole);

        // Get the pole
        restPoleMockMvc
            .perform(get(ENTITY_API_URL_ID, pole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pole.getId().intValue()))
            .andExpect(jsonPath("$.nomPole").value(DEFAULT_NOM_POLE));
    }

    @Test
    @Transactional
    void getNonExistingPole() throws Exception {
        // Get the pole
        restPoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPole() throws Exception {
        // Initialize the database
        poleRepository.saveAndFlush(pole);

        int databaseSizeBeforeUpdate = poleRepository.findAll().size();

        // Update the pole
        Pole updatedPole = poleRepository.findById(pole.getId()).get();
        // Disconnect from session so that the updates on updatedPole are not directly saved in db
        em.detach(updatedPole);
        updatedPole.nomPole(UPDATED_NOM_POLE);

        restPoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPole))
            )
            .andExpect(status().isOk());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
        Pole testPole = poleList.get(poleList.size() - 1);
        assertThat(testPole.getNomPole()).isEqualTo(UPDATED_NOM_POLE);
    }

    @Test
    @Transactional
    void putNonExistingPole() throws Exception {
        int databaseSizeBeforeUpdate = poleRepository.findAll().size();
        pole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPole() throws Exception {
        int databaseSizeBeforeUpdate = poleRepository.findAll().size();
        pole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPole() throws Exception {
        int databaseSizeBeforeUpdate = poleRepository.findAll().size();
        pole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoleWithPatch() throws Exception {
        // Initialize the database
        poleRepository.saveAndFlush(pole);

        int databaseSizeBeforeUpdate = poleRepository.findAll().size();

        // Update the pole using partial update
        Pole partialUpdatedPole = new Pole();
        partialUpdatedPole.setId(pole.getId());

        restPoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPole))
            )
            .andExpect(status().isOk());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
        Pole testPole = poleList.get(poleList.size() - 1);
        assertThat(testPole.getNomPole()).isEqualTo(DEFAULT_NOM_POLE);
    }

    @Test
    @Transactional
    void fullUpdatePoleWithPatch() throws Exception {
        // Initialize the database
        poleRepository.saveAndFlush(pole);

        int databaseSizeBeforeUpdate = poleRepository.findAll().size();

        // Update the pole using partial update
        Pole partialUpdatedPole = new Pole();
        partialUpdatedPole.setId(pole.getId());

        partialUpdatedPole.nomPole(UPDATED_NOM_POLE);

        restPoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPole))
            )
            .andExpect(status().isOk());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
        Pole testPole = poleList.get(poleList.size() - 1);
        assertThat(testPole.getNomPole()).isEqualTo(UPDATED_NOM_POLE);
    }

    @Test
    @Transactional
    void patchNonExistingPole() throws Exception {
        int databaseSizeBeforeUpdate = poleRepository.findAll().size();
        pole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPole() throws Exception {
        int databaseSizeBeforeUpdate = poleRepository.findAll().size();
        pole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPole() throws Exception {
        int databaseSizeBeforeUpdate = poleRepository.findAll().size();
        pole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pole in the database
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePole() throws Exception {
        // Initialize the database
        poleRepository.saveAndFlush(pole);

        int databaseSizeBeforeDelete = poleRepository.findAll().size();

        // Delete the pole
        restPoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, pole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pole> poleList = poleRepository.findAll();
        assertThat(poleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
