package com.jungsan.backend.repository;

import com.jungsan.backend.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {

    List<Participant> findByIsActiveTrueOrderByName();

    Optional<Participant> findByIdAndIsActiveTrue(UUID id);

    @Query("SELECT p FROM Participant p WHERE p.isActive = true AND p.name ILIKE %:name%")
    List<Participant> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);

    boolean existsByIdAndIsActiveTrue(UUID id);
}
