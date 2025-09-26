package com.jungsan.backend.service;

import com.jungsan.backend.dto.SettlementDto;
import com.jungsan.backend.entity.Game;
import com.jungsan.backend.entity.GameParticipant;
import com.jungsan.backend.entity.GameRound;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.entity.Payment;
import com.jungsan.backend.repository.GameRepository;
import com.jungsan.backend.repository.ParticipantRepository;
import com.jungsan.backend.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SettlementServiceTest {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private Game testGame;
    private Participant participant1;
    private Participant participant2;
    private Participant participant3;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        participantRepository.deleteAll();
        paymentRepository.deleteAll();

        // 참가자 생성
        participant1 = Participant.builder()
                .name("참가자1")
                .isActive(true)
                .build();
        participant1 = participantRepository.save(participant1);

        participant2 = Participant.builder()
                .name("참가자2")
                .isActive(true)
                .build();
        participant2 = participantRepository.save(participant2);

        participant3 = Participant.builder()
                .name("참가자3")
                .isActive(true)
                .build();
        participant3 = participantRepository.save(participant3);

        // 게임 생성
        testGame = Game.builder()
                .title("테스트 게임")
                .status(Game.GameStatus.IN_PROGRESS)
                .build();
        testGame = gameRepository.save(testGame);

        // 게임 참가자 추가
        GameParticipant gameParticipant1 = GameParticipant.builder()
                .game(testGame)
                .participant(participant1)
                .build();

        GameParticipant gameParticipant2 = GameParticipant.builder()
                .game(testGame)
                .participant(participant2)
                .build();

        GameParticipant gameParticipant3 = GameParticipant.builder()
                .game(testGame)
                .participant(participant3)
                .build();

        testGame.getParticipants().add(gameParticipant1);
        testGame.getParticipants().add(gameParticipant2);
        testGame.getParticipants().add(gameParticipant3);
        testGame = gameRepository.save(testGame);

        // 라운드 생성
        GameRound round = GameRound.builder()
                .game(testGame)
                .roundNumber(1)
                .winner(participant1)
                .build();
        testGame.getRounds().add(round);
        testGame = gameRepository.save(testGame);

        // 지급 내역 생성
        Payment payment1 = Payment.builder()
                .round(round)
                .payer(participant2)
                .recipient(participant1)
                .amount(new BigDecimal("10000"))
                .memo("1라운드 지급")
                .build();

        Payment payment2 = Payment.builder()
                .round(round)
                .payer(participant3)
                .recipient(participant1)
                .amount(new BigDecimal("5000"))
                .memo("1라운드 지급")
                .build();

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        
        // Payment 저장 완료
    }

    @Test
    void calculateGameSettlement_ShouldCalculateCorrectSettlement() {
        // When
        SettlementDto.GameSettlementResponse result = settlementService.calculateGameSettlement(testGame.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGameId()).isEqualTo(testGame.getId());
        assertThat(result.getGameTitle()).isEqualTo("테스트 게임");
        assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("15000"));
        assertThat(result.getParticipantBalances()).hasSize(3);
        assertThat(result.getTransactions()).isNotEmpty();

        // 참가자1 (승자) - 받은 금액: 15000, 지급한 금액: 0
        SettlementDto.ParticipantBalance winnerBalance = result.getParticipantBalances().stream()
                .filter(balance -> balance.getParticipantId().equals(participant1.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(winnerBalance.getTotalReceived()).isEqualByComparingTo(new BigDecimal("15000"));
        assertThat(winnerBalance.getTotalPaid()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(winnerBalance.getBalance()).isEqualByComparingTo(new BigDecimal("15000"));

        // 참가자2 - 받은 금액: 0, 지급한 금액: 10000
        SettlementDto.ParticipantBalance payer2Balance = result.getParticipantBalances().stream()
                .filter(balance -> balance.getParticipantId().equals(participant2.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(payer2Balance.getTotalReceived()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(payer2Balance.getTotalPaid()).isEqualByComparingTo(new BigDecimal("10000"));
        assertThat(payer2Balance.getBalance()).isEqualByComparingTo(new BigDecimal("-10000"));

        // 참가자3 - 받은 금액: 0, 지급한 금액: 5000
        SettlementDto.ParticipantBalance payer3Balance = result.getParticipantBalances().stream()
                .filter(balance -> balance.getParticipantId().equals(participant3.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(payer3Balance.getTotalReceived()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(payer3Balance.getTotalPaid()).isEqualByComparingTo(new BigDecimal("5000"));
        assertThat(payer3Balance.getBalance()).isEqualByComparingTo(new BigDecimal("-5000"));
    }

    @Test
    void calculateGameSettlement_WithNoPayments_ShouldReturnEmptySettlement() {
        // Given - 새로운 게임 생성 (지급 내역 없음)
        Game emptyGame = Game.builder()
                .title("빈 게임")
                .status(Game.GameStatus.IN_PROGRESS)
                .build();
        emptyGame = gameRepository.save(emptyGame);

        // When
        SettlementDto.GameSettlementResponse result = settlementService.calculateGameSettlement(emptyGame.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGameId()).isEqualTo(emptyGame.getId());
        assertThat(result.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getParticipantBalances()).isEmpty();
        assertThat(result.getTransactions()).isEmpty();
    }

    @Test
    void calculateGameSettlement_WithComplexPayments_ShouldOptimizeTransactions() {
        // Given - 복잡한 지급 내역 추가
        GameRound round2 = GameRound.builder()
                .game(testGame)
                .roundNumber(2)
                .winner(participant2)
                .build();
        testGame.getRounds().add(round2);
        gameRepository.save(testGame);

        // 참가자1이 참가자2에게 3000원 지급
        Payment payment3 = Payment.builder()
                .round(round2)
                .payer(participant1)
                .recipient(participant2)
                .amount(new BigDecimal("3000"))
                .memo("2라운드 지급")
                .build();
        paymentRepository.save(payment3);

        // When
        SettlementDto.GameSettlementResponse result = settlementService.calculateGameSettlement(testGame.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("18000"));
        
        // 최적화된 정산 거래가 생성되었는지 확인
        assertThat(result.getTransactions()).isNotEmpty();
        
        // 모든 거래의 총합이 0이 되는지 확인 (정산 완료)
        BigDecimal totalTransactionAmount = result.getTransactions().stream()
                .map(SettlementDto.SettlementTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(totalTransactionAmount).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
