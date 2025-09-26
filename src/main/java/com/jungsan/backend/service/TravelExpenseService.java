package com.jungsan.backend.service;

import com.jungsan.backend.dto.TravelExpenseDto;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.entity.TravelExpense;
import com.jungsan.backend.entity.TravelSettlement;
import com.jungsan.backend.exception.ResourceNotFoundException;
import com.jungsan.backend.mapper.TravelExpenseMapper;
import com.jungsan.backend.repository.ParticipantRepository;
import com.jungsan.backend.repository.TravelExpenseRepository;
import com.jungsan.backend.repository.TravelSettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelExpenseService {

    private final TravelExpenseRepository travelExpenseRepository;
    private final TravelSettlementRepository travelSettlementRepository;
    private final ParticipantRepository participantRepository;
    private final TravelExpenseMapper travelExpenseMapper;

    public List<TravelExpenseDto.Response> getExpensesBySettlementId(UUID settlementId) {
        List<TravelExpense> expenses = travelExpenseRepository.findBySettlementIdWithParticipant(settlementId);
        return travelExpenseMapper.toResponseList(expenses);
    }

    public TravelExpenseDto.Response getExpenseById(UUID id) {
        TravelExpense expense = travelExpenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelExpense", "id", id));
        return travelExpenseMapper.toResponse(expense);
    }

    @Transactional
    public TravelExpenseDto.Response createExpense(UUID settlementId, TravelExpenseDto.Create dto) {
        TravelSettlement settlement = travelSettlementRepository.findById(settlementId)
                .orElseThrow(() -> new ResourceNotFoundException("TravelSettlement", "id", settlementId));
        
        Participant participant = participantRepository.findByIdAndIsActiveTrue(dto.getParticipantId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getParticipantId()));
        
        TravelExpense expense = TravelExpense.builder()
                .settlement(settlement)
                .participant(participant)
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .build();
        
        TravelExpense savedExpense = travelExpenseRepository.save(expense);
        return travelExpenseMapper.toResponse(savedExpense);
    }

    @Transactional
    public TravelExpenseDto.Response updateExpense(UUID settlementId, UUID expenseId, TravelExpenseDto.Update dto) {
        TravelExpense expense = travelExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("TravelExpense", "id", expenseId));
        
        if (!expense.getSettlement().getId().equals(settlementId)) {
            throw new ResourceNotFoundException("TravelExpense", "settlementId", settlementId);
        }
        
        Participant participant = participantRepository.findByIdAndIsActiveTrue(dto.getParticipantId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getParticipantId()));
        
        expense.setParticipant(participant);
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        
        TravelExpense savedExpense = travelExpenseRepository.save(expense);
        return travelExpenseMapper.toResponse(savedExpense);
    }

    @Transactional
    public void deleteExpense(UUID settlementId, UUID expenseId) {
        TravelExpense expense = travelExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("TravelExpense", "id", expenseId));
        
        if (!expense.getSettlement().getId().equals(settlementId)) {
            throw new ResourceNotFoundException("TravelExpense", "settlementId", settlementId);
        }
        
        travelExpenseRepository.delete(expense);
    }
}
