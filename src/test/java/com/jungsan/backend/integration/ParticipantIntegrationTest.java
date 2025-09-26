package com.jungsan.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Participant;
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
class ParticipantIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Participant testParticipant;

    @BeforeEach
    void setUp() {
        participantRepository.deleteAll();
        
        testParticipant = Participant.builder()
                .name("테스트 참가자")
                .avatar("https://example.com/avatar.jpg")
                .isActive(true)
                .build();
        
        testParticipant = participantRepository.save(testParticipant);
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllParticipants_ShouldReturnAllActiveParticipants() throws Exception {
        mockMvc.perform(get("/participants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getParticipantById_ShouldReturnParticipant() throws Exception {
        mockMvc.perform(get("/participants/{id}", testParticipant.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("테스트 참가자"));
    }

    @Test
    void createParticipant_ShouldCreateAndReturnParticipant() throws Exception {
        ParticipantDto.Create createDto = ParticipantDto.Create.builder()
                .name("새 참가자")
                .avatar("https://example.com/new-avatar.jpg")
                .build();

        mockMvc.perform(post("/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("새 참가자"));

        assertThat(participantRepository.count()).isEqualTo(2);
    }
}
