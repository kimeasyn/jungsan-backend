package com.jungsan.backend.repository;

import com.jungsan.backend.entity.TravelExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface TravelExpenseRepository extends JpaRepository<TravelExpense, UUID> {

    List<TravelExpense> findBySettlementIdOrderByCreatedAt(UUID settlementId);

    @Query("SELECT te FROM TravelExpense te JOIN FETCH te.participant WHERE te.settlement.id = :settlementId")
    List<TravelExpense> findBySettlementIdWithParticipant(@Param("settlementId") UUID settlementId);

    @Query("SELECT te FROM TravelExpense te WHERE te.participant.id = :participantId")
    List<TravelExpense> findByParticipantId(@Param("participantId") UUID participantId);

    @Query("SELECT COALESCE(SUM(te.amount), 0) FROM TravelExpense te WHERE te.settlement.id = :settlementId")
    BigDecimal getTotalExpensesBySettlementId(@Param("settlementId") UUID settlementId);

    @Query("SELECT COALESCE(SUM(te.amount), 0) FROM TravelExpense te WHERE te.participant.id = :participantId AND te.settlement.id = :settlementId")
    BigDecimal getTotalExpensesByParticipantAndSettlement(@Param("participantId") UUID participantId, @Param("settlementId") UUID settlementId);
}
