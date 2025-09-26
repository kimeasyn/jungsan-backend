package com.jungsan.backend.controller;

import com.jungsan.backend.dto.ApiResponse;
import com.jungsan.backend.dto.SettlementDto;
import com.jungsan.backend.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/games/{gameId}/settlement")
@RequiredArgsConstructor
@Tag(name = "정산 관리", description = "게임 정산 API")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping
    @Operation(summary = "게임 정산 결과 조회", description = "특정 게임의 정산 결과를 조회합니다")
    public ResponseEntity<ApiResponse<SettlementDto.GameSettlementResponse>> getGameSettlement(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId) {
        SettlementDto.GameSettlementResponse settlement = settlementService.calculateGameSettlement(gameId);
        return ResponseEntity.ok(ApiResponse.success(settlement));
    }

    @PostMapping("/calculate")
    @Operation(summary = "정산 계산 실행", description = "게임의 정산을 계산합니다")
    public ResponseEntity<ApiResponse<SettlementDto.GameSettlementResponse>> calculateSettlement(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId) {
        SettlementDto.GameSettlementResponse settlement = settlementService.calculateGameSettlement(gameId);
        return ResponseEntity.ok(ApiResponse.success(settlement, "정산이 성공적으로 계산되었습니다"));
    }
}
