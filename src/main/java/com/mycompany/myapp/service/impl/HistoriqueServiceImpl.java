package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Historique;
import com.mycompany.myapp.repository.HistoriqueRepository;
import com.mycompany.myapp.service.HistoriqueService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Historique}.
 */
@Service
@Transactional
public class HistoriqueServiceImpl implements HistoriqueService {

    private final Logger log = LoggerFactory.getLogger(HistoriqueServiceImpl.class);

    private final HistoriqueRepository historiqueRepository;

    public HistoriqueServiceImpl(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    @Override
    public Historique save(Historique historique) {
        log.debug("Request to save Historique : {}", historique);
        return historiqueRepository.save(historique);
    }

    @Override
    public Optional<Historique> partialUpdate(Historique historique) {
        log.debug("Request to partially update Historique : {}", historique);

        return historiqueRepository
            .findById(historique.getId())
            .map(
                existingHistorique -> {
                    if (historique.getDateDebut() != null) {
                        existingHistorique.setDateDebut(historique.getDateDebut());
                    }
                    if (historique.getDateFin() != null) {
                        existingHistorique.setDateFin(historique.getDateFin());
                    }

                    return existingHistorique;
                }
            )
            .map(historiqueRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Historique> findAll(Pageable pageable) {
        log.debug("Request to get all Historiques");
        return historiqueRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Historique> findOne(Long id) {
        log.debug("Request to get Historique : {}", id);
        return historiqueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Historique : {}", id);
        historiqueRepository.deleteById(id);
    }
}
