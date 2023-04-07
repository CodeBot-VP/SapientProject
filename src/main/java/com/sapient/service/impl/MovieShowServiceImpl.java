package com.sapient.service.impl;

import com.sapient.domain.MovieShow;
import com.sapient.repository.MovieShowRepository;
import com.sapient.service.MovieShowService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MovieShow}.
 */
@Service
@Transactional
public class MovieShowServiceImpl implements MovieShowService {

    private final Logger log = LoggerFactory.getLogger(MovieShowServiceImpl.class);

    private final MovieShowRepository movieShowRepository;

    public MovieShowServiceImpl(MovieShowRepository movieShowRepository) {
        this.movieShowRepository = movieShowRepository;
    }

    @Override
    public MovieShow save(MovieShow movieShow) {
        log.debug("Request to save MovieShow : {}", movieShow);
        return movieShowRepository.save(movieShow);
    }

    @Override
    public MovieShow update(MovieShow movieShow) {
        log.debug("Request to update MovieShow : {}", movieShow);
        return movieShowRepository.save(movieShow);
    }

    @Override
    public Optional<MovieShow> partialUpdate(MovieShow movieShow) {
        log.debug("Request to partially update MovieShow : {}", movieShow);

        return movieShowRepository
            .findById(movieShow.getId())
            .map(existingMovieShow -> {
                if (movieShow.getMovieName() != null) {
                    existingMovieShow.setMovieName(movieShow.getMovieName());
                }
                if (movieShow.getShowTime() != null) {
                    existingMovieShow.setShowTime(movieShow.getShowTime());
                }

                return existingMovieShow;
            })
            .map(movieShowRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieShow> findAll() {
        log.debug("Request to get all MovieShows");
        return movieShowRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovieShow> findOne(Long id) {
        log.debug("Request to get MovieShow : {}", id);
        return movieShowRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MovieShow : {}", id);
        movieShowRepository.deleteById(id);
    }
}
