package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employe;
import com.mycompany.myapp.domain.IService;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Employe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Employe findEmployeByCompte_Id(Long id);

    Page<Employe> findByServiceIn(Pageable pageable, Set<IService> services);

    Page<Employe> findByService(Pageable pageable, IService service);

    Employe findEmployeById(Long id);
}
