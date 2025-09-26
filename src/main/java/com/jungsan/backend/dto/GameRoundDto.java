package com.jungsan.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GameRoundDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotNull(message = "라운드 번호는 필수입니다")
        @Positive(message = "라운드 번호는 양수여야 합니다")
        private Integer roundNumber;

        @NotNull(message = "승자 ID는 필수입니다")
        private UUID winnerId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotNull(message = "승자 ID는 필수입니다")
        private UUID winnerId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private Integer roundNumber;
        private ParticipantDto.Simple winner;
        private BigDecimal totalAmount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<PaymentDto.Response> payments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private UUID id;
        private Integer roundNumber;
        private ParticipantDto.Simple winner;
        private BigDecimal totalAmount;
        private LocalDateTime createdAt;
    }
}
