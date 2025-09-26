package com.jungsan.backend.controller;

import com.jungsan.backend.dto.ApiResponse;
import com.jungsan.backend.dto.TravelExpenseDto;
import com.jungsan.backend.service.TravelExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/travel-settlements/{settlementId}/expenses")
@RequiredArgsConstructor
@Tag(name = "여행 지출 관리", description = "여행 지출 CRUD API")
public class TravelExpenseController {

    private final TravelExpenseService travelExpenseService;

    @GetMapping
    @Operation(summary = "여행 지출 목록 조회", description = "특정 여행 정산의 모든 지출을 조회합니다")
    public ResponseEntity<ApiResponse<List<TravelExpenseDto.Response>>> getExpensesBySettlementId(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID settlementId) {
        List<TravelExpenseDto.Response> expenses = travelExpenseService.getExpensesBySettlementId(settlementId);
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    @GetMapping("/{expenseId}")
    @Operation(summary = "여행 지출 상세 조회", description = "특정 지출의 상세 정보를 조회합니다")
    public ResponseEntity<ApiResponse<TravelExpenseDto.Response>> getExpenseById(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID settlementId,
            @Parameter(description = "지출 ID") @PathVariable UUID expenseId) {
        TravelExpenseDto.Response expense = travelExpenseService.getExpenseById(expenseId);
        return ResponseEntity.ok(ApiResponse.success(expense));
    }

    @PostMapping
    @Operation(summary = "여행 지출 생성", description = "새로운 여행 지출을 생성합니다")
    public ResponseEntity<ApiResponse<TravelExpenseDto.Response>> createExpense(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID settlementId,
            @Valid @RequestBody TravelExpenseDto.Create dto) {
        TravelExpenseDto.Response expense = travelExpenseService.createExpense(settlementId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(expense, "여행 지출이 성공적으로 생성되었습니다"));
    }

    @PutMapping("/{expenseId}")
    @Operation(summary = "여행 지출 수정", description = "기존 여행 지출 정보를 수정합니다")
    public ResponseEntity<ApiResponse<TravelExpenseDto.Response>> updateExpense(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID settlementId,
            @Parameter(description = "지출 ID") @PathVariable UUID expenseId,
            @Valid @RequestBody TravelExpenseDto.Update dto) {
        TravelExpenseDto.Response expense = travelExpenseService.updateExpense(settlementId, expenseId, dto);
        return ResponseEntity.ok(ApiResponse.success(expense, "여행 지출이 성공적으로 수정되었습니다"));
    }

    @DeleteMapping("/{expenseId}")
    @Operation(summary = "여행 지출 삭제", description = "여행 지출을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID settlementId,
            @Parameter(description = "지출 ID") @PathVariable UUID expenseId) {
        travelExpenseService.deleteExpense(settlementId, expenseId);
        return ResponseEntity.ok(ApiResponse.success(null, "여행 지출이 성공적으로 삭제되었습니다"));
    }
}
