package com.sapient.service;

import com.sapient.domain.MovieShow;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link MovieShow}.
 */
public interface MovieShowService {
    /**
     * Save a movieShow.
     *
     * @param movieShow the entity to save.
     * @return the persisted entity.
     */
    MovieShow save(MovieShow movieShow);

    /**
     * Updates a movieShow.
     *
     * @param movieShow the entity to update.
     * @return the persisted entity.
     */
    MovieShow update(MovieShow movieShow);

    /**
     * Partially updates a movieShow.
     *
     * @param movieShow the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MovieShow> partialUpdate(MovieShow movieShow);

    /**
     * Get all the movieShows.
     *
     * @return the list of entities.
     */
    List<MovieShow> findAll();

    /**
     * Get the "id" movieShow.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MovieShow> findOne(Long id);

    /**
     * Delete the "id" movieShow.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
