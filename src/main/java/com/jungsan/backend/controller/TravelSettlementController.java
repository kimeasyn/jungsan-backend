package com.jungsan.backend.controller;

import com.jungsan.backend.dto.ApiResponse;
import com.jungsan.backend.dto.TravelSettlementDto;
import com.jungsan.backend.entity.TravelSettlement;
import com.jungsan.backend.service.TravelSettlementService;
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
@RequestMapping("/travel-settlements")
@RequiredArgsConstructor
@Tag(name = "여행 정산 관리", description = "여행 정산 CRUD API")
public class TravelSettlementController {

    private final TravelSettlementService travelSettlementService;

    @GetMapping
    @Operation(summary = "여행 정산 목록 조회", description = "모든 여행 정산을 조회합니다")
    public ResponseEntity<ApiResponse<List<TravelSettlementDto.Response>>> getAllTravelSettlements() {
        List<TravelSettlementDto.Response> settlements = travelSettlementService.getAllTravelSettlements();
        return ResponseEntity.ok(ApiResponse.success(settlements));
    }

    @GetMapping(params = "status")
    @Operation(summary = "상태별 여행 정산 목록 조회", description = "상태별로 여행 정산을 조회합니다")
    public ResponseEntity<ApiResponse<List<TravelSettlementDto.Response>>> getTravelSettlementsByStatus(
            @Parameter(description = "정산 상태") @RequestParam TravelSettlement.SettlementStatus status) {
        List<TravelSettlementDto.Response> settlements = travelSettlementService.getTravelSettlementsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(settlements));
    }

    @GetMapping("/{id}")
    @Operation(summary = "여행 정산 상세 조회", description = "ID로 특정 여행 정산을 조회합니다")
    public ResponseEntity<ApiResponse<TravelSettlementDto.Response>> getTravelSettlementById(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID id) {
        TravelSettlementDto.Response settlement = travelSettlementService.getTravelSettlementById(id);
        return ResponseEntity.ok(ApiResponse.success(settlement));
    }

    @PostMapping
    @Operation(summary = "여행 정산 생성", description = "새로운 여행 정산을 생성합니다")
    public ResponseEntity<ApiResponse<TravelSettlementDto.Response>> createTravelSettlement(
            @Valid @RequestBody TravelSettlementDto.Create dto) {
        TravelSettlementDto.Response settlement = travelSettlementService.createTravelSettlement(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(settlement, "여행 정산이 성공적으로 생성되었습니다"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "여행 정산 수정", description = "기존 여행 정산 정보를 수정합니다")
    public ResponseEntity<ApiResponse<TravelSettlementDto.Response>> updateTravelSettlement(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID id,
            @Valid @RequestBody TravelSettlementDto.Update dto) {
        TravelSettlementDto.Response settlement = travelSettlementService.updateTravelSettlement(id, dto);
        return ResponseEntity.ok(ApiResponse.success(settlement, "여행 정산 정보가 성공적으로 수정되었습니다"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "여행 정산 삭제", description = "여행 정산을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteTravelSettlement(
            @Parameter(description = "여행 정산 ID") @PathVariable UUID id) {
        travelSettlementService.deleteTravelSettlement(id);
        return ResponseEntity.ok(ApiResponse.success(null, "여행 정산이 성공적으로 삭제되었습니다"));
    }
}
