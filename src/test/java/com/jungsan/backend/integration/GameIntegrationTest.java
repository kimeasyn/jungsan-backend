package com.jungsan.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungsan.backend.dto.GameDto;
import com.jungsan.backend.entity.Game;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.repository.GameRepository;
import com.jungsan.backend.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GameIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Game testGame;
    private Participant testParticipant;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        participantRepository.deleteAll();

        testParticipant = Participant.builder()
                .name("테스트 참가자")
                .avatar("https://example.com/avatar.jpg")
                .isActive(true)
                .build();
        testParticipant = participantRepository.save(testParticipant);

        testGame = Game.builder()
                .title("테스트 게임")
                .status(Game.GameStatus.IN_PROGRESS)
                .build();
        testGame = gameRepository.save(testGame);
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllGames_ShouldReturnAllGames() throws Exception {
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("테스트 게임"));
    }

    @Test
    void getGameById_ShouldReturnGame() throws Exception {
        mockMvc.perform(get("/games/{id}", testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("테스트 게임"));
    }

    @Test
    void createGame_ShouldCreateAndReturnGame() throws Exception {
        GameDto.Create createDto = GameDto.Create.builder()
                .title("새 게임")
                .build();

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("새 게임"));

        assertThat(gameRepository.count()).isEqualTo(2);
    }

    @Test
    void updateGame_ShouldUpdateAndReturnGame() throws Exception {
        GameDto.Update updateDto = GameDto.Update.builder()
                .title("수정된 게임")
                .status(Game.GameStatus.COMPLETED)
                .build();

        mockMvc.perform(put("/games/{id}", testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("수정된 게임"));

        Game updatedGame = gameRepository.findById(testGame.getId()).orElseThrow();
        assertThat(updatedGame.getTitle()).isEqualTo("수정된 게임");
        assertThat(updatedGame.getStatus()).isEqualTo(Game.GameStatus.COMPLETED);
    }

    @Test
    void addParticipant_ShouldAddParticipantToGame() throws Exception {
        GameDto.AddParticipant addParticipantDto = GameDto.AddParticipant.builder()
                .participantId(testParticipant.getId())
                .build();

        mockMvc.perform(post("/api/games/{id}/participants", testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addParticipantDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.participants").isArray())
                .andExpect(jsonPath("$.data.participants[0].name").value("테스트 참가자"));
    }

    @Test
    void getGameParticipants_ShouldReturnGameParticipants() throws Exception {
        // 먼저 참가자를 게임에 추가
        GameDto.AddParticipant addParticipantDto = GameDto.AddParticipant.builder()
                .participantId(testParticipant.getId())
                .build();

        mockMvc.perform(post("/api/games/{id}/participants", testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addParticipantDto)));

        // 참가자 목록 조회
        mockMvc.perform(get("/api/games/{id}/participants", testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("테스트 참가자"));
    }

    @Test
    void deleteGame_ShouldDeleteGame() throws Exception {
        mockMvc.perform(delete("/games/{id}", testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));

        assertThat(gameRepository.count()).isEqualTo(0);
    }

    @Test
    void createGame_WithInvalidData_ShouldReturn400() throws Exception {
        GameDto.Create invalidDto = GameDto.Create.builder()
                .title("") // 빈 제목으로 유효성 검증 실패
                .build();

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }
}
