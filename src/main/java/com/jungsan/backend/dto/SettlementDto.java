package com.jungsan.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class SettlementDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantBalance {
        private UUID participantId;
        private String participantName;
        private BigDecimal totalPaid;
        private BigDecimal totalReceived;
        private BigDecimal balance; // 받은 금액 - 지급한 금액
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SettlementTransaction {
        private UUID payerId;
        private String payerName;
        private UUID recipientId;
        private String recipientName;
        private BigDecimal amount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GameSettlementResponse {
        private UUID gameId;
        private String gameTitle;
        private BigDecimal totalAmount;
        private List<ParticipantBalance> participantBalances;
        private List<SettlementTransaction> transactions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TravelSettlementResponse {
        private UUID settlementId;
        private String title;
        private BigDecimal totalAmount;
        private BigDecimal totalExpenses;
        private List<ParticipantBalance> participantBalances;
        private List<SettlementTransaction> transactions;
    }
}
