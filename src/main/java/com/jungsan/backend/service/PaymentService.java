package com.jungsan.backend.service;

import com.jungsan.backend.dto.PaymentDto;
import com.jungsan.backend.entity.GameRound;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.entity.Payment;
import com.jungsan.backend.exception.BusinessException;
import com.jungsan.backend.exception.ResourceNotFoundException;
import com.jungsan.backend.mapper.PaymentMapper;
import com.jungsan.backend.repository.GameRoundRepository;
import com.jungsan.backend.repository.ParticipantRepository;
import com.jungsan.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final GameRoundRepository gameRoundRepository;
    private final ParticipantRepository participantRepository;
    private final PaymentMapper paymentMapper;

    public List<PaymentDto.Response> getPaymentsByRoundId(UUID roundId) {
        List<Payment> payments = paymentRepository.findByRoundIdWithParticipants(roundId);
        return paymentMapper.toResponseList(payments);
    }

    public PaymentDto.Response getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        return paymentMapper.toResponse(payment);
    }

    @Transactional
    public PaymentDto.Response createPayment(UUID roundId, PaymentDto.Create dto) {
        GameRound round = gameRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("GameRound", "id", roundId));
        
        Participant payer = participantRepository.findByIdAndIsActiveTrue(dto.getPayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getPayerId()));
        
        Participant recipient = participantRepository.findByIdAndIsActiveTrue(dto.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getRecipientId()));
        
        if (payer.getId().equals(recipient.getId())) {
            throw new BusinessException("INVALID_PAYMENT", "지급자와 수령자가 같을 수 없습니다");
        }
        
        Payment payment = Payment.builder()
                .round(round)
                .payer(payer)
                .recipient(recipient)
                .amount(dto.getAmount())
                .memo(dto.getMemo())
                .build();
        
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(savedPayment);
    }

    @Transactional
    public PaymentDto.Response updatePayment(UUID id, PaymentDto.Update dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        
        Participant payer = participantRepository.findByIdAndIsActiveTrue(dto.getPayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getPayerId()));
        
        Participant recipient = participantRepository.findByIdAndIsActiveTrue(dto.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getRecipientId()));
        
        if (payer.getId().equals(recipient.getId())) {
            throw new BusinessException("INVALID_PAYMENT", "지급자와 수령자가 같을 수 없습니다");
        }
        
        payment.setPayer(payer);
        payment.setRecipient(recipient);
        payment.setAmount(dto.getAmount());
        payment.setMemo(dto.getMemo());
        
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(savedPayment);
    }

    @Transactional
    public void deletePayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        
        paymentRepository.delete(payment);
    }
}
