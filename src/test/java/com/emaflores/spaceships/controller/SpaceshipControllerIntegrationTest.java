package com.emaflores.spaceships.controller;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class SpaceshipControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpaceshipRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void testGetAllSpaceships() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        repository.save(spaceship);

        mockMvc.perform(get("/api/spaceships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Enterprise"));
    }

    @Test
    public void testGetSpaceshipById() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        spaceship = repository.save(spaceship);

        mockMvc.perform(get("/api/spaceships/" + spaceship.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Enterprise"));
    }

    @Test
    public void testCreateSpaceship() throws Exception {
        String spaceshipJson = "{\"name\":\"Enterprise\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(post("/api/spaceships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(spaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Enterprise"));
    }

    @Test
    public void testCreateSpaceshipDuplicate() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        repository.save(spaceship);

        String spaceshipJson = "{\"name\":\"Enterprise\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(post("/api/spaceships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(spaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testUpdateSpaceship() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        spaceship = repository.save(spaceship);

        String updatedSpaceshipJson = "{\"name\":\"Enterprise Updated\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(put("/api/spaceships/" + spaceship.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSpaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Enterprise Updated"));
    }

    @Test
    public void testUpdateSpaceshipInvalidId() throws Exception {
        String updatedSpaceshipJson = "{\"name\":\"Enterprise Updated\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(put("/api/spaceships/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSpaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDeleteSpaceship() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        spaceship = repository.save(spaceship);

        mockMvc.perform(delete("/api/spaceships/" + spaceship.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteSpaceshipNotFound() throws Exception {
        mockMvc.perform(delete("/api/spaceships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testHandleDataIntegrityViolationException() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        repository.save(spaceship);

        String invalidSpaceshipJson = "{\"name\":null,\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(post("/api/spaceships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidSpaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
