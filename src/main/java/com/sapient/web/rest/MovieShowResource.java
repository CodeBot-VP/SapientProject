package com.sapient.web.rest;

import com.sapient.domain.MovieShow;
import com.sapient.repository.MovieShowRepository;
import com.sapient.service.MovieShowService;
import com.sapient.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sapient.domain.MovieShow}.
 */
@RestController
@RequestMapping("/api")
public class MovieShowResource {

    private final Logger log = LoggerFactory.getLogger(MovieShowResource.class);

    private static final String ENTITY_NAME = "movieShow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovieShowService movieShowService;

    private final MovieShowRepository movieShowRepository;

    public MovieShowResource(MovieShowService movieShowService, MovieShowRepository movieShowRepository) {
        this.movieShowService = movieShowService;
        this.movieShowRepository = movieShowRepository;
    }

    /**
     * {@code POST  /movie-shows} : Create a new movieShow.
     *
     * @param movieShow the movieShow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movieShow, or with status {@code 400 (Bad Request)} if the movieShow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movie-shows")
    public ResponseEntity<MovieShow> createMovieShow(@RequestBody MovieShow movieShow) throws URISyntaxException {
        log.debug("REST request to save MovieShow : {}", movieShow);
        if (movieShow.getId() != null) {
            throw new BadRequestAlertException("A new movieShow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovieShow result = movieShowService.save(movieShow);
        return ResponseEntity
            .created(new URI("/api/movie-shows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movie-shows/:id} : Updates an existing movieShow.
     *
     * @param id the id of the movieShow to save.
     * @param movieShow the movieShow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movieShow,
     * or with status {@code 400 (Bad Request)} if the movieShow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movieShow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movie-shows/{id}")
    public ResponseEntity<MovieShow> updateMovieShow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MovieShow movieShow
    ) throws URISyntaxException {
        log.debug("REST request to update MovieShow : {}, {}", id, movieShow);
        if (movieShow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movieShow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movieShowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MovieShow result = movieShowService.update(movieShow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, movieShow.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /movie-shows/:id} : Partial updates given fields of an existing movieShow, field will ignore if it is null
     *
     * @param id the id of the movieShow to save.
     * @param movieShow the movieShow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movieShow,
     * or with status {@code 400 (Bad Request)} if the movieShow is not valid,
     * or with status {@code 404 (Not Found)} if the movieShow is not found,
     * or with status {@code 500 (Internal Server Error)} if the movieShow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/movie-shows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MovieShow> partialUpdateMovieShow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MovieShow movieShow
    ) throws URISyntaxException {
        log.debug("REST request to partial update MovieShow partially : {}, {}", id, movieShow);
        if (movieShow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movieShow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movieShowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MovieShow> result = movieShowService.partialUpdate(movieShow);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, movieShow.getId().toString())
        );
    }

    /**
     * {@code GET  /movie-shows} : get all the movieShows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movieShows in body.
     */
    @GetMapping("/movie-shows")
    public List<MovieShow> getAllMovieShows() {
        log.debug("REST request to get all MovieShows");
        return movieShowService.findAll();
    }

    /**
     * {@code GET  /movie-shows/:id} : get the "id" movieShow.
     *
     * @param id the id of the movieShow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movieShow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movie-shows/{id}")
    public ResponseEntity<MovieShow> getMovieShow(@PathVariable Long id) {
        log.debug("REST request to get MovieShow : {}", id);
        Optional<MovieShow> movieShow = movieShowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movieShow);
    }

    /**
     * {@code DELETE  /movie-shows/:id} : delete the "id" movieShow.
     *
     * @param id the id of the movieShow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movie-shows/{id}")
    public ResponseEntity<Void> deleteMovieShow(@PathVariable Long id) {
        log.debug("REST request to delete MovieShow : {}", id);
        movieShowService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
