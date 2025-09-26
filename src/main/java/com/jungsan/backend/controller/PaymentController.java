package com.jungsan.backend.controller;

import com.jungsan.backend.dto.ApiResponse;
import com.jungsan.backend.dto.PaymentDto;
import com.jungsan.backend.service.PaymentService;
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
@RequestMapping("/rounds/{roundId}/payments")
@RequiredArgsConstructor
@Tag(name = "지급 내역 관리", description = "지급 내역 CRUD API")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "라운드 지급 내역 조회", description = "특정 라운드의 모든 지급 내역을 조회합니다")
    public ResponseEntity<ApiResponse<List<PaymentDto.Response>>> getPaymentsByRoundId(
            @Parameter(description = "라운드 ID") @PathVariable UUID roundId) {
        List<PaymentDto.Response> payments = paymentService.getPaymentsByRoundId(roundId);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "지급 내역 상세 조회", description = "특정 지급 내역의 상세 정보를 조회합니다")
    public ResponseEntity<ApiResponse<PaymentDto.Response>> getPaymentById(
            @Parameter(description = "지급 내역 ID") @PathVariable UUID id) {
        PaymentDto.Response payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success(payment));
    }

    @PostMapping
    @Operation(summary = "지급 내역 생성", description = "새로운 지급 내역을 생성합니다")
    public ResponseEntity<ApiResponse<PaymentDto.Response>> createPayment(
            @Parameter(description = "라운드 ID") @PathVariable UUID roundId,
            @Valid @RequestBody PaymentDto.Create dto) {
        PaymentDto.Response payment = paymentService.createPayment(roundId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(payment, "지급 내역이 성공적으로 생성되었습니다"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "지급 내역 수정", description = "기존 지급 내역 정보를 수정합니다")
    public ResponseEntity<ApiResponse<PaymentDto.Response>> updatePayment(
            @Parameter(description = "지급 내역 ID") @PathVariable UUID id,
            @Valid @RequestBody PaymentDto.Update dto) {
        PaymentDto.Response payment = paymentService.updatePayment(id, dto);
        return ResponseEntity.ok(ApiResponse.success(payment, "지급 내역이 성공적으로 수정되었습니다"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "지급 내역 삭제", description = "지급 내역을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deletePayment(
            @Parameter(description = "지급 내역 ID") @PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "지급 내역이 성공적으로 삭제되었습니다"));
    }
}
