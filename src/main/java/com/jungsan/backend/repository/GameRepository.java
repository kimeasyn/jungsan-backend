package com.jungsan.backend.repository;

import com.jungsan.backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    List<Game> findByStatusOrderByCreatedAtDesc(Game.GameStatus status);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.participants gp LEFT JOIN FETCH gp.participant WHERE g.id = :id")
    Optional<Game> findByIdWithParticipants(@Param("id") UUID id);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.rounds WHERE g.id = :id")
    Optional<Game> findByIdWithRounds(@Param("id") UUID id);

    @Query("SELECT g FROM Game g WHERE g.title ILIKE %:title% ORDER BY g.createdAt DESC")
    List<Game> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT COUNT(gp) FROM GameParticipant gp WHERE gp.game.id = :gameId")
    long countParticipantsByGameId(@Param("gameId") UUID gameId);
}
