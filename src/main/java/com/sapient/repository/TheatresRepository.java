package com.sapient.repository;

import com.sapient.domain.Theatres;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Theatres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TheatresRepository extends JpaRepository<Theatres, Long> {}
