package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Chef;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Chef entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {
    Chef findChefByCompte_Id(Long id);
}
