package com.jungsan.backend.dto;

import com.jungsan.backend.entity.TravelSettlement;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TravelSettlementDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotBlank(message = "정산 제목은 필수입니다")
        @Size(max = 200, message = "정산 제목은 200자를 초과할 수 없습니다")
        private String title;

        @NotNull(message = "총 금액은 필수입니다")
        @DecimalMin(value = "0.0", message = "총 금액은 0 이상이어야 합니다")
        private BigDecimal totalAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotBlank(message = "정산 제목은 필수입니다")
        @Size(max = 200, message = "정산 제목은 200자를 초과할 수 없습니다")
        private String title;

        @NotNull(message = "총 금액은 필수입니다")
        @DecimalMin(value = "0.0", message = "총 금액은 0 이상이어야 합니다")
        private BigDecimal totalAmount;

        private TravelSettlement.SettlementStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String title;
        private BigDecimal totalAmount;
        private TravelSettlement.SettlementStatus status;
        private BigDecimal totalExpenses;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
        private List<TravelExpenseDto.Response> expenses;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private UUID id;
        private String title;
        private BigDecimal totalAmount;
        private TravelSettlement.SettlementStatus status;
        private LocalDateTime createdAt;
    }
}
