package com.jungsan.backend.service;

import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.exception.ResourceNotFoundException;
// import com.jungsan.backend.mapper.ParticipantMapper;
import com.jungsan.backend.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    // private final ParticipantMapper participantMapper;

    public List<ParticipantDto.Response> getAllParticipants() {
        List<Participant> participants = participantRepository.findByIsActiveTrueOrderByName();
        return participants.stream()
                .map(participant -> ParticipantDto.Response.builder()
                        .id(participant.getId())
                        .name(participant.getName())
                        .avatar(participant.getAvatar())
                        .isActive(participant.getIsActive())
                        .createdAt(participant.getCreatedAt())
                        .updatedAt(participant.getUpdatedAt())
                        .build())
                .toList();
    }

    public ParticipantDto.Response getParticipantById(UUID id) {
        Participant participant = participantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", id));
        
        // MapStruct 대신 수동 매핑
        return ParticipantDto.Response.builder()
                .id(participant.getId())
                .name(participant.getName())
                .avatar(participant.getAvatar())
                .isActive(participant.getIsActive())
                .createdAt(participant.getCreatedAt())
                .updatedAt(participant.getUpdatedAt())
                .build();
    }

    @Transactional
    public ParticipantDto.Response createParticipant(ParticipantDto.Create dto) {
        System.out.println("DEBUG: Received DTO: " + dto);
        System.out.println("DEBUG: DTO name: " + dto.getName());
        System.out.println("DEBUG: DTO avatar: " + dto.getAvatar());
        
        // MapStruct 대신 수동 매핑
        Participant participant = Participant.builder()
                .name(dto.getName())
                .avatar(dto.getAvatar())
                .isActive(true)
                .build();
        
        System.out.println("DEBUG: Mapped entity: " + participant);
        System.out.println("DEBUG: Entity name: " + participant.getName());
        System.out.println("DEBUG: Entity avatar: " + participant.getAvatar());
        
        Participant savedParticipant = participantRepository.save(participant);
        System.out.println("DEBUG: Saved entity: " + savedParticipant);
        System.out.println("DEBUG: Saved entity name: " + savedParticipant.getName());
        System.out.println("DEBUG: Saved entity avatar: " + savedParticipant.getAvatar());
        
        // MapStruct 대신 수동 매핑
        ParticipantDto.Response response = ParticipantDto.Response.builder()
                .id(savedParticipant.getId())
                .name(savedParticipant.getName())
                .avatar(savedParticipant.getAvatar())
                .isActive(savedParticipant.getIsActive())
                .createdAt(savedParticipant.getCreatedAt())
                .updatedAt(savedParticipant.getUpdatedAt())
                .build();
        
        return response;
    }

    @Transactional
    public ParticipantDto.Response updateParticipant(UUID id, ParticipantDto.Update dto) {
        Participant participant = participantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", id));
        
        // MapStruct 대신 수동 매핑
        if (dto.getName() != null) {
            participant.setName(dto.getName());
        }
        if (dto.getAvatar() != null) {
            participant.setAvatar(dto.getAvatar());
        }
        if (dto.getIsActive() != null) {
            participant.setIsActive(dto.getIsActive());
        }
        
        Participant savedParticipant = participantRepository.save(participant);
        
        return ParticipantDto.Response.builder()
                .id(savedParticipant.getId())
                .name(savedParticipant.getName())
                .avatar(savedParticipant.getAvatar())
                .isActive(savedParticipant.getIsActive())
                .createdAt(savedParticipant.getCreatedAt())
                .updatedAt(savedParticipant.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteParticipant(UUID id) {
        Participant participant = participantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", id));
        
        participant.setIsActive(false);
        participantRepository.save(participant);
    }

    public List<ParticipantDto.Simple> searchParticipants(String name) {
        List<Participant> participants = participantRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
        return participants.stream()
                .map(participant -> ParticipantDto.Simple.builder()
                        .id(participant.getId())
                        .name(participant.getName())
                        .avatar(participant.getAvatar())
                        .build())
                .toList();
    }

    public boolean existsById(UUID id) {
        return participantRepository.existsByIdAndIsActiveTrue(id);
    }
}
