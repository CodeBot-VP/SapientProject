package com.sapient.web.rest;

import com.sapient.domain.Theatres;
import com.sapient.repository.TheatresRepository;
import com.sapient.service.TheatresService;
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
 * REST controller for managing {@link com.sapient.domain.Theatres}.
 */
@RestController
@RequestMapping("/api")
public class TheatresResource {

    private final Logger log = LoggerFactory.getLogger(TheatresResource.class);

    private static final String ENTITY_NAME = "theatres";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TheatresService theatresService;

    private final TheatresRepository theatresRepository;

    public TheatresResource(TheatresService theatresService, TheatresRepository theatresRepository) {
        this.theatresService = theatresService;
        this.theatresRepository = theatresRepository;
    }

    /**
     * {@code POST  /theatres} : Create a new theatres.
     *
     * @param theatres the theatres to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new theatres, or with status {@code 400 (Bad Request)} if the theatres has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/theatres")
    public ResponseEntity<Theatres> createTheatres(@RequestBody Theatres theatres) throws URISyntaxException {
        log.debug("REST request to save Theatres : {}", theatres);
        if (theatres.getId() != null) {
            throw new BadRequestAlertException("A new theatres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Theatres result = theatresService.save(theatres);
        return ResponseEntity
            .created(new URI("/api/theatres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theatres/:id} : Updates an existing theatres.
     *
     * @param id the id of the theatres to save.
     * @param theatres the theatres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theatres,
     * or with status {@code 400 (Bad Request)} if the theatres is not valid,
     * or with status {@code 500 (Internal Server Error)} if the theatres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/theatres/{id}")
    public ResponseEntity<Theatres> updateTheatres(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Theatres theatres
    ) throws URISyntaxException {
        log.debug("REST request to update Theatres : {}, {}", id, theatres);
        if (theatres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, theatres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!theatresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Theatres result = theatresService.update(theatres);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, theatres.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /theatres/:id} : Partial updates given fields of an existing theatres, field will ignore if it is null
     *
     * @param id the id of the theatres to save.
     * @param theatres the theatres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theatres,
     * or with status {@code 400 (Bad Request)} if the theatres is not valid,
     * or with status {@code 404 (Not Found)} if the theatres is not found,
     * or with status {@code 500 (Internal Server Error)} if the theatres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/theatres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Theatres> partialUpdateTheatres(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Theatres theatres
    ) throws URISyntaxException {
        log.debug("REST request to partial update Theatres partially : {}, {}", id, theatres);
        if (theatres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, theatres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!theatresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Theatres> result = theatresService.partialUpdate(theatres);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, theatres.getId().toString())
        );
    }

    /**
     * {@code GET  /theatres} : get all the theatres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of theatres in body.
     */
    @GetMapping("/theatres")
    public List<Theatres> getAllTheatres() {
        log.debug("REST request to get all Theatres");
        return theatresService.findAll();
    }

    /**
     * {@code GET  /theatres/:id} : get the "id" theatres.
     *
     * @param id the id of the theatres to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the theatres, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/theatres/{id}")
    public ResponseEntity<Theatres> getTheatres(@PathVariable Long id) {
        log.debug("REST request to get Theatres : {}", id);
        Optional<Theatres> theatres = theatresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(theatres);
    }

    /**
     * {@code DELETE  /theatres/:id} : delete the "id" theatres.
     *
     * @param id the id of the theatres to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/theatres/{id}")
    public ResponseEntity<Void> deleteTheatres(@PathVariable Long id) {
        log.debug("REST request to delete Theatres : {}", id);
        theatresService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
