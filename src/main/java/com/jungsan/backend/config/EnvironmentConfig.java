package com.jungsan.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EnvironmentConfig {
    
    // Spring Boot는 기본적으로 다음 순서로 환경 변수를 로드합니다:
    // 1. 시스템 환경 변수
    // 2. .env 파일 (프로젝트 루트)
    // 3. application-{profile}.yml
    // 4. application.yml
    
    // 환경별 설정 파일 로드 순서:
    // 1. env/.env.{profile} (예: env/.env.local, env/.env.production)
    // 2. env/.env
    // 3. .env.{profile}
    // 4. .env
}
