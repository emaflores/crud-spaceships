package com.emaflores.spaceships.service;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.exception.DuplicateSpaceshipException;
import com.emaflores.spaceships.repository.SpaceshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceshipService {
    @Autowired
    private SpaceshipRepository repository;

    @Cacheable(value = "spaceships", key = "#pageable.pageNumber")
    public Page<Spaceship> findAll(Pageable pageable) {
        return repository.findAll(pageable);
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
        if (repository.existsByName(spaceship.getName())) {
            throw new DuplicateSpaceshipException("A spaceship with the same name already exists.");
        }
        return repository.save(spaceship);
    }

    @CacheEvict(value = {"spaceships", "spaceship", "spaceshipsByName"}, allEntries = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}