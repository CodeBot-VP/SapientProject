package com.sapient.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapient.IntegrationTest;
import com.sapient.domain.MovieShow;
import com.sapient.repository.MovieShowRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MovieShowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MovieShowResourceIT {

    private static final String DEFAULT_MOVIE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOVIE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHOW_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHOW_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/movie-shows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovieShowRepository movieShowRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovieShowMockMvc;

    private MovieShow movieShow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovieShow createEntity(EntityManager em) {
        MovieShow movieShow = new MovieShow().movieName(DEFAULT_MOVIE_NAME).showTime(DEFAULT_SHOW_TIME);
        return movieShow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovieShow createUpdatedEntity(EntityManager em) {
        MovieShow movieShow = new MovieShow().movieName(UPDATED_MOVIE_NAME).showTime(UPDATED_SHOW_TIME);
        return movieShow;
    }

    @BeforeEach
    public void initTest() {
        movieShow = createEntity(em);
    }

    @Test
    @Transactional
    void createMovieShow() throws Exception {
        int databaseSizeBeforeCreate = movieShowRepository.findAll().size();
        // Create the MovieShow
        restMovieShowMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isCreated());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeCreate + 1);
        MovieShow testMovieShow = movieShowList.get(movieShowList.size() - 1);
        assertThat(testMovieShow.getMovieName()).isEqualTo(DEFAULT_MOVIE_NAME);
        assertThat(testMovieShow.getShowTime()).isEqualTo(DEFAULT_SHOW_TIME);
    }

    @Test
    @Transactional
    void createMovieShowWithExistingId() throws Exception {
        // Create the MovieShow with an existing ID
        movieShow.setId(1L);

        int databaseSizeBeforeCreate = movieShowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieShowMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMovieShows() throws Exception {
        // Initialize the database
        movieShowRepository.saveAndFlush(movieShow);

        // Get all the movieShowList
        restMovieShowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movieShow.getId().intValue())))
            .andExpect(jsonPath("$.[*].movieName").value(hasItem(DEFAULT_MOVIE_NAME)))
            .andExpect(jsonPath("$.[*].showTime").value(hasItem(DEFAULT_SHOW_TIME.toString())));
    }

    @Test
    @Transactional
    void getMovieShow() throws Exception {
        // Initialize the database
        movieShowRepository.saveAndFlush(movieShow);

        // Get the movieShow
        restMovieShowMockMvc
            .perform(get(ENTITY_API_URL_ID, movieShow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movieShow.getId().intValue()))
            .andExpect(jsonPath("$.movieName").value(DEFAULT_MOVIE_NAME))
            .andExpect(jsonPath("$.showTime").value(DEFAULT_SHOW_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMovieShow() throws Exception {
        // Get the movieShow
        restMovieShowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovieShow() throws Exception {
        // Initialize the database
        movieShowRepository.saveAndFlush(movieShow);

        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();

        // Update the movieShow
        MovieShow updatedMovieShow = movieShowRepository.findById(movieShow.getId()).get();
        // Disconnect from session so that the updates on updatedMovieShow are not directly saved in db
        em.detach(updatedMovieShow);
        updatedMovieShow.movieName(UPDATED_MOVIE_NAME).showTime(UPDATED_SHOW_TIME);

        restMovieShowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMovieShow.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMovieShow))
            )
            .andExpect(status().isOk());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
        MovieShow testMovieShow = movieShowList.get(movieShowList.size() - 1);
        assertThat(testMovieShow.getMovieName()).isEqualTo(UPDATED_MOVIE_NAME);
        assertThat(testMovieShow.getShowTime()).isEqualTo(UPDATED_SHOW_TIME);
    }

    @Test
    @Transactional
    void putNonExistingMovieShow() throws Exception {
        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();
        movieShow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieShowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movieShow.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovieShow() throws Exception {
        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();
        movieShow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieShowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovieShow() throws Exception {
        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();
        movieShow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieShowMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovieShowWithPatch() throws Exception {
        // Initialize the database
        movieShowRepository.saveAndFlush(movieShow);

        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();

        // Update the movieShow using partial update
        MovieShow partialUpdatedMovieShow = new MovieShow();
        partialUpdatedMovieShow.setId(movieShow.getId());

        restMovieShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovieShow.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovieShow))
            )
            .andExpect(status().isOk());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
        MovieShow testMovieShow = movieShowList.get(movieShowList.size() - 1);
        assertThat(testMovieShow.getMovieName()).isEqualTo(DEFAULT_MOVIE_NAME);
        assertThat(testMovieShow.getShowTime()).isEqualTo(DEFAULT_SHOW_TIME);
    }

    @Test
    @Transactional
    void fullUpdateMovieShowWithPatch() throws Exception {
        // Initialize the database
        movieShowRepository.saveAndFlush(movieShow);

        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();

        // Update the movieShow using partial update
        MovieShow partialUpdatedMovieShow = new MovieShow();
        partialUpdatedMovieShow.setId(movieShow.getId());

        partialUpdatedMovieShow.movieName(UPDATED_MOVIE_NAME).showTime(UPDATED_SHOW_TIME);

        restMovieShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovieShow.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovieShow))
            )
            .andExpect(status().isOk());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
        MovieShow testMovieShow = movieShowList.get(movieShowList.size() - 1);
        assertThat(testMovieShow.getMovieName()).isEqualTo(UPDATED_MOVIE_NAME);
        assertThat(testMovieShow.getShowTime()).isEqualTo(UPDATED_SHOW_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingMovieShow() throws Exception {
        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();
        movieShow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movieShow.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovieShow() throws Exception {
        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();
        movieShow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovieShow() throws Exception {
        int databaseSizeBeforeUpdate = movieShowRepository.findAll().size();
        movieShow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieShowMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movieShow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovieShow in the database
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovieShow() throws Exception {
        // Initialize the database
        movieShowRepository.saveAndFlush(movieShow);

        int databaseSizeBeforeDelete = movieShowRepository.findAll().size();

        // Delete the movieShow
        restMovieShowMockMvc
            .perform(delete(ENTITY_API_URL_ID, movieShow.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MovieShow> movieShowList = movieShowRepository.findAll();
        assertThat(movieShowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
