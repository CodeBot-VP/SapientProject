package com.sapient.service.impl;

import com.sapient.domain.Movie;
import com.sapient.repository.MovieRepository;
import com.sapient.service.MovieService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Movie}.
 */
@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie save(Movie movie) {
        log.debug("Request to save Movie : {}", movie);
        return movieRepository.save(movie);
    }

    @Override
    public Movie update(Movie movie) {
        log.debug("Request to update Movie : {}", movie);
        return movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> partialUpdate(Movie movie) {
        log.debug("Request to partially update Movie : {}", movie);

        return movieRepository
            .findById(movie.getId())
            .map(existingMovie -> {
                if (movie.getTitle() != null) {
                    existingMovie.setTitle(movie.getTitle());
                }
                if (movie.getGenre() != null) {
                    existingMovie.setGenre(movie.getGenre());
                }

                return existingMovie;
            })
            .map(movieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        log.debug("Request to get all Movies");
        return movieRepository.findAll();
    }

    public Page<Movie> findAllWithEagerRelationships(Pageable pageable) {
        return movieRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Movie> findOne(Long id) {
        log.debug("Request to get Movie : {}", id);
        return movieRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Movie : {}", id);
        movieRepository.deleteById(id);
    }
}
