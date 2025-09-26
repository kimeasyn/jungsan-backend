package com.jungsan.backend.service;

import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    private Participant participant;
    private ParticipantDto.Create createDto;
    private ParticipantDto.Update updateDto;

    @BeforeEach
    void setUp() {
        participant = Participant.builder()
                .id(UUID.randomUUID())
                .name("테스트 참가자")
                .avatar("https://example.com/avatar.jpg")
                .isActive(true)
                .build();

        createDto = ParticipantDto.Create.builder()
                .name("새 참가자")
                .avatar("https://example.com/new-avatar.jpg")
                .build();

        updateDto = ParticipantDto.Update.builder()
                .name("수정된 참가자")
                .avatar("https://example.com/updated-avatar.jpg")
                .isActive(true)
                .build();
    }

    @Test
    void getAllParticipants_ShouldReturnAllActiveParticipants() {
        // Given
        when(participantRepository.findByIsActiveTrueOrderByName()).thenReturn(List.of(participant));

        // When
        List<ParticipantDto.Response> result = participantService.getAllParticipants();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("테스트 참가자");
        verify(participantRepository).findByIsActiveTrueOrderByName();
    }

    @Test
    void getParticipantById_ShouldReturnParticipant() {
        // Given
        UUID id = participant.getId();
        when(participantRepository.findByIdAndIsActiveTrue(id)).thenReturn(Optional.of(participant));

        // When
        ParticipantDto.Response result = participantService.getParticipantById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("테스트 참가자");
        verify(participantRepository).findByIdAndIsActiveTrue(id);
    }

    @Test
    void createParticipant_ShouldCreateAndReturnParticipant() {
        // Given
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        // When
        ParticipantDto.Response result = participantService.createParticipant(createDto);

        // Then
        assertThat(result).isNotNull();
        verify(participantRepository).save(any(Participant.class));
    }

    @Test
    void updateParticipant_ShouldUpdateAndReturnParticipant() {
        // Given
        UUID id = participant.getId();
        when(participantRepository.findByIdAndIsActiveTrue(id)).thenReturn(Optional.of(participant));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        // When
        ParticipantDto.Response result = participantService.updateParticipant(id, updateDto);

        // Then
        assertThat(result).isNotNull();
        verify(participantRepository).findByIdAndIsActiveTrue(id);
        verify(participantRepository).save(any(Participant.class));
    }

    @Test
    void deleteParticipant_ShouldDeactivateParticipant() {
        // Given
        UUID id = participant.getId();
        when(participantRepository.findByIdAndIsActiveTrue(id)).thenReturn(Optional.of(participant));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        // When
        participantService.deleteParticipant(id);

        // Then
        verify(participantRepository).findByIdAndIsActiveTrue(id);
        verify(participantRepository).save(any(Participant.class));
    }
}
