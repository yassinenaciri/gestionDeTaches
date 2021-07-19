package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Direction;
import com.mycompany.myapp.repository.DirectionRepository;
import com.mycompany.myapp.service.DirectionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Direction}.
 */
@Service
@Transactional
public class DirectionServiceImpl implements DirectionService {

    private final Logger log = LoggerFactory.getLogger(DirectionServiceImpl.class);

    private final DirectionRepository directionRepository;

    public DirectionServiceImpl(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    @Override
    public Direction save(Direction direction) {
        log.debug("Request to save Direction : {}", direction);
        return directionRepository.save(direction);
    }

    @Override
    public Optional<Direction> partialUpdate(Direction direction) {
        log.debug("Request to partially update Direction : {}", direction);

        return directionRepository
            .findById(direction.getId())
            .map(
                existingDirection -> {
                    if (direction.getNomDirection() != null) {
                        existingDirection.setNomDirection(direction.getNomDirection());
                    }

                    return existingDirection;
                }
            )
            .map(directionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Direction> findAll(Pageable pageable) {
        log.debug("Request to get all Directions");
        return directionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Direction> findOne(Long id) {
        log.debug("Request to get Direction : {}", id);
        return directionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Direction : {}", id);
        directionRepository.deleteById(id);
    }
}
