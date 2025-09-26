package com.jungsan.backend.controller;

import com.jungsan.backend.dto.ApiResponse;
import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.service.ParticipantService;
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
@RequestMapping("/participants")
@RequiredArgsConstructor
@Tag(name = "참가자 관리", description = "참가자 CRUD API")
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    @Operation(summary = "참가자 목록 조회", description = "활성화된 모든 참가자를 조회합니다")
    public ResponseEntity<ApiResponse<List<ParticipantDto.Response>>> getAllParticipants() {
        List<ParticipantDto.Response> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(ApiResponse.success(participants));
    }

    @GetMapping("/{id}")
    @Operation(summary = "참가자 상세 조회", description = "ID로 특정 참가자를 조회합니다")
    public ResponseEntity<ApiResponse<ParticipantDto.Response>> getParticipantById(
            @Parameter(description = "참가자 ID") @PathVariable UUID id) {
        ParticipantDto.Response participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(ApiResponse.success(participant));
    }

    @PostMapping
    @Operation(summary = "참가자 생성", description = "새로운 참가자를 생성합니다")
    public ResponseEntity<ApiResponse<ParticipantDto.Response>> createParticipant(
            @Valid @RequestBody ParticipantDto.Create dto) {
        ParticipantDto.Response participant = participantService.createParticipant(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(participant, "참가자가 성공적으로 생성되었습니다"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "참가자 수정", description = "기존 참가자 정보를 수정합니다")
    public ResponseEntity<ApiResponse<ParticipantDto.Response>> updateParticipant(
            @Parameter(description = "참가자 ID") @PathVariable UUID id,
            @Valid @RequestBody ParticipantDto.Update dto) {
        ParticipantDto.Response participant = participantService.updateParticipant(id, dto);
        return ResponseEntity.ok(ApiResponse.success(participant, "참가자 정보가 성공적으로 수정되었습니다"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "참가자 삭제", description = "참가자를 비활성화합니다")
    public ResponseEntity<ApiResponse<Void>> deleteParticipant(
            @Parameter(description = "참가자 ID") @PathVariable UUID id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.ok(ApiResponse.success(null, "참가자가 성공적으로 삭제되었습니다"));
    }

    @GetMapping("/search")
    @Operation(summary = "참가자 검색", description = "이름으로 참가자를 검색합니다")
    public ResponseEntity<ApiResponse<List<ParticipantDto.Simple>>> searchParticipants(
            @Parameter(description = "검색할 이름") @RequestParam String name) {
        List<ParticipantDto.Simple> participants = participantService.searchParticipants(name);
        return ResponseEntity.ok(ApiResponse.success(participants));
    }
}
