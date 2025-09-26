# 정산 앱 백엔드 API

여행 및 게임 정산을 위한 Spring Boot 3 기반의 REST API 서버입니다.

## 🚀 기술 스택

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Security**
- **MapStruct** (Entity-DTO 매핑)
- **SpringDoc OpenAPI 3** (Swagger 문서화)
- **Flyway** (데이터베이스 마이그레이션)
- **Gradle** (빌드 도구)

## 📋 주요 기능

### 참가자 관리
- 참가자 CRUD 작업
- 참가자 검색 및 필터링

### 게임 관리
- 게임 생성, 수정, 삭제
- 게임 참가자 관리 (추가/제거)
- 게임 상태 관리 (진행중/완료)

### 게임 라운드 관리
- 라운드 생성 및 관리
- 승자 지정
- 라운드별 지급 내역 관리

### 지급 내역 관리
- 지급 내역 CRUD 작업
- 참가자 간 금액 이체 기록

### 정산 계산
- 최적화된 정산 알고리즘
- 최소 거래 횟수로 정산 완료
- 참가자별 잔액 계산

### 여행 정산 관리
- 여행 정산 생성 및 관리
- 여행 지출 내역 관리
- 정산 상태 관리

## 🗄️ 데이터베이스 스키마

### 주요 테이블
- `participants` - 참가자 정보
- `games` - 게임 정보
- `game_participants` - 게임 참가자 관계
- `game_rounds` - 게임 라운드
- `payments` - 지급 내역
- `travel_settlements` - 여행 정산
- `travel_expenses` - 여행 지출

## 🚀 실행 방법

### 1. 사전 요구사항
- Java 17 이상
- PostgreSQL 12 이상
- Gradle 8.5 이상

### 2. 환경 변수 설정
```bash
# 환경별 설정 파일을 복사하고 설정을 수정하세요
cp env/.env.example env/.env.local

# 환경별 .env 파일 편집 (데이터베이스 정보 등)
nano env/.env.local
```

### 3. 데이터베이스 설정
```sql
CREATE DATABASE testdb;
CREATE USER admin WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE testdb TO admin;
```

### 4. 애플리케이션 실행
```bash
# 의존성 설치
./gradlew build

# 환경별 애플리케이션 실행
# 로컬 환경
./gradlew bootRun --args='--spring.profiles.active=local'

# 개발 환경
./gradlew bootRun --args='--spring.profiles.active=development'

# 프로덕션 환경
./gradlew bootRun --args='--spring.profiles.active=production'

# 테스트 환경
./gradlew bootRun --args='--spring.profiles.active=test'
```

### 4. API 문서 확인
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/api-docs

## 📡 API 엔드포인트

### 참가자 관리
- `GET /api/participants` - 참가자 목록 조회
- `POST /api/participants` - 참가자 생성
- `GET /api/participants/{id}` - 참가자 상세 조회
- `PUT /api/participants/{id}` - 참가자 수정
- `DELETE /api/participants/{id}` - 참가자 삭제

### 게임 관리
- `GET /api/games` - 게임 목록 조회
- `POST /api/games` - 게임 생성
- `GET /api/games/{id}` - 게임 상세 조회
- `PUT /api/games/{id}` - 게임 수정
- `DELETE /api/games/{id}` - 게임 삭제
- `POST /api/games/{id}/participants` - 게임에 참가자 추가
- `DELETE /api/games/{id}/participants/{participantId}` - 게임에서 참가자 제거

### 라운드 관리
- `GET /api/games/{gameId}/rounds` - 게임의 라운드 목록 조회
- `POST /api/games/{gameId}/rounds` - 라운드 생성
- `GET /api/games/{gameId}/rounds/{roundId}` - 라운드 상세 조회
- `PUT /api/games/{gameId}/rounds/{roundId}` - 라운드 수정
- `DELETE /api/games/{gameId}/rounds/{roundId}` - 라운드 삭제

### 지급 내역 관리
- `GET /api/rounds/{roundId}/payments` - 라운드의 지급 내역 조회
- `POST /api/rounds/{roundId}/payments` - 지급 내역 생성
- `PUT /api/payments/{id}` - 지급 내역 수정
- `DELETE /api/payments/{id}` - 지급 내역 삭제

### 정산 결과
- `GET /api/games/{id}/settlement` - 게임 정산 결과 조회
- `POST /api/games/{id}/settlement/calculate` - 정산 계산 실행

### 여행 정산 관리
- `GET /api/travel-settlements` - 여행 정산 목록 조회
- `POST /api/travel-settlements` - 여행 정산 생성
- `GET /api/travel-settlements/{id}` - 여행 정산 상세 조회
- `PUT /api/travel-settlements/{id}` - 여행 정산 수정
- `DELETE /api/travel-settlements/{id}` - 여행 정산 삭제

## 🔧 설정

### 환경별 설정 파일 구조
```
env/
├── .env.example          # 환경 변수 템플릿 (Git에 포함)
├── .env.local           # 로컬 환경 (Git에서 제외)
├── .env.development     # 개발 환경 (Git에서 제외)
├── .env.test           # 테스트 환경 (Git에서 제외)
└── .env.production     # 프로덕션 환경 (Git에서 제외)
```

### 환경 변수 설정 예시 (env/.env.local)
```bash
# 데이터베이스 설정
DB_HOST=localhost
DB_PORT=5432
DB_NAME=testdb
DB_USERNAME=admin
DB_PASSWORD=admin

# JWT 설정
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000

# 서버 설정
SERVER_PORT=8080
SERVER_CONTEXT_PATH=/api

# 로깅 설정
LOG_LEVEL_ROOT=INFO
LOG_LEVEL_APP=DEBUG
LOG_LEVEL_SQL=DEBUG
```

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:testdb}
    username: ${DB_USERNAME:admin}
    password: ${DB_PASSWORD:admin}
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  
  flyway:
    enabled: true
    locations: classpath:db/migration
```

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "ParticipantServiceTest"
```

## 📝 API 응답 형식

### 성공 응답
```json
{
  "success": true,
  "data": { ... },
  "message": "성공적으로 처리되었습니다",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### 에러 응답
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "유효성 검증 실패",
    "details": { ... }
  },
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## 🏗️ 프로젝트 구조

```
src/main/java/com/jungsan/backend/
├── config/          # 설정 클래스
├── controller/      # REST 컨트롤러
├── dto/            # 데이터 전송 객체
├── entity/         # JPA 엔티티
├── exception/      # 예외 처리
├── mapper/         # MapStruct 매퍼
├── repository/     # 데이터 접근 계층
└── service/        # 비즈니스 로직
```

## 🔒 보안

- CORS 설정 (모바일 앱 도메인 허용)
- 입력 데이터 검증
- SQL 인젝션 방지
- 전역 예외 처리

## 📊 정산 알고리즘

최적화된 정산 알고리즘을 사용하여 최소 거래 횟수로 정산을 완료합니다:

1. 각 참가자별 잔액 계산 (받은 금액 - 지급한 금액)
2. 양수 잔액(채권자)과 음수 잔액(채무자) 분리
3. 그리디 알고리즘을 사용한 최적 거래 조합 생성

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.
