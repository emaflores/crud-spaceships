package com.emaflores.spaceships.controller;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.exception.DuplicateSpaceshipException;
import com.emaflores.spaceships.exception.ErrorResponse;
import com.emaflores.spaceships.exception.InvalidIdException;
import com.emaflores.spaceships.service.MessageProducerService;
import com.emaflores.spaceships.service.SpaceshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/spaceships")
@Validated
public class SpaceshipController {

    @Autowired
    private SpaceshipService service;

    @Autowired
    private PagedResourcesAssembler<Spaceship> pagedResourcesAssembler;

    @Autowired
    private MessageProducerService messageProducerService;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Spaceship>>> getAllSpaceships(Pageable pageable) {
        Page<Spaceship> spaceships = service.findAll(pageable);
        PagedModel<EntityModel<Spaceship>> pagedModel = pagedResourcesAssembler.toModel(spaceships);
        if (pageable.getPageNumber() >= spaceships.getTotalPages() && spaceships.getTotalPages() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PagedModel.empty());
        }
        return ResponseEntity.ok(pagedModel);
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
            Spaceship savedSpaceship = service.save(spaceship);
            messageProducerService.sendMessage("Created spaceship: " + savedSpaceship.getName());
            return new ResponseEntity<>(savedSpaceship, HttpStatus.CREATED);
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
                        Spaceship updatedSpaceship = service.save(spaceship);
                        messageProducerService.sendMessage("Updated spaceship: " + updatedSpaceship.getName());
                        return ResponseEntity.ok(updatedSpaceship);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (InvalidIdException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DuplicateSpaceshipException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpaceship(@PathVariable String id) {
        try {
            Long spaceshipId = validateAndConvertId(id);
            if (service.findById(spaceshipId).isPresent()) {
                service.deleteById(spaceshipId);
                messageProducerService.sendMessage("Deleted spaceship with ID: " + spaceshipId);
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