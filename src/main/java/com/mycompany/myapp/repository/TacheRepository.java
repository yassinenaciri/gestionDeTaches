package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tache;
import com.mycompany.myapp.domain.enumeration.Etat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {
    Page<Tache> findByEtat(Pageable pageable, Etat etat);
}
