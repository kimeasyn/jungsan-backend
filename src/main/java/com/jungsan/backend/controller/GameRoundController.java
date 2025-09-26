package com.jungsan.backend.controller;

import com.jungsan.backend.dto.ApiResponse;
import com.jungsan.backend.dto.GameRoundDto;
import com.jungsan.backend.service.GameRoundService;
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
@RequestMapping("/games/{gameId}/rounds")
@RequiredArgsConstructor
@Tag(name = "게임 라운드 관리", description = "게임 라운드 CRUD API")
public class GameRoundController {

    private final GameRoundService gameRoundService;

    @GetMapping
    @Operation(summary = "게임 라운드 목록 조회", description = "특정 게임의 모든 라운드를 조회합니다")
    public ResponseEntity<ApiResponse<List<GameRoundDto.Response>>> getGameRounds(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId) {
        List<GameRoundDto.Response> rounds = gameRoundService.getGameRounds(gameId);
        return ResponseEntity.ok(ApiResponse.success(rounds));
    }

    @GetMapping("/{roundId}")
    @Operation(summary = "게임 라운드 상세 조회", description = "특정 라운드의 상세 정보를 조회합니다")
    public ResponseEntity<ApiResponse<GameRoundDto.Response>> getGameRoundById(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId,
            @Parameter(description = "라운드 ID") @PathVariable UUID roundId) {
        GameRoundDto.Response round = gameRoundService.getGameRoundById(gameId, roundId);
        return ResponseEntity.ok(ApiResponse.success(round));
    }

    @PostMapping
    @Operation(summary = "게임 라운드 생성", description = "새로운 라운드를 생성합니다")
    public ResponseEntity<ApiResponse<GameRoundDto.Response>> createGameRound(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId,
            @Valid @RequestBody GameRoundDto.Create dto) {
        GameRoundDto.Response round = gameRoundService.createGameRound(gameId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(round, "라운드가 성공적으로 생성되었습니다"));
    }

    @PutMapping("/{roundId}")
    @Operation(summary = "게임 라운드 수정", description = "기존 라운드 정보를 수정합니다")
    public ResponseEntity<ApiResponse<GameRoundDto.Response>> updateGameRound(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId,
            @Parameter(description = "라운드 ID") @PathVariable UUID roundId,
            @Valid @RequestBody GameRoundDto.Update dto) {
        GameRoundDto.Response round = gameRoundService.updateGameRound(gameId, roundId, dto);
        return ResponseEntity.ok(ApiResponse.success(round, "라운드 정보가 성공적으로 수정되었습니다"));
    }

    @DeleteMapping("/{roundId}")
    @Operation(summary = "게임 라운드 삭제", description = "라운드를 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteGameRound(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId,
            @Parameter(description = "라운드 ID") @PathVariable UUID roundId) {
        gameRoundService.deleteGameRound(gameId, roundId);
        return ResponseEntity.ok(ApiResponse.success(null, "라운드가 성공적으로 삭제되었습니다"));
    }

    @GetMapping("/next-number")
    @Operation(summary = "다음 라운드 번호 조회", description = "다음에 생성할 라운드 번호를 조회합니다")
    public ResponseEntity<ApiResponse<Integer>> getNextRoundNumber(
            @Parameter(description = "게임 ID") @PathVariable UUID gameId) {
        Integer nextRoundNumber = gameRoundService.getNextRoundNumber(gameId);
        return ResponseEntity.ok(ApiResponse.success(nextRoundNumber));
    }
}
