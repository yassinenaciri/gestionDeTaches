package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Chef;
import com.mycompany.myapp.domain.Direction;
import com.mycompany.myapp.repository.DirectionRepository;
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
 * Integration tests for the {@link DirectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirectionResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/directions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirectionMockMvc;

    private Direction direction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direction createEntity(EntityManager em) {
        Direction direction = new Direction().nom(DEFAULT_NOM);
        // Add required entity
        Chef chef;
        if (TestUtil.findAll(em, Chef.class).isEmpty()) {
            chef = ChefResourceIT.createEntity(em);
            em.persist(chef);
            em.flush();
        } else {
            chef = TestUtil.findAll(em, Chef.class).get(0);
        }
        direction.setDirecteur(chef);
        return direction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direction createUpdatedEntity(EntityManager em) {
        Direction direction = new Direction().nom(UPDATED_NOM);
        // Add required entity
        Chef chef;
        if (TestUtil.findAll(em, Chef.class).isEmpty()) {
            chef = ChefResourceIT.createUpdatedEntity(em);
            em.persist(chef);
            em.flush();
        } else {
            chef = TestUtil.findAll(em, Chef.class).get(0);
        }
        direction.setDirecteur(chef);
        return direction;
    }

    @BeforeEach
    public void initTest() {
        direction = createEntity(em);
    }

    @Test
    @Transactional
    void createDirection() throws Exception {
        int databaseSizeBeforeCreate = directionRepository.findAll().size();
        // Create the Direction
        restDirectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isCreated());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeCreate + 1);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createDirectionWithExistingId() throws Exception {
        // Create the Direction with an existing ID
        direction.setId(1L);

        int databaseSizeBeforeCreate = directionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRepository.findAll().size();
        // set the field null
        direction.setNom(null);

        // Create the Direction, which fails.

        restDirectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDirections() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        // Get all the directionList
        restDirectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direction.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        // Get the direction
        restDirectionMockMvc
            .perform(get(ENTITY_API_URL_ID, direction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direction.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingDirection() throws Exception {
        // Get the direction
        restDirectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        int databaseSizeBeforeUpdate = directionRepository.findAll().size();

        // Update the direction
        Direction updatedDirection = directionRepository.findById(direction.getId()).get();
        // Disconnect from session so that the updates on updatedDirection are not directly saved in db
        em.detach(updatedDirection);
        updatedDirection.nom(UPDATED_NOM);

        restDirectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDirection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDirection))
            )
            .andExpect(status().isOk());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();
        direction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();
        direction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();
        direction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirectionWithPatch() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        int databaseSizeBeforeUpdate = directionRepository.findAll().size();

        // Update the direction using partial update
        Direction partialUpdatedDirection = new Direction();
        partialUpdatedDirection.setId(direction.getId());

        restDirectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirection))
            )
            .andExpect(status().isOk());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateDirectionWithPatch() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        int databaseSizeBeforeUpdate = directionRepository.findAll().size();

        // Update the direction using partial update
        Direction partialUpdatedDirection = new Direction();
        partialUpdatedDirection.setId(direction.getId());

        partialUpdatedDirection.nom(UPDATED_NOM);

        restDirectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirection))
            )
            .andExpect(status().isOk());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();
        direction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();
        direction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();
        direction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(direction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        int databaseSizeBeforeDelete = directionRepository.findAll().size();

        // Delete the direction
        restDirectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, direction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
