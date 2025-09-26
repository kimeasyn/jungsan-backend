package com.jungsan.backend.service;

import com.jungsan.backend.dto.TravelSettlementDto;
import com.jungsan.backend.entity.TravelSettlement;
import com.jungsan.backend.exception.ResourceNotFoundException;
import com.jungsan.backend.mapper.TravelSettlementMapper;
import com.jungsan.backend.repository.TravelSettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelSettlementService {

    private final TravelSettlementRepository travelSettlementRepository;
    private final TravelSettlementMapper travelSettlementMapper;

    public List<TravelSettlementDto.Response> getAllTravelSettlements() {
        List<TravelSettlement> settlements = travelSettlementRepository.findAll();
        return travelSettlementMapper.toResponseList(settlements);
    }

    public List<TravelSettlementDto.Response> getTravelSettlementsByStatus(TravelSettlement.SettlementStatus status) {
        List<TravelSettlement> settlements = travelSettlementRepository.findByStatusOrderByCreatedAtDesc(status);
        return travelSettlementMapper.toResponseList(settlements);
    }

    public TravelSettlementDto.Response getTravelSettlementById(UUID id) {
        TravelSettlement settlement = travelSettlementRepository.findByIdWithExpenses(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelSettlement", "id", id));
        return travelSettlementMapper.toResponse(settlement);
    }

    @Transactional
    public TravelSettlementDto.Response createTravelSettlement(TravelSettlementDto.Create dto) {
        TravelSettlement settlement = travelSettlementMapper.toEntity(dto);
        TravelSettlement savedSettlement = travelSettlementRepository.save(settlement);
        return travelSettlementMapper.toResponse(savedSettlement);
    }

    @Transactional
    public TravelSettlementDto.Response updateTravelSettlement(UUID id, TravelSettlementDto.Update dto) {
        TravelSettlement settlement = travelSettlementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelSettlement", "id", id));
        
        travelSettlementMapper.updateEntity(dto, settlement);
        
        if (dto.getStatus() == TravelSettlement.SettlementStatus.COMPLETED && settlement.getCompletedAt() == null) {
            settlement.setCompletedAt(LocalDateTime.now());
        }
        
        TravelSettlement savedSettlement = travelSettlementRepository.save(settlement);
        return travelSettlementMapper.toResponse(savedSettlement);
    }

    @Transactional
    public void deleteTravelSettlement(UUID id) {
        TravelSettlement settlement = travelSettlementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelSettlement", "id", id));
        
        travelSettlementRepository.delete(settlement);
    }
}
