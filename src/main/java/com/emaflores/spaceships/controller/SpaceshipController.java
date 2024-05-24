package com.emaflores.spaceships.controller;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.exception.DuplicateSpaceshipException;
import com.emaflores.spaceships.exception.ErrorResponse;
import com.emaflores.spaceships.exception.InvalidIdException;
import com.emaflores.spaceships.service.SpaceshipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spaceships")
@Validated
public class SpaceshipController {
    @Autowired
    private SpaceshipService service;

    @GetMapping
    public Page<Spaceship> getAllSpaceships(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpaceshipById(@PathVariable String id) {
        try {
            Long spaceshipId = validateAndConvertId(id);
            return service.findById(spaceshipId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (InvalidIdException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public List<Spaceship> getSpaceshipsByName(@RequestParam String name) {
        return service.findByName(name);
    }

    @PostMapping
    public ResponseEntity<?> createSpaceship(@RequestBody @Valid Spaceship spaceship) {
        try {
            return new ResponseEntity<>(service.save(spaceship), HttpStatus.CREATED);
        } catch (DuplicateSpaceshipException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpaceship(@PathVariable String id, @RequestBody @Valid Spaceship spaceship) {
        try {
            Long spaceshipId = validateAndConvertId(id);
            return service.findById(spaceshipId)
                    .map(existing -> {
                        spaceship.setId(spaceshipId);
                        return ResponseEntity.ok(service.save(spaceship));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (InvalidIdException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpaceship(@PathVariable String id) {
        try {
            Long spaceshipId = validateAndConvertId(id);
            if (service.findById(spaceshipId).isPresent()) {
                service.deleteById(spaceshipId);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (InvalidIdException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private Long validateAndConvertId(String id) throws InvalidIdException {
        try {
            Long spaceshipId = Long.parseLong(id);
            if (spaceshipId <= 0) {
                throw new InvalidIdException("ID must be a positive number.");
            }
            return spaceshipId;
        } catch (NumberFormatException e) {
            throw new InvalidIdException("ID must be a valid number.");
        }
    }
}