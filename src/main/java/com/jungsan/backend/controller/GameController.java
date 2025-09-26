package com.jungsan.backend.controller;

import com.jungsan.backend.dto.ApiResponse;
import com.jungsan.backend.dto.GameDto;
import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Game;
import com.jungsan.backend.service.GameService;
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
@RequestMapping("/games")
@RequiredArgsConstructor
@Tag(name = "게임 관리", description = "게임 CRUD API")
public class GameController {

    private final GameService gameService;

    @GetMapping
    @Operation(summary = "게임 목록 조회", description = "모든 게임을 조회합니다")
    public ResponseEntity<ApiResponse<List<GameDto.Response>>> getAllGames() {
        List<GameDto.Response> games = gameService.getAllGames();
        return ResponseEntity.ok(ApiResponse.success(games));
    }

    @GetMapping(params = "status")
    @Operation(summary = "상태별 게임 목록 조회", description = "상태별로 게임을 조회합니다")
    public ResponseEntity<ApiResponse<List<GameDto.Response>>> getGamesByStatus(
            @Parameter(description = "게임 상태") @RequestParam Game.GameStatus status) {
        List<GameDto.Response> games = gameService.getGamesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(games));
    }

    @GetMapping("/{id}")
    @Operation(summary = "게임 상세 조회", description = "ID로 특정 게임을 조회합니다")
    public ResponseEntity<ApiResponse<GameDto.Response>> getGameById(
            @Parameter(description = "게임 ID") @PathVariable UUID id) {
        GameDto.Response game = gameService.getGameById(id);
        return ResponseEntity.ok(ApiResponse.success(game));
    }

    @PostMapping
    @Operation(summary = "게임 생성", description = "새로운 게임을 생성합니다")
    public ResponseEntity<ApiResponse<GameDto.Response>> createGame(
            @Valid @RequestBody GameDto.Create dto) {
        GameDto.Response game = gameService.createGame(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(game, "게임이 성공적으로 생성되었습니다"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "게임 수정", description = "기존 게임 정보를 수정합니다")
    public ResponseEntity<ApiResponse<GameDto.Response>> updateGame(
            @Parameter(description = "게임 ID") @PathVariable UUID id,
            @Valid @RequestBody GameDto.Update dto) {
        GameDto.Response game = gameService.updateGame(id, dto);
        return ResponseEntity.ok(ApiResponse.success(game, "게임 정보가 성공적으로 수정되었습니다"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게임 삭제", description = "게임을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteGame(
            @Parameter(description = "게임 ID") @PathVariable UUID id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok(ApiResponse.success(null, "게임이 성공적으로 삭제되었습니다"));
    }

    @PostMapping("/{id}/participants")
    @Operation(summary = "게임 참가자 추가", description = "게임에 참가자를 추가합니다")
    public ResponseEntity<ApiResponse<GameDto.Response>> addParticipant(
            @Parameter(description = "게임 ID") @PathVariable UUID id,
            @Valid @RequestBody GameDto.AddParticipant dto) {
        GameDto.Response game = gameService.addParticipant(id, dto);
        return ResponseEntity.ok(ApiResponse.success(game, "참가자가 성공적으로 추가되었습니다"));
    }

    @DeleteMapping("/{id}/participants/{participantId}")
    @Operation(summary = "게임 참가자 제거", description = "게임에서 참가자를 제거합니다")
    public ResponseEntity<ApiResponse<GameDto.Response>> removeParticipant(
            @Parameter(description = "게임 ID") @PathVariable UUID id,
            @Parameter(description = "참가자 ID") @PathVariable UUID participantId) {
        GameDto.Response game = gameService.removeParticipant(id, participantId);
        return ResponseEntity.ok(ApiResponse.success(game, "참가자가 성공적으로 제거되었습니다"));
    }

    @GetMapping("/{id}/participants")
    @Operation(summary = "게임 참가자 목록 조회", description = "게임의 참가자 목록을 조회합니다")
    public ResponseEntity<ApiResponse<List<ParticipantDto.Simple>>> getGameParticipants(
            @Parameter(description = "게임 ID") @PathVariable UUID id) {
        List<ParticipantDto.Simple> participants = gameService.getGameParticipants(id);
        return ResponseEntity.ok(ApiResponse.success(participants));
    }
}
