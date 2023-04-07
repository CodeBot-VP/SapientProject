package com.sapient.service;

import com.sapient.domain.Theatres;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Theatres}.
 */
public interface TheatresService {
    /**
     * Save a theatres.
     *
     * @param theatres the entity to save.
     * @return the persisted entity.
     */
    Theatres save(Theatres theatres);

    /**
     * Updates a theatres.
     *
     * @param theatres the entity to update.
     * @return the persisted entity.
     */
    Theatres update(Theatres theatres);

    /**
     * Partially updates a theatres.
     *
     * @param theatres the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Theatres> partialUpdate(Theatres theatres);

    /**
     * Get all the theatres.
     *
     * @return the list of entities.
     */
    List<Theatres> findAll();

    /**
     * Get the "id" theatres.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Theatres> findOne(Long id);

    /**
     * Delete the "id" theatres.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
