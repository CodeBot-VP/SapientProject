package com.sapient.service.impl;

import com.sapient.domain.Theatres;
import com.sapient.repository.TheatresRepository;
import com.sapient.service.TheatresService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Theatres}.
 */
@Service
@Transactional
public class TheatresServiceImpl implements TheatresService {

    private final Logger log = LoggerFactory.getLogger(TheatresServiceImpl.class);

    private final TheatresRepository theatresRepository;

    public TheatresServiceImpl(TheatresRepository theatresRepository) {
        this.theatresRepository = theatresRepository;
    }

    @Override
    public Theatres save(Theatres theatres) {
        log.debug("Request to save Theatres : {}", theatres);
        return theatresRepository.save(theatres);
    }

    @Override
    public Theatres update(Theatres theatres) {
        log.debug("Request to update Theatres : {}", theatres);
        return theatresRepository.save(theatres);
    }

    @Override
    public Optional<Theatres> partialUpdate(Theatres theatres) {
        log.debug("Request to partially update Theatres : {}", theatres);

        return theatresRepository
            .findById(theatres.getId())
            .map(existingTheatres -> {
                if (theatres.getTheatreName() != null) {
                    existingTheatres.setTheatreName(theatres.getTheatreName());
                }
                if (theatres.getTheatreCity() != null) {
                    existingTheatres.setTheatreCity(theatres.getTheatreCity());
                }

                return existingTheatres;
            })
            .map(theatresRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Theatres> findAll() {
        log.debug("Request to get all Theatres");
        return theatresRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Theatres> findOne(Long id) {
        log.debug("Request to get Theatres : {}", id);
        return theatresRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Theatres : {}", id);
        theatresRepository.deleteById(id);
    }
}
