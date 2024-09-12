package com.myapp.sabrine.repository;

import com.myapp.sabrine.domain.HR;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HR entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HRRepository extends JpaRepository<HR, Long> {}
