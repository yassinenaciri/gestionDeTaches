package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employe;
import com.mycompany.myapp.domain.IService;
import com.mycompany.myapp.domain.Tache;
import com.mycompany.myapp.domain.enumeration.Etat;
import java.util.List;
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
    Page<Tache> findAll(Pageable pageable);
    Page<Tache> findByEtatAndServiceIn(Pageable pageable, Etat etat, List<IService> services);
    Tache[] findByEtatAndService(Etat etat, IService service);
    Page<Tache> findByEtatAndCadreAffecte(Pageable pageable, Etat etat, Employe cadre);
    Tache[] findByEtatAndCadreAffecte(Etat etat, Employe cadre);

    Page<Tache> findByCadreAffecte(Pageable pageable, Employe employe);

    Page<Tache> findByService(Pageable pageable, IService service);
    List<Tache> findByService(IService service);
    List<Tache> findByCadreAffecte(Employe employe);
}
