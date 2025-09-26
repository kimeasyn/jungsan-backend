package com.jungsan.backend.repository;

import com.jungsan.backend.entity.GameParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameParticipantRepository extends JpaRepository<GameParticipant, UUID> {

    List<GameParticipant> findByGameIdOrderByJoinedAt(UUID gameId);

    @Query("SELECT gp FROM GameParticipant gp JOIN FETCH gp.participant WHERE gp.game.id = :gameId")
    List<GameParticipant> findByGameIdWithParticipant(@Param("gameId") UUID gameId);

    Optional<GameParticipant> findByGameIdAndParticipantId(UUID gameId, UUID participantId);

    boolean existsByGameIdAndParticipantId(UUID gameId, UUID participantId);

    @Query("SELECT COUNT(gp) FROM GameParticipant gp WHERE gp.game.id = :gameId")
    long countByGameId(@Param("gameId") UUID gameId);

    void deleteByGameIdAndParticipantId(UUID gameId, UUID participantId);
}
