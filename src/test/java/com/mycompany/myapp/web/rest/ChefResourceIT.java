package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Chef;
import com.mycompany.myapp.repository.ChefRepository;
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
 * Integration tests for the {@link ChefResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChefResourceIT {

    private static final String DEFAULT_NOM_COMPLET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chefs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChefMockMvc;

    private Chef chef;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chef createEntity(EntityManager em) {
        Chef chef = new Chef().nomComplet(DEFAULT_NOM_COMPLET).role(DEFAULT_ROLE);
        return chef;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chef createUpdatedEntity(EntityManager em) {
        Chef chef = new Chef().nomComplet(UPDATED_NOM_COMPLET).role(UPDATED_ROLE);
        return chef;
    }

    @BeforeEach
    public void initTest() {
        chef = createEntity(em);
    }

    @Test
    @Transactional
    void createChef() throws Exception {
        int databaseSizeBeforeCreate = chefRepository.findAll().size();
        // Create the Chef
        restChefMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chef)))
            .andExpect(status().isCreated());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeCreate + 1);
        Chef testChef = chefList.get(chefList.size() - 1);
        assertThat(testChef.getNomComplet()).isEqualTo(DEFAULT_NOM_COMPLET);
        assertThat(testChef.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void createChefWithExistingId() throws Exception {
        // Create the Chef with an existing ID
        chef.setId(1L);

        int databaseSizeBeforeCreate = chefRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChefMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chef)))
            .andExpect(status().isBadRequest());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomCompletIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefRepository.findAll().size();
        // set the field null
        chef.setNomComplet(null);

        // Create the Chef, which fails.

        restChefMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chef)))
            .andExpect(status().isBadRequest());

        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChefs() throws Exception {
        // Initialize the database
        chefRepository.saveAndFlush(chef);

        // Get all the chefList
        restChefMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chef.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomComplet").value(hasItem(DEFAULT_NOM_COMPLET)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }

    @Test
    @Transactional
    void getChef() throws Exception {
        // Initialize the database
        chefRepository.saveAndFlush(chef);

        // Get the chef
        restChefMockMvc
            .perform(get(ENTITY_API_URL_ID, chef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chef.getId().intValue()))
            .andExpect(jsonPath("$.nomComplet").value(DEFAULT_NOM_COMPLET))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingChef() throws Exception {
        // Get the chef
        restChefMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChef() throws Exception {
        // Initialize the database
        chefRepository.saveAndFlush(chef);

        int databaseSizeBeforeUpdate = chefRepository.findAll().size();

        // Update the chef
        Chef updatedChef = chefRepository.findById(chef.getId()).get();
        // Disconnect from session so that the updates on updatedChef are not directly saved in db
        em.detach(updatedChef);
        updatedChef.nomComplet(UPDATED_NOM_COMPLET).role(UPDATED_ROLE);

        restChefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChef))
            )
            .andExpect(status().isOk());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
        Chef testChef = chefList.get(chefList.size() - 1);
        assertThat(testChef.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testChef.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingChef() throws Exception {
        int databaseSizeBeforeUpdate = chefRepository.findAll().size();
        chef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chef))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChef() throws Exception {
        int databaseSizeBeforeUpdate = chefRepository.findAll().size();
        chef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chef))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChef() throws Exception {
        int databaseSizeBeforeUpdate = chefRepository.findAll().size();
        chef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chef)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChefWithPatch() throws Exception {
        // Initialize the database
        chefRepository.saveAndFlush(chef);

        int databaseSizeBeforeUpdate = chefRepository.findAll().size();

        // Update the chef using partial update
        Chef partialUpdatedChef = new Chef();
        partialUpdatedChef.setId(chef.getId());

        partialUpdatedChef.nomComplet(UPDATED_NOM_COMPLET).role(UPDATED_ROLE);

        restChefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChef))
            )
            .andExpect(status().isOk());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
        Chef testChef = chefList.get(chefList.size() - 1);
        assertThat(testChef.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testChef.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateChefWithPatch() throws Exception {
        // Initialize the database
        chefRepository.saveAndFlush(chef);

        int databaseSizeBeforeUpdate = chefRepository.findAll().size();

        // Update the chef using partial update
        Chef partialUpdatedChef = new Chef();
        partialUpdatedChef.setId(chef.getId());

        partialUpdatedChef.nomComplet(UPDATED_NOM_COMPLET).role(UPDATED_ROLE);

        restChefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChef))
            )
            .andExpect(status().isOk());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
        Chef testChef = chefList.get(chefList.size() - 1);
        assertThat(testChef.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testChef.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingChef() throws Exception {
        int databaseSizeBeforeUpdate = chefRepository.findAll().size();
        chef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chef))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChef() throws Exception {
        int databaseSizeBeforeUpdate = chefRepository.findAll().size();
        chef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chef))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChef() throws Exception {
        int databaseSizeBeforeUpdate = chefRepository.findAll().size();
        chef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChefMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chef)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chef in the database
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChef() throws Exception {
        // Initialize the database
        chefRepository.saveAndFlush(chef);

        int databaseSizeBeforeDelete = chefRepository.findAll().size();

        // Delete the chef
        restChefMockMvc
            .perform(delete(ENTITY_API_URL_ID, chef.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chef> chefList = chefRepository.findAll();
        assertThat(chefList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
