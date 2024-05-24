package com.emaflores.spaceships.controller;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.exception.DuplicateSpaceshipException;
import com.emaflores.spaceships.exception.InvalidIdException;
import com.emaflores.spaceships.service.SpaceshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpaceshipControllerTest {

    @InjectMocks
    private SpaceshipController controller;

    @Mock
    private SpaceshipService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllSpaceships() {
        Pageable pageable = PageRequest.of(0, 10);
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        List<Spaceship> spaceshipList = Arrays.asList(spaceship);
        Page<Spaceship> page = new PageImpl<>(spaceshipList);

        when(service.findAll(pageable)).thenReturn(page);

        Page<Spaceship> result = controller.getAllSpaceships(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Enterprise", result.getContent().get(0).getName());
    }

    @Test
    public void testGetSpaceshipById() throws InvalidIdException {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("Enterprise");

        when(service.findById(1L)).thenReturn(Optional.of(spaceship));

        ResponseEntity<?> result = controller.getSpaceshipById("1");

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Enterprise", ((Spaceship) result.getBody()).getName());
    }

    @Test
    public void testGetSpaceshipByIdInvalid() {
        ResponseEntity<?> result = controller.getSpaceshipById("abc");

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetSpaceshipsByName() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        List<Spaceship> spaceshipList = Arrays.asList(spaceship);

        when(service.findByName("Enterprise")).thenReturn(spaceshipList);

        List<Spaceship> result = controller.getSpaceshipsByName("Enterprise");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Enterprise", result.get(0).getName());
    }

    @Test
    public void testCreateSpaceship() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");

        when(service.save(spaceship)).thenReturn(spaceship);

        ResponseEntity<?> result = controller.createSpaceship(spaceship);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Enterprise", ((Spaceship) result.getBody()).getName());
    }

    @Test
    public void testCreateSpaceshipDuplicate() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");

        when(service.save(spaceship)).thenThrow(new DuplicateSpaceshipException("A spaceship with the same name already exists."));

        ResponseEntity<?> result = controller.createSpaceship(spaceship);

        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    public void testUpdateSpaceship() throws InvalidIdException {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("Enterprise");

        when(service.findById(1L)).thenReturn(Optional.of(spaceship));
        when(service.save(spaceship)).thenReturn(spaceship);

        ResponseEntity<?> result = controller.updateSpaceship("1", spaceship);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Enterprise", ((Spaceship) result.getBody()).getName());
    }

    @Test
    public void testUpdateSpaceshipInvalidId() {
        ResponseEntity<?> result = controller.updateSpaceship("abc", new Spaceship());

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testDeleteSpaceship() throws InvalidIdException {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);

        when(service.findById(1L)).thenReturn(Optional.of(spaceship));
        doNothing().when(service).deleteById(1L);

        ResponseEntity<?> result = controller.deleteSpaceship("1");

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testDeleteSpaceshipInvalidId() {
        ResponseEntity<?> result = controller.deleteSpaceship("abc");

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testDeleteSpaceshipNotFound() throws InvalidIdException {
        when(service.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> result = controller.deleteSpaceship("1");

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
