package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enumeration.Etat;
import com.mycompany.myapp.repository.ChefRepository;
import com.mycompany.myapp.repository.EmployeRepository;
import com.mycompany.myapp.repository.TacheRepository;
import com.mycompany.myapp.service.TacheService;
import com.mycompany.myapp.service.UserService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tache}.
 */
@Service
@Transactional
public class TacheServiceImpl implements TacheService {

    private final Logger log = LoggerFactory.getLogger(TacheServiceImpl.class);

    private final TacheRepository tacheRepository;
    private final UserService userService;
    private final ChefRepository chefRepository;
    private final EmployeRepository employeRepository;

    public TacheServiceImpl(
        TacheRepository tacheRepository,
        UserService userService,
        ChefRepository chefRepository,
        EmployeRepository employeRepository
    ) {
        this.tacheRepository = tacheRepository;
        this.userService = userService;
        this.chefRepository = chefRepository;
        this.employeRepository = employeRepository;
    }

    @Override
    public Tache save(Tache tache) {
        User user = userService.getUserWithAuthorities().get();
        Chef chef = chefRepository.findChefByCompte_Id(user.getId());

        tache.setEtat(Etat.NonCommence);
        tache.setDateDebut(null);
        tache.setDateFin(null);
        tache.setService(chef.getService());
        log.debug("Request to save Tache : {}", tache);

        return tacheRepository.save(tache);
    }

    @Override
    public Optional<Tache> partialUpdate(Tache tache) {
        log.debug("Request to partially update Tache : {}", tache);

        return tacheRepository
            .findById(tache.getId())
            .map(
                existingTache -> {
                    if (tache.getIntitule() != null) {
                        existingTache.setIntitule(tache.getIntitule());
                    }
                    if (tache.getDateLimite() != null) {
                        existingTache.setDateLimite(tache.getDateLimite());
                    }
                    if (tache.getDescription() != null) {
                        existingTache.setDescription(tache.getDescription());
                    }
                    if (tache.getEtat() != null) {
                        existingTache.setEtat(tache.getEtat());
                    }
                    if (tache.getDateDebut() != null) {
                        existingTache.setDateDebut(tache.getDateDebut());
                    }
                    if (tache.getDateFin() != null) {
                        existingTache.setDateFin(tache.getDateFin());
                    }

                    return existingTache;
                }
            )
            .map(tacheRepository::save);
    }

    @Override
    public Page<Tache> findByFiltre(Pageable pageable, String etat) {
        User user = userService.getUserWithAuthorities().get();
        Set<Authority> authorities = user.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (Authority authority : authorities) {
            roles.add(authority.getName());
        }
        if (roles.contains("ROLE_CADRE")) {
            Employe employe = employeRepository.findEmployeByCompte_Id(user.getId());
            return findForCadreWithFiltre(pageable, etat, employe);
        } else if (roles.contains("ROLE_CHEFSERVICE")) {
            Chef chef = chefRepository.findChefByCompte_Id(user.getId());
            return findByServiceAndEtat(pageable, etat, chef.getService());
        }
        return tacheRepository.findByEtat(pageable, Etat.valueOf(etat));
    }

    @Override
    public Tache[] findListByFiltre(String etat) {
        User user = userService.getUserWithAuthorities().get();
        Set<Authority> authorities = user.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (Authority authority : authorities) {
            roles.add(authority.getName());
        }
        if (roles.contains("ROLE_CADRE")) {
            Employe employe = employeRepository.findEmployeByCompte_Id(user.getId());
            return tacheRepository.findByEtatAndCadreAffecte(Etat.valueOf(etat), employe);
        } else if (roles.contains("ROLE_CHEFSERVICE")) {
            Chef chef = chefRepository.findChefByCompte_Id(user.getId());
            return tacheRepository.findByEtatAndService(Etat.valueOf(etat), chef.getService());
        }
        return null;
    }

