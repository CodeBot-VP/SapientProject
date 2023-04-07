package com.sapient.repository;

import com.sapient.domain.MovieShow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MovieShow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieShowRepository extends JpaRepository<MovieShow, Long> {}
