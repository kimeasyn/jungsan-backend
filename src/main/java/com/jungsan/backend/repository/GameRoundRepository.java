package com.jungsan.backend.repository;

import com.jungsan.backend.entity.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, UUID> {

    List<GameRound> findByGameIdOrderByRoundNumber(UUID gameId);

    @Query("SELECT gr FROM GameRound gr LEFT JOIN FETCH gr.payments WHERE gr.id = :id")
    Optional<GameRound> findByIdWithPayments(@Param("id") UUID id);

    @Query("SELECT gr FROM GameRound gr LEFT JOIN FETCH gr.payments p LEFT JOIN FETCH p.payer LEFT JOIN FETCH p.recipient WHERE gr.game.id = :gameId ORDER BY gr.roundNumber")
    List<GameRound> findByGameIdWithPayments(@Param("gameId") UUID gameId);

    Optional<GameRound> findByGameIdAndRoundNumber(UUID gameId, Integer roundNumber);

    @Query("SELECT MAX(gr.roundNumber) FROM GameRound gr WHERE gr.game.id = :gameId")
    Optional<Integer> findMaxRoundNumberByGameId(@Param("gameId") UUID gameId);

    boolean existsByGameIdAndRoundNumber(UUID gameId, Integer roundNumber);
}
