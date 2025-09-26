package com.jungsan.backend.repository;

import com.jungsan.backend.entity.TravelSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TravelSettlementRepository extends JpaRepository<TravelSettlement, UUID> {

    List<TravelSettlement> findByStatusOrderByCreatedAtDesc(TravelSettlement.SettlementStatus status);

    @Query("SELECT ts FROM TravelSettlement ts LEFT JOIN FETCH ts.expenses te LEFT JOIN FETCH te.participant WHERE ts.id = :id")
    Optional<TravelSettlement> findByIdWithExpenses(@Param("id") UUID id);

    @Query("SELECT ts FROM TravelSettlement ts WHERE ts.title ILIKE %:title% ORDER BY ts.createdAt DESC")
    List<TravelSettlement> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT COUNT(te) FROM TravelExpense te WHERE te.settlement.id = :settlementId")
    long countExpensesBySettlementId(@Param("settlementId") UUID settlementId);
}
