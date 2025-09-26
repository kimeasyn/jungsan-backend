package com.jungsan.backend;

import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.repository.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DatabaseIntegrationTest {

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    void saveAndFindParticipant() {
        // Given
        Participant participant = Participant.builder()
                .name("테스트 참가자")
                .avatar("https://example.com/avatar.jpg")
                .isActive(true)
                .build();

        // When
        Participant saved = participantRepository.save(participant);
        Participant found = participantRepository.findById(saved.getId()).orElse(null);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("테스트 참가자");
    }
}
