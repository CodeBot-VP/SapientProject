package com.sapient.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapient.IntegrationTest;
import com.sapient.domain.Movie;
import com.sapient.repository.MovieRepository;
import com.sapient.service.MovieService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MovieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MovieResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_GENRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/movies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovieRepository movieRepository;

    @Mock
    private MovieRepository movieRepositoryMock;

    @Mock
    private MovieService movieServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovieMockMvc;

    private Movie movie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie().title(DEFAULT_TITLE).genre(DEFAULT_GENRE);
        return movie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createUpdatedEntity(EntityManager em) {
        Movie movie = new Movie().title(UPDATED_TITLE).genre(UPDATED_GENRE);
        return movie;
    }

    @BeforeEach
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();
        // Create the Movie
        restMovieMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getGenre()).isEqualTo(DEFAULT_GENRE);
    }

    @Test
    @Transactional
    void createMovieWithExistingId() throws Exception {
        // Create the Movie with an existing ID
        movie.setId(1L);

        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(movieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(movieRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc
            .perform(get(ENTITY_API_URL_ID, movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE));
    }

    @Test
    @Transactional
    void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findById(movie.getId()).get();
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie.title(UPDATED_TITLE).genre(UPDATED_GENRE);

        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMovie.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMovie))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getGenre()).isEqualTo(UPDATED_GENRE);
    }

    @Test
    @Transactional
    void putNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movie.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovieWithPatch() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie using partial update
        Movie partialUpdatedMovie = new Movie();
        partialUpdatedMovie.setId(movie.getId());

        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovie))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getGenre()).isEqualTo(DEFAULT_GENRE);
    }

    @Test
    @Transactional
    void fullUpdateMovieWithPatch() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie using partial update
        Movie partialUpdatedMovie = new Movie();
        partialUpdatedMovie.setId(movie.getId());

        partialUpdatedMovie.title(UPDATED_TITLE).genre(UPDATED_GENRE);

        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovie))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getGenre()).isEqualTo(UPDATED_GENRE);
    }

    @Test
    @Transactional
    void patchNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Delete the movie
        restMovieMockMvc
            .perform(delete(ENTITY_API_URL_ID, movie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
