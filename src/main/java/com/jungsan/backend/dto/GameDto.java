package com.jungsan.backend.dto;

import com.jungsan.backend.entity.Game;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GameDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotBlank(message = "게임 제목은 필수입니다")
        @Size(max = 200, message = "게임 제목은 200자를 초과할 수 없습니다")
        private String title;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotBlank(message = "게임 제목은 필수입니다")
        @Size(max = 200, message = "게임 제목은 200자를 초과할 수 없습니다")
        private String title;

        private Game.GameStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String title;
        private Game.GameStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
        private LocalDateTime updatedAt;
        private List<ParticipantDto.Simple> participants;
        private List<GameRoundDto.Simple> rounds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private UUID id;
        private String title;
        private Game.GameStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddParticipant {
        @NotBlank(message = "참가자 ID는 필수입니다")
        private UUID participantId;
    }
}
