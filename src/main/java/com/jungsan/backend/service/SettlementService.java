package com.jungsan.backend.service;

import com.jungsan.backend.dto.SettlementDto;
import com.jungsan.backend.entity.Game;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.entity.Payment;
import com.jungsan.backend.exception.ResourceNotFoundException;
import com.jungsan.backend.repository.GameRepository;
import com.jungsan.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final GameRepository gameRepository;
    private final PaymentRepository paymentRepository;

    public SettlementDto.GameSettlementResponse calculateGameSettlement(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        
        List<Payment> allPayments = paymentRepository.findByGameId(gameId);
        
        // 참가자별 잔액 계산
        Map<UUID, SettlementDto.ParticipantBalance> participantBalances = calculateParticipantBalances(allPayments);
        
        // 최적화된 정산 거래 생성
        List<SettlementDto.SettlementTransaction> transactions = optimizeSettlementTransactions(participantBalances);
        
        BigDecimal totalAmount = allPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return SettlementDto.GameSettlementResponse.builder()
                .gameId(gameId)
                .gameTitle(game.getTitle())
                .totalAmount(totalAmount)
                .participantBalances(new ArrayList<>(participantBalances.values()))
                .transactions(transactions)
                .build();
    }

    private Map<UUID, SettlementDto.ParticipantBalance> calculateParticipantBalances(List<Payment> payments) {
        Map<UUID, SettlementDto.ParticipantBalance> balances = new HashMap<>();
        
        for (Payment payment : payments) {
            UUID payerId = payment.getPayer().getId();
            UUID recipientId = payment.getRecipient().getId();
            BigDecimal amount = payment.getAmount();
            
            // 지급자 잔액 업데이트 (지급한 금액 증가)
            SettlementDto.ParticipantBalance payerBalance = balances.computeIfAbsent(payerId, id -> createParticipantBalance(payment.getPayer()));
            payerBalance.setTotalPaid(payerBalance.getTotalPaid().add(amount));
            
            // 수령자 잔액 업데이트 (받은 금액 증가)
            SettlementDto.ParticipantBalance recipientBalance = balances.computeIfAbsent(recipientId, id -> createParticipantBalance(payment.getRecipient()));
            recipientBalance.setTotalReceived(recipientBalance.getTotalReceived().add(amount));
        }
        
        // 최종 잔액 계산 (받은 금액 - 지급한 금액)
        Map<UUID, SettlementDto.ParticipantBalance> finalBalances = new HashMap<>();
        for (SettlementDto.ParticipantBalance balance : balances.values()) {
            BigDecimal finalBalance = balance.getTotalReceived().subtract(balance.getTotalPaid());
            
            SettlementDto.ParticipantBalance newBalance = new SettlementDto.ParticipantBalance(
                    balance.getParticipantId(),
                    balance.getParticipantName(),
                    balance.getTotalPaid(),
                    balance.getTotalReceived(),
                    finalBalance
            );
            finalBalances.put(balance.getParticipantId(), newBalance);
        }
        
        return finalBalances;
    }

    private SettlementDto.ParticipantBalance createParticipantBalance(Participant participant) {
        return new SettlementDto.ParticipantBalance(
                participant.getId(),
                participant.getName(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    private List<SettlementDto.SettlementTransaction> optimizeSettlementTransactions(
            Map<UUID, SettlementDto.ParticipantBalance> participantBalances) {
        
        List<SettlementDto.SettlementTransaction> transactions = new ArrayList<>();
        
        // 양수 잔액과 음수 잔액을 분리
        List<SettlementDto.ParticipantBalance> creditors = participantBalances.values().stream()
                .filter(balance -> balance.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .sorted((a, b) -> b.getBalance().compareTo(a.getBalance()))
                .collect(Collectors.toList());
        
        List<SettlementDto.ParticipantBalance> debtors = participantBalances.values().stream()
                .filter(balance -> balance.getBalance().compareTo(BigDecimal.ZERO) < 0)
                .sorted((a, b) -> a.getBalance().compareTo(b.getBalance()))
                .collect(Collectors.toList());
        
        // 최적화된 정산 거래 생성
        int creditorIndex = 0;
        int debtorIndex = 0;
        
        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {
            SettlementDto.ParticipantBalance creditor = creditors.get(creditorIndex);
            SettlementDto.ParticipantBalance debtor = debtors.get(debtorIndex);
            
            BigDecimal creditorBalance = creditor.getBalance();
            BigDecimal debtorDebt = debtor.getBalance().abs();
            
            BigDecimal transactionAmount = creditorBalance.min(debtorDebt);
            
            if (transactionAmount.compareTo(BigDecimal.ZERO) > 0) {
                transactions.add(SettlementDto.SettlementTransaction.builder()
                        .payerId(debtor.getParticipantId())
                        .payerName(debtor.getParticipantName())
                        .recipientId(creditor.getParticipantId())
                        .recipientName(creditor.getParticipantName())
                        .amount(transactionAmount)
                        .build());
                
                // 잔액 업데이트
                creditor.setBalance(creditorBalance.subtract(transactionAmount));
                debtor.setBalance(debtorDebt.subtract(transactionAmount));
            }
            
            // 잔액이 0에 가까우면 다음 참가자로 이동
            if (creditor.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                creditorIndex++;
            }
            if (debtor.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
                debtorIndex++;
            }
        }
        
        return transactions;
    }
}
