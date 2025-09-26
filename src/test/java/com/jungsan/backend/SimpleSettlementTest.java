package com.jungsan.backend;

import com.jungsan.backend.dto.SettlementDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleSettlementTest {

    @Test
    void testParticipantBalance() {
        // Given
        UUID participantId = UUID.randomUUID();
        String participantName = "테스트 참가자";
        BigDecimal totalPaid = new BigDecimal("10000");
        BigDecimal totalReceived = new BigDecimal("15000");
        BigDecimal expectedBalance = new BigDecimal("5000");

        // When
        SettlementDto.ParticipantBalance balance = new SettlementDto.ParticipantBalance(
                participantId,
                participantName,
                totalPaid,
                totalReceived,
                expectedBalance
        );

        // Then
        System.out.println("Created balance - Balance: " + balance.getBalance());
        assertThat(balance.getBalance()).isEqualByComparingTo(expectedBalance);
        
        // 추가 테스트: setBalance 호출
        balance.setBalance(new BigDecimal("10000"));
        System.out.println("After setBalance - Balance: " + balance.getBalance());
        assertThat(balance.getBalance()).isEqualByComparingTo(new BigDecimal("10000"));
    }
}
