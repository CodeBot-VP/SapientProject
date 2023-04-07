package com.sapient.service;

import com.sapient.domain.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Movie}.
 */
public interface MovieService {
    /**
     * Save a movie.
     *
     * @param movie the entity to save.
     * @return the persisted entity.
     */
    Movie save(Movie movie);

    /**
     * Updates a movie.
     *
     * @param movie the entity to update.
     * @return the persisted entity.
     */
    Movie update(Movie movie);

    /**
     * Partially updates a movie.
     *
     * @param movie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Movie> partialUpdate(Movie movie);

    /**
     * Get all the movies.
     *
     * @return the list of entities.
     */
    List<Movie> findAll();

    /**
     * Get all the movies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Movie> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" movie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Movie> findOne(Long id);

    /**
     * Delete the "id" movie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
