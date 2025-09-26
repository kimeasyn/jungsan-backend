package com.jungsan.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class SettlementDto {

    public static class ParticipantBalance {
        private UUID participantId;
        private String participantName;
        private BigDecimal totalPaid = BigDecimal.ZERO;
        private BigDecimal totalReceived = BigDecimal.ZERO;
        private BigDecimal balance = BigDecimal.ZERO; // 받은 금액 - 지급한 금액
        
        public ParticipantBalance() {}
        
        public ParticipantBalance(UUID participantId, String participantName, BigDecimal totalPaid, BigDecimal totalReceived, BigDecimal balance) {
            this.participantId = participantId;
            this.participantName = participantName;
            this.totalPaid = totalPaid;
            this.totalReceived = totalReceived;
            this.balance = balance;
        }
        
        // Getters and Setters
        public UUID getParticipantId() { return participantId; }
        public void setParticipantId(UUID participantId) { this.participantId = participantId; }
        
        public String getParticipantName() { return participantName; }
        public void setParticipantName(String participantName) { this.participantName = participantName; }
        
        public BigDecimal getTotalPaid() { return totalPaid; }
        public void setTotalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; }
        
        public BigDecimal getTotalReceived() { return totalReceived; }
        public void setTotalReceived(BigDecimal totalReceived) { this.totalReceived = totalReceived; }
        
        public BigDecimal getBalance() { return balance; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
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
