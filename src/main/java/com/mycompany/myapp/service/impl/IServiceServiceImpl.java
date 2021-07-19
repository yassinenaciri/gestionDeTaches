package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.IService;
import com.mycompany.myapp.repository.IServiceRepository;
import com.mycompany.myapp.service.IServiceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IService}.
 */
@Service
@Transactional
public class IServiceServiceImpl implements IServiceService {

    private final Logger log = LoggerFactory.getLogger(IServiceServiceImpl.class);

    private final IServiceRepository iServiceRepository;

    public IServiceServiceImpl(IServiceRepository iServiceRepository) {
        this.iServiceRepository = iServiceRepository;
    }

    @Override
    public IService save(IService iService) {
        log.debug("Request to save IService : {}", iService);
        return iServiceRepository.save(iService);
    }

    @Override
    public Optional<IService> partialUpdate(IService iService) {
        log.debug("Request to partially update IService : {}", iService);

        return iServiceRepository
            .findById(iService.getId())
            .map(
                existingIService -> {
                    if (iService.getNomService() != null) {
                        existingIService.setNomService(iService.getNomService());
                    }

                    return existingIService;
                }
            )
            .map(iServiceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IService> findAll(Pageable pageable) {
        log.debug("Request to get all IServices");
        return iServiceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IService> findOne(Long id) {
        log.debug("Request to get IService : {}", id);
        return iServiceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IService : {}", id);
        iServiceRepository.deleteById(id);
    }
}