    public Page<Tache> findForCadreWithFiltre(Pageable pageable, String etat, Employe employe) {
        return tacheRepository.findByEtatAndCadreAffecte(pageable, Etat.valueOf(etat), employe);
    }

    public Page<Tache> findByServiceAndEtat(Pageable pageable, String etat, IService service) {
        List<IService> services = new ArrayList<>();
        services.add(service);
        return tacheRepository.findByEtatAndServiceIn(pageable, Etat.valueOf(etat), services);
    }

    public Stats findStatsForEmploye(Employe employe) {
        Stats stats = new Stats(0, 0, 0, 0, 0, 0);
        List<Tache> taches = tacheRepository.findByCadreAffecte(employe);
        for (Tache tache : taches) {
            if (tache.getEtat() == Etat.NonCommence) {
                stats.setNonCommence(stats.getNonCommence() + 1);
            } else if (tache.getEtat() == Etat.Encours) {
                stats.setEncours(stats.getEncours() + 1);
            } else if (tache.getEtat() == Etat.Termine) {
                stats.setTermine(stats.getTermine() + 1);
            } else if (tache.getEtat() == Etat.Abondonne) {
                stats.setAbondonne(stats.getAbondonne() + 1);
            } else if (tache.getEtat() == Etat.Valide) {
                stats.setValide(stats.getValide() + 1);
            } else if (tache.getEtat() == Etat.Refuse) {
                stats.setRefuse(stats.getRefuse() + 1);
            }
        }
        return stats;
    }

    public Stats findStatsForService(IService service) {
        Stats stats = new Stats(0, 0, 0, 0, 0, 0);
        List<Tache> taches = tacheRepository.findByService(service);
        for (Tache tache : taches) {
            if (tache.getEtat() == Etat.NonCommence) {
                stats.setNonCommence(stats.getNonCommence() + 1);
            } else if (tache.getEtat() == Etat.Encours) {
                stats.setEncours(stats.getEncours() + 1);
            } else if (tache.getEtat() == Etat.Termine) {
                stats.setTermine(stats.getTermine() + 1);
            } else if (tache.getEtat() == Etat.Abondonne) {
                stats.setAbondonne(stats.getAbondonne() + 1);
            } else if (tache.getEtat() == Etat.Valide) {
                stats.setValide(stats.getValide() + 1);
            } else if (tache.getEtat() == Etat.Refuse) {
                stats.setRefuse(stats.getRefuse() + 1);
            }
        }
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tache> findAll(Pageable pageable) {
        return tacheRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tache> findOne(Long id) {
        log.debug("Request to get Tache : {}", id);
        return tacheRepository.findById(id);
    }

    @Override
    public Tache updateEtat(long id, String nouveauEtat) {
        Tache tache = tacheRepository.findById(id).get();
        Etat etat = Etat.valueOf(nouveauEtat);
        tache.setEtat(etat);
        if (etat == Etat.Encours) {
            tache.setDateDebut(Instant.now());
        } else if (etat == Etat.Termine) {
            tache.setDateFin(Instant.now());
        }
        return tacheRepository.save(tache);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tache : {}", id);
        tacheRepository.deleteById(id);
    }

    @Override
    public Stats findStatsForUser() {
        User user = userService.getUserWithAuthorities().get();
        Set<Authority> authorities = user.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (Authority authority : authorities) {
            roles.add(authority.getName());
        }
        if (roles.contains("ROLE_CADRE")) {
            Employe employe = employeRepository.findEmployeByCompte_Id(user.getId());
            return findStatsForEmploye(employe);
        } else if (roles.contains("ROLE_CHEFSERVICE")) {
            IService service = chefRepository.findChefByCompte_Id(user.getId()).getService();
            return findStatsForService(service);
        }
        return null;
    }

    @Override
    public Stats findStatsByEmploye(Long id) {
        Employe employe = employeRepository.findEmployeById(id);
        return findStatsForEmploye(employe);
    }
}
