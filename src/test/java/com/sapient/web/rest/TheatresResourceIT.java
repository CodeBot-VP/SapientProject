package com.sapient.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapient.IntegrationTest;
import com.sapient.domain.Theatres;
import com.sapient.repository.TheatresRepository;
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
 * Integration tests for the {@link TheatresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TheatresResourceIT {

    private static final String DEFAULT_THEATRE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_THEATRE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_THEATRE_CITY = "AAAAAAAAAA";
    private static final String UPDATED_THEATRE_CITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/theatres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TheatresRepository theatresRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTheatresMockMvc;

    private Theatres theatres;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theatres createEntity(EntityManager em) {
        Theatres theatres = new Theatres().theatreName(DEFAULT_THEATRE_NAME).theatreCity(DEFAULT_THEATRE_CITY);
        return theatres;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theatres createUpdatedEntity(EntityManager em) {
        Theatres theatres = new Theatres().theatreName(UPDATED_THEATRE_NAME).theatreCity(UPDATED_THEATRE_CITY);
        return theatres;
    }

    @BeforeEach
    public void initTest() {
        theatres = createEntity(em);
    }

    @Test
    @Transactional
    void createTheatres() throws Exception {
        int databaseSizeBeforeCreate = theatresRepository.findAll().size();
        // Create the Theatres
        restTheatresMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isCreated());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeCreate + 1);
        Theatres testTheatres = theatresList.get(theatresList.size() - 1);
        assertThat(testTheatres.getTheatreName()).isEqualTo(DEFAULT_THEATRE_NAME);
        assertThat(testTheatres.getTheatreCity()).isEqualTo(DEFAULT_THEATRE_CITY);
    }

    @Test
    @Transactional
    void createTheatresWithExistingId() throws Exception {
        // Create the Theatres with an existing ID
        theatres.setId(1L);

        int databaseSizeBeforeCreate = theatresRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTheatresMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTheatres() throws Exception {
        // Initialize the database
        theatresRepository.saveAndFlush(theatres);

        // Get all the theatresList
        restTheatresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theatres.getId().intValue())))
            .andExpect(jsonPath("$.[*].theatreName").value(hasItem(DEFAULT_THEATRE_NAME)))
            .andExpect(jsonPath("$.[*].theatreCity").value(hasItem(DEFAULT_THEATRE_CITY)));
    }

    @Test
    @Transactional
    void getTheatres() throws Exception {
        // Initialize the database
        theatresRepository.saveAndFlush(theatres);

        // Get the theatres
        restTheatresMockMvc
            .perform(get(ENTITY_API_URL_ID, theatres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(theatres.getId().intValue()))
            .andExpect(jsonPath("$.theatreName").value(DEFAULT_THEATRE_NAME))
            .andExpect(jsonPath("$.theatreCity").value(DEFAULT_THEATRE_CITY));
    }

    @Test
    @Transactional
    void getNonExistingTheatres() throws Exception {
        // Get the theatres
        restTheatresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTheatres() throws Exception {
        // Initialize the database
        theatresRepository.saveAndFlush(theatres);

        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();

        // Update the theatres
        Theatres updatedTheatres = theatresRepository.findById(theatres.getId()).get();
        // Disconnect from session so that the updates on updatedTheatres are not directly saved in db
        em.detach(updatedTheatres);
        updatedTheatres.theatreName(UPDATED_THEATRE_NAME).theatreCity(UPDATED_THEATRE_CITY);

        restTheatresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTheatres.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTheatres))
            )
            .andExpect(status().isOk());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
        Theatres testTheatres = theatresList.get(theatresList.size() - 1);
        assertThat(testTheatres.getTheatreName()).isEqualTo(UPDATED_THEATRE_NAME);
        assertThat(testTheatres.getTheatreCity()).isEqualTo(UPDATED_THEATRE_CITY);
    }

    @Test
    @Transactional
    void putNonExistingTheatres() throws Exception {
        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();
        theatres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTheatresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, theatres.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTheatres() throws Exception {
        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();
        theatres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTheatres() throws Exception {
        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();
        theatres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatresMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTheatresWithPatch() throws Exception {
        // Initialize the database
        theatresRepository.saveAndFlush(theatres);

        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();

        // Update the theatres using partial update
        Theatres partialUpdatedTheatres = new Theatres();
        partialUpdatedTheatres.setId(theatres.getId());

        partialUpdatedTheatres.theatreName(UPDATED_THEATRE_NAME).theatreCity(UPDATED_THEATRE_CITY);

        restTheatresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheatres.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheatres))
            )
            .andExpect(status().isOk());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
        Theatres testTheatres = theatresList.get(theatresList.size() - 1);
        assertThat(testTheatres.getTheatreName()).isEqualTo(UPDATED_THEATRE_NAME);
        assertThat(testTheatres.getTheatreCity()).isEqualTo(UPDATED_THEATRE_CITY);
    }

    @Test
    @Transactional
    void fullUpdateTheatresWithPatch() throws Exception {
        // Initialize the database
        theatresRepository.saveAndFlush(theatres);

        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();

        // Update the theatres using partial update
        Theatres partialUpdatedTheatres = new Theatres();
        partialUpdatedTheatres.setId(theatres.getId());

        partialUpdatedTheatres.theatreName(UPDATED_THEATRE_NAME).theatreCity(UPDATED_THEATRE_CITY);

        restTheatresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheatres.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheatres))
            )
            .andExpect(status().isOk());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
        Theatres testTheatres = theatresList.get(theatresList.size() - 1);
        assertThat(testTheatres.getTheatreName()).isEqualTo(UPDATED_THEATRE_NAME);
        assertThat(testTheatres.getTheatreCity()).isEqualTo(UPDATED_THEATRE_CITY);
    }

    @Test
    @Transactional
    void patchNonExistingTheatres() throws Exception {
        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();
        theatres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTheatresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, theatres.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTheatres() throws Exception {
        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();
        theatres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTheatres() throws Exception {
        int databaseSizeBeforeUpdate = theatresRepository.findAll().size();
        theatres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatresMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(theatres))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theatres in the database
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTheatres() throws Exception {
        // Initialize the database
        theatresRepository.saveAndFlush(theatres);

        int databaseSizeBeforeDelete = theatresRepository.findAll().size();

        // Delete the theatres
        restTheatresMockMvc
            .perform(delete(ENTITY_API_URL_ID, theatres.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Theatres> theatresList = theatresRepository.findAll();
        assertThat(theatresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
