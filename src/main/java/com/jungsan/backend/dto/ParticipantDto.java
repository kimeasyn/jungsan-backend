package com.jungsan.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public class ParticipantDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotBlank(message = "이름은 필수입니다")
        @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다")
        private String name;

        @Size(max = 255, message = "아바타 URL은 255자를 초과할 수 없습니다")
        private String avatar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotBlank(message = "이름은 필수입니다")
        @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다")
        private String name;

        @Size(max = 255, message = "아바타 URL은 255자를 초과할 수 없습니다")
        private String avatar;

        private Boolean isActive;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String name;
        private String avatar;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private UUID id;
        private String name;
        private String avatar;
    }
}
