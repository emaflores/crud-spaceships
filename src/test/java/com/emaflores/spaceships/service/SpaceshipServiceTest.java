package com.emaflores.spaceships.service;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.exception.DuplicateSpaceshipException;
import com.emaflores.spaceships.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpaceshipServiceTest {

    @InjectMocks
    private SpaceshipService service;

    @Mock
    private SpaceshipRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        List<Spaceship> spaceshipList = Arrays.asList(spaceship);
        Page<Spaceship> page = new PageImpl<>(spaceshipList);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<Spaceship> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Enterprise", result.getContent().get(0).getName());
    }

    @Test
    public void testFindById() {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("Enterprise");

        when(repository.findById(1L)).thenReturn(Optional.of(spaceship));

        Optional<Spaceship> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Enterprise", result.get().getName());
    }

    @Test
    public void testFindByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Spaceship> result = service.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByName() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        List<Spaceship> spaceshipList = Arrays.asList(spaceship);

        when(repository.findByNameContainingIgnoreCase("Enterprise")).thenReturn(spaceshipList);

        List<Spaceship> result = service.findByName("Enterprise");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Enterprise", result.get(0).getName());
    }

    @Test
    public void testSave() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");

        when(repository.existsByName("Enterprise")).thenReturn(false);
        when(repository.save(spaceship)).thenReturn(spaceship);

        Spaceship result = service.save(spaceship);

        assertNotNull(result);
        assertEquals("Enterprise", result.getName());
    }

    @Test
    public void testSaveDuplicate() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");

        when(repository.existsByName("Enterprise")).thenReturn(true);

        assertThrows(DuplicateSpaceshipException.class, () -> {
            service.save(spaceship);
        });
    }

    @Test
    public void testDeleteById() {
        doNothing().when(repository).deleteById(1L);

        service.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}