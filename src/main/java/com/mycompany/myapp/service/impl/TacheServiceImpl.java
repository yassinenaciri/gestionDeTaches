package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Chef;
import com.mycompany.myapp.domain.Tache;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.enumeration.Etat;
import com.mycompany.myapp.repository.ChefRepository;
import com.mycompany.myapp.repository.TacheRepository;
import com.mycompany.myapp.service.TacheService;
import com.mycompany.myapp.service.UserService;
import java.util.Optional;
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
    private final UserService userService;

    private final TacheRepository tacheRepository;
    private final ChefRepository chefRepository;

    public TacheServiceImpl(TacheRepository tacheRepository, UserService userService, ChefRepository chefRepository) {
        this.tacheRepository = tacheRepository;
        this.userService = userService;
        this.chefRepository = chefRepository;
    }

    @Override
    public Tache save(Tache tache) {
        User user = userService.getUserWithAuthorities().get();
        Chef chef = chefRepository.findChefByCompte_Id(user.getId());
        log.debug("Request to save Tache : {}", tache);
        tache.setEtat(Etat.NonCommence);
        tache.setDateDebut(null);
        tache.setDateFin(null);
        tache.setService(chef.getService());

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
    @Transactional(readOnly = true)
    public Page<Tache> findAll(Pageable pageable, String filter) {
        return tacheRepository.findByEtat(pageable, Etat.valueOf(filter));
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
        tache.setEtat(Etat.valueOf(nouveauEtat));
        return tacheRepository.save(tache);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tache : {}", id);
        tacheRepository.deleteById(id);
    }
}
