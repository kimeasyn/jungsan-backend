package com.jungsan.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotNull(message = "지급자 ID는 필수입니다")
        private UUID payerId;

        @NotNull(message = "수령자 ID는 필수입니다")
        private UUID recipientId;

        @NotNull(message = "금액은 필수입니다")
        @DecimalMin(value = "0.01", message = "금액은 0.01 이상이어야 합니다")
        private BigDecimal amount;

        @Size(max = 1000, message = "메모는 1000자를 초과할 수 없습니다")
        private String memo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotNull(message = "지급자 ID는 필수입니다")
        private UUID payerId;

        @NotNull(message = "수령자 ID는 필수입니다")
        private UUID recipientId;

        @NotNull(message = "금액은 필수입니다")
        @DecimalMin(value = "0.01", message = "금액은 0.01 이상이어야 합니다")
        private BigDecimal amount;

        @Size(max = 1000, message = "메모는 1000자를 초과할 수 없습니다")
        private String memo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private ParticipantDto.Simple payer;
        private ParticipantDto.Simple recipient;
        private BigDecimal amount;
        private String memo;
        private LocalDateTime createdAt;
    }
}
