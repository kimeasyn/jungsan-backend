package com.jungsan.backend.repository;

import com.jungsan.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByRoundIdOrderByCreatedAt(UUID roundId);

    @Query("SELECT p FROM Payment p JOIN FETCH p.payer JOIN FETCH p.recipient WHERE p.round.id = :roundId")
    List<Payment> findByRoundIdWithParticipants(@Param("roundId") UUID roundId);

    @Query("SELECT p FROM Payment p WHERE p.payer.id = :participantId")
    List<Payment> findByPayerId(@Param("participantId") UUID participantId);

    @Query("SELECT p FROM Payment p WHERE p.recipient.id = :participantId")
    List<Payment> findByRecipientId(@Param("participantId") UUID participantId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.payer.id = :participantId")
    BigDecimal getTotalPaidByParticipant(@Param("participantId") UUID participantId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.recipient.id = :participantId")
    BigDecimal getTotalReceivedByParticipant(@Param("participantId") UUID participantId);

    @Query("SELECT p FROM Payment p WHERE p.round.game.id = :gameId")
    List<Payment> findByGameId(@Param("gameId") UUID gameId);
}
