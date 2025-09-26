package com.jungsan.backend.service;

import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.exception.ResourceNotFoundException;
import com.jungsan.backend.mapper.ParticipantMapper;
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
    private final ParticipantMapper participantMapper;

    public List<ParticipantDto.Response> getAllParticipants() {
        List<Participant> participants = participantRepository.findByIsActiveTrueOrderByName();
        return participantMapper.toResponseList(participants);
    }

    public ParticipantDto.Response getParticipantById(UUID id) {
        Participant participant = participantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", id));
        return participantMapper.toResponse(participant);
    }

    @Transactional
    public ParticipantDto.Response createParticipant(ParticipantDto.Create dto) {
        Participant participant = participantMapper.toEntity(dto);
        Participant savedParticipant = participantRepository.save(participant);
        return participantMapper.toResponse(savedParticipant);
    }

    @Transactional
    public ParticipantDto.Response updateParticipant(UUID id, ParticipantDto.Update dto) {
        Participant participant = participantRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", id));
        
        participantMapper.updateEntity(dto, participant);
        Participant savedParticipant = participantRepository.save(participant);
        return participantMapper.toResponse(savedParticipant);
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
        return participantMapper.toSimpleList(participants);
    }

    public boolean existsById(UUID id) {
        return participantRepository.existsByIdAndIsActiveTrue(id);
    }
}
