package com.jungsan.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "travel_settlements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private SettlementStatus status = SettlementStatus.IN_PROGRESS;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TravelExpense> expenses = new ArrayList<>();

    public enum SettlementStatus {
        IN_PROGRESS, COMPLETED
    }

    // 비즈니스 로직 메서드
    public BigDecimal getTotalExpenses() {
        return expenses.stream()
                .map(TravelExpense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
