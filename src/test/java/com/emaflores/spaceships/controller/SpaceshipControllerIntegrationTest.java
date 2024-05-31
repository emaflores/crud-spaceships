package com.emaflores.spaceships.controller;

import com.emaflores.spaceships.entity.Spaceship;
import com.emaflores.spaceships.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SpaceshipControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpaceshipRepository repository;

    private String basicAuthHeader;

    private static RabbitMQContainer rabbitMQContainer;

    @BeforeAll
    public static void setUpContainer() {

        rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management-alpine")
                .withExposedPorts(5672, 15672);
        rabbitMQContainer.start();

        System.setProperty("spring.rabbitmq.host", rabbitMQContainer.getHost());
        System.setProperty("spring.rabbitmq.port", rabbitMQContainer.getMappedPort(5672).toString());
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes());
    }

    @Test
    void testGetAllSpaceships() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        repository.save(spaceship);

        mockMvc.perform(get("/api/spaceships")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.spaceshipList[0].name").value("Enterprise"));
    }

    @Test
    void testGetSpaceshipById() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        spaceship = repository.save(spaceship);

        mockMvc.perform(get("/api/spaceships/" + spaceship.getId())
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Enterprise"));
    }

    @Test
    void testCreateSpaceship() throws Exception {
        String spaceshipJson = "{\"name\":\"Enterprise\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(post("/api/spaceships")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(spaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Enterprise"));
    }

    @Test
    void testCreateSpaceshipDuplicate() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        repository.save(spaceship);

        String spaceshipJson = "{\"name\":\"Enterprise\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(post("/api/spaceships")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(spaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void testUpdateSpaceship() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        spaceship = repository.save(spaceship);

        String updatedSpaceshipJson = "{\"name\":\"Enterprise Updated\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(put("/api/spaceships/" + spaceship.getId())
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSpaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Enterprise Updated"));
    }

    @Test
    void testUpdateSpaceshipInvalidId() throws Exception {
        String updatedSpaceshipJson = "{\"name\":\"Enterprise Updated\",\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(put("/api/spaceships/abc")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSpaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testDeleteSpaceship() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Enterprise");
        spaceship.setType("Explorer");
        spaceship.setSource("Earth");
        spaceship = repository.save(spaceship);

        mockMvc.perform(delete("/api/spaceships/" + spaceship.getId())
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testDeleteSpaceshipNotFound() throws Exception {
        mockMvc.perform(delete("/api/spaceships/1")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testHandleDataIntegrityViolationException() throws Exception {

        String invalidSpaceshipJson = "{\"type\":\"Explorer\",\"source\":\"Earth\"}";

        mockMvc.perform(post("/api/spaceships")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidSpaceshipJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testSwaggerUIAccess() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}