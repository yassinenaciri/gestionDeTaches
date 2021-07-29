package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.ChefRepository;
import com.mycompany.myapp.repository.EmployeRepository;
import com.mycompany.myapp.repository.TacheRepository;
import com.mycompany.myapp.service.EmployeService;
import com.mycompany.myapp.service.UserService;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employe}.
 */
@Service
@Transactional
public class EmployeServiceImpl implements EmployeService {

    private final Logger log = LoggerFactory.getLogger(EmployeServiceImpl.class);

    private final EmployeRepository employeRepository;
    private final UserService userService;
    private final ChefRepository chefRepository;
    private final TacheRepository tacheRepository;

    public EmployeServiceImpl(
        EmployeRepository employeRepository,
        UserService userService,
        ChefRepository chefRepository,
        TacheRepository tacheRepository
    ) {
        this.employeRepository = employeRepository;
        this.tacheRepository = tacheRepository;
        this.userService = userService;
        this.chefRepository = chefRepository;
    }

    @Override
    public Employe save(Employe employe) {
        log.debug("Request to save Employe : {}", employe);
        return employeRepository.save(employe);
    }

    @Override
    public Optional<Employe> partialUpdate(Employe employe) {
        log.debug("Request to partially update Employe : {}", employe);

        return employeRepository
            .findById(employe.getId())
            .map(
                existingEmploye -> {
                    if (employe.getNomComplet() != null) {
                        existingEmploye.setNomComplet(employe.getNomComplet());
                    }

                    return existingEmploye;
                }
            )
            .map(employeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employe> findAll(Pageable pageable) {
        User user = userService.getUserWithAuthorities().get();
        Set<Authority> authorities = user.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (Authority authority : authorities) {
            roles.add(authority.getName());
        }
        if (roles.contains("ROLE_CHEFSERVICE")) {
            Chef chef = chefRepository.findChefByCompte_Id(user.getId());
            return employeRepository.findByService(pageable, chef.getService());
        } else if (roles.contains("ROLE_ADMIN")) {
            return employeRepository.findAll(pageable);
        } else if (roles.contains("ROLE_CHEFDIVISION")) {
            Chef chef = chefRepository.findChefByCompte_Id(user.getId());
            return findByDivision(pageable, chef.getDivision());
        } else if (roles.contains("ROLE_CHEFPOLE")) {
            Chef chef = chefRepository.findChefByCompte_Id(user.getId());
            return findByPole(pageable, chef.getPole());
        } else if (roles.contains("ROLE_DIRECTEUR")) {
            Chef chef = chefRepository.findChefByCompte_Id(user.getId());
            return findByDirection(pageable, chef.getDirection());
        }

        return null;
    }

    @Override
    public List<Employe> findClassementByService(IService service) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<Employe> findByServices(Pageable pageable, Set<IService> services) {
        return employeRepository.findByServiceIn(pageable, services);
    }

    @Transactional(readOnly = true)
    public Page<Employe> findByDivision(Pageable pageable, Division division) {
        return findByServices(pageable, division.getServices());
    }

    @Transactional(readOnly = true)
    public Page<Employe> findByPole(Pageable pageable, Pole pole) {
        Set<IService> services = new HashSet<>();
        for (Division division : pole.getDivisions()) {
            for (IService service : division.getServices()) {
                services.add(service);
            }
        }

        return findByServices(pageable, services);
    }

    @Transactional(readOnly = true)
    public Page<Employe> findByDirection(Pageable pageable, Direction direction) {
        Set<IService> services = new HashSet<>();
        for (Pole pole : direction.getPoles()) {
            for (Division division : pole.getDivisions()) {
                services.addAll(division.getServices());
            }
        }
        return findByServices(pageable, services);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employe> findOne(Long id) {
        log.debug("Request to get Employe : {}", id);
        return employeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employe : {}", id);
        employeRepository.deleteById(id);
    }
}
