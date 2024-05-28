package com.emaflores.spaceships.service;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.exception.DuplicateSpaceshipException;
import com.emaflores.spaceships.repository.SpaceshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SpaceshipService {
    @Autowired
    private SpaceshipRepository repository;

    @Cacheable(value = "spaceships", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<Spaceship> findAll(Pageable pageable) {
        Page<Spaceship> spaceships = repository.findAll(pageable);
        if (spaceships.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        return spaceships;
    }

    @Cacheable(value = "spaceship", key = "#id")
    public Optional<Spaceship> findById(Long id) {
        return repository.findById(id);
    }

    @Cacheable(value = "spaceshipsByName", key = "#name")
    public List<Spaceship> findByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @CacheEvict(value = {"spaceships", "spaceship", "spaceshipsByName"}, allEntries = true)
    public Spaceship save(Spaceship spaceship) {
        Optional<Spaceship> existingSpaceship = repository.findByNameContainingIgnoreCase(spaceship.getName()).stream()
                .filter(s -> !s.getId().equals(spaceship.getId()))
                .findFirst();

        if (existingSpaceship.isPresent()) {
            throw new DuplicateSpaceshipException("A spaceship with the same name already exists.");
        }
        return repository.save(spaceship);
    }

    @CacheEvict(value = {"spaceships", "spaceship", "spaceshipsByName"}, allEntries = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}