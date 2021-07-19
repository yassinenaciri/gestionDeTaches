package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoleRepository extends JpaRepository<Pole, Long> {}
