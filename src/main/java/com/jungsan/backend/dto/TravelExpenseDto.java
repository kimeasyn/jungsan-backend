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

public class TravelExpenseDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotNull(message = "참가자 ID는 필수입니다")
        private UUID participantId;

        @NotNull(message = "금액은 필수입니다")
        @DecimalMin(value = "0.01", message = "금액은 0.01 이상이어야 합니다")
        private BigDecimal amount;

        @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다")
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotNull(message = "참가자 ID는 필수입니다")
        private UUID participantId;

        @NotNull(message = "금액은 필수입니다")
        @DecimalMin(value = "0.01", message = "금액은 0.01 이상이어야 합니다")
        private BigDecimal amount;

        @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다")
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private ParticipantDto.Simple participant;
        private BigDecimal amount;
        private String description;
        private LocalDateTime createdAt;
    }
}
