package com.jungsan.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SimpleIntegrationTest {

    @Test
    void contextLoads() {
        // Spring 컨텍스트가 정상적으로 로드되는지 확인
        assertThat(true).isTrue();
    }
}
