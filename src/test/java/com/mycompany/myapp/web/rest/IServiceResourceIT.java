package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Chef;
import com.mycompany.myapp.domain.Division;
import com.mycompany.myapp.domain.IService;
import com.mycompany.myapp.repository.IServiceRepository;
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
 * Integration tests for the {@link IServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IServiceResourceIT {

    private static final String DEFAULT_NOM_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SERVICE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/i-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IServiceRepository iServiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIServiceMockMvc;

    private IService iService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IService createEntity(EntityManager em) {
        IService iService = new IService().nomService(DEFAULT_NOM_SERVICE);
        // Add required entity
        Chef chef;
        if (TestUtil.findAll(em, Chef.class).isEmpty()) {
            chef = ChefResourceIT.createEntity(em);
            em.persist(chef);
            em.flush();
        } else {
            chef = TestUtil.findAll(em, Chef.class).get(0);
        }
        iService.setChef(chef);
        // Add required entity
        Division division;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            division = DivisionResourceIT.createEntity(em);
            em.persist(division);
            em.flush();
        } else {
            division = TestUtil.findAll(em, Division.class).get(0);
        }
        iService.setDivision(division);
        return iService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IService createUpdatedEntity(EntityManager em) {
        IService iService = new IService().nomService(UPDATED_NOM_SERVICE);
        // Add required entity
        Chef chef;
        if (TestUtil.findAll(em, Chef.class).isEmpty()) {
            chef = ChefResourceIT.createUpdatedEntity(em);
            em.persist(chef);
            em.flush();
        } else {
            chef = TestUtil.findAll(em, Chef.class).get(0);
        }
        iService.setChef(chef);
        // Add required entity
        Division division;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            division = DivisionResourceIT.createUpdatedEntity(em);
            em.persist(division);
            em.flush();
        } else {
            division = TestUtil.findAll(em, Division.class).get(0);
        }
        iService.setDivision(division);
        return iService;
    }

    @BeforeEach
    public void initTest() {
        iService = createEntity(em);
    }

    @Test
    @Transactional
    void createIService() throws Exception {
        int databaseSizeBeforeCreate = iServiceRepository.findAll().size();
        // Create the IService
        restIServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iService)))
            .andExpect(status().isCreated());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeCreate + 1);
        IService testIService = iServiceList.get(iServiceList.size() - 1);
        assertThat(testIService.getNomService()).isEqualTo(DEFAULT_NOM_SERVICE);
    }

    @Test
    @Transactional
    void createIServiceWithExistingId() throws Exception {
        // Create the IService with an existing ID
        iService.setId(1L);

        int databaseSizeBeforeCreate = iServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iService)))
            .andExpect(status().isBadRequest());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomServiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = iServiceRepository.findAll().size();
        // set the field null
        iService.setNomService(null);

        // Create the IService, which fails.

        restIServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iService)))
            .andExpect(status().isBadRequest());

        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIServices() throws Exception {
        // Initialize the database
        iServiceRepository.saveAndFlush(iService);

        // Get all the iServiceList
        restIServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iService.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomService").value(hasItem(DEFAULT_NOM_SERVICE)));
    }

    @Test
    @Transactional
    void getIService() throws Exception {
        // Initialize the database
        iServiceRepository.saveAndFlush(iService);

        // Get the iService
        restIServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, iService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iService.getId().intValue()))
            .andExpect(jsonPath("$.nomService").value(DEFAULT_NOM_SERVICE));
    }

    @Test
    @Transactional
    void getNonExistingIService() throws Exception {
        // Get the iService
        restIServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIService() throws Exception {
        // Initialize the database
        iServiceRepository.saveAndFlush(iService);

        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();

        // Update the iService
        IService updatedIService = iServiceRepository.findById(iService.getId()).get();
        // Disconnect from session so that the updates on updatedIService are not directly saved in db
        em.detach(updatedIService);
        updatedIService.nomService(UPDATED_NOM_SERVICE);

        restIServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIService))
            )
            .andExpect(status().isOk());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
        IService testIService = iServiceList.get(iServiceList.size() - 1);
        assertThat(testIService.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
    }

    @Test
    @Transactional
    void putNonExistingIService() throws Exception {
        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();
        iService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iService))
            )
            .andExpect(status().isBadRequest());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIService() throws Exception {
        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();
        iService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iService))
            )
            .andExpect(status().isBadRequest());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIService() throws Exception {
        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();
        iService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIServiceWithPatch() throws Exception {
        // Initialize the database
        iServiceRepository.saveAndFlush(iService);

        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();

        // Update the iService using partial update
        IService partialUpdatedIService = new IService();
        partialUpdatedIService.setId(iService.getId());

        partialUpdatedIService.nomService(UPDATED_NOM_SERVICE);

        restIServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIService))
            )
            .andExpect(status().isOk());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
        IService testIService = iServiceList.get(iServiceList.size() - 1);
        assertThat(testIService.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
    }

    @Test
    @Transactional
    void fullUpdateIServiceWithPatch() throws Exception {
        // Initialize the database
        iServiceRepository.saveAndFlush(iService);

        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();

        // Update the iService using partial update
        IService partialUpdatedIService = new IService();
        partialUpdatedIService.setId(iService.getId());

        partialUpdatedIService.nomService(UPDATED_NOM_SERVICE);

        restIServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIService))
            )
            .andExpect(status().isOk());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
        IService testIService = iServiceList.get(iServiceList.size() - 1);
        assertThat(testIService.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
    }

    @Test
    @Transactional
    void patchNonExistingIService() throws Exception {
        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();
        iService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iService))
            )
            .andExpect(status().isBadRequest());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIService() throws Exception {
        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();
        iService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iService))
            )
            .andExpect(status().isBadRequest());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIService() throws Exception {
        int databaseSizeBeforeUpdate = iServiceRepository.findAll().size();
        iService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(iService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IService in the database
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIService() throws Exception {
        // Initialize the database
        iServiceRepository.saveAndFlush(iService);

        int databaseSizeBeforeDelete = iServiceRepository.findAll().size();

        // Delete the iService
        restIServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, iService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IService> iServiceList = iServiceRepository.findAll();
        assertThat(iServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
