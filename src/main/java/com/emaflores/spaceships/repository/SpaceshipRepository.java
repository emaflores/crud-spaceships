package com.emaflores.spaceships.repository;

import com.emaflores.spaceships.entity.Spaceship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
    Page<Spaceship> findAll(Pageable pageable);

    @Query("SELECT s FROM Spaceship s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Spaceship> findByNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByName(String name);
}

