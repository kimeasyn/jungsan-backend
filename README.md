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

## 🚀 설치 및 실행 방법

### 1. 사전 요구사항

#### macOS 설치
```bash
# Java 17 설치
brew install openjdk@17

# 환경 변수 설정 (.zshrc 또는 .bash_profile에 추가)
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# PostgreSQL 설치
brew install postgresql@15
brew services start postgresql@15

# Gradle은 프로젝트에 포함된 Gradle Wrapper 사용
```

#### 필수 요구사항
- **Java 17** 이상
- **PostgreSQL 12** 이상
- **Gradle 8.5** 이상 (프로젝트에 포함됨)

### 2. 프로젝트 클론 및 설정

```bash
# 프로젝트 클론
git clone https://github.com/kimeasyn/jungsan-backend.git
cd jungsan-backend

# Gradle Wrapper 실행 권한 부여
chmod +x gradlew
```

### 3. 데이터베이스 설정

```bash
# PostgreSQL 접속
psql postgres

# 데이터베이스 및 사용자 생성
CREATE DATABASE testdb;
CREATE USER admin WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE testdb TO admin;
\q

# 데이터베이스 스키마 생성 (수동 실행)
psql -h localhost -U admin -d testdb -f src/main/resources/db/migration/V1__Create_tables.sql
```

### 3-1. 데이터베이스 마이그레이션 정보

#### 마이그레이션 파일 위치
```
src/main/resources/db/migration/
└── V1__Create_tables.sql
```

#### V1__Create_tables.sql 주요 내용
- **참가자 테이블** (`participants`): 사용자 정보 관리
- **게임 테이블** (`games`): 게임 정보 및 상태 관리
- **게임 참가자 테이블** (`game_participants`): 게임-참가자 관계
- **게임 라운드 테이블** (`game_rounds`): 라운드별 승자 정보
- **지급 내역 테이블** (`payments`): 참가자 간 금액 이체 기록
- **여행 정산 테이블** (`travel_settlements`): 여행 정산 정보
- **여행 지출 테이블** (`travel_expenses`): 여행 지출 내역

#### 주요 특징
- **UUID 기본키**: 모든 테이블에 UUID 사용
- **자동 타임스탬프**: `created_at`, `updated_at` 자동 관리
- **외래키 제약조건**: CASCADE 삭제로 데이터 무결성 보장
- **체크 제약조건**: 금액 양수 검증, 상태값 검증
- **인덱스 최적화**: 자주 조회되는 컬럼에 인덱스 생성
- **트리거**: `updated_at` 자동 업데이트

#### ENUM 타입 처리
PostgreSQL ENUM 타입 대신 VARCHAR + CHECK 제약조건 사용:
```sql
-- 기존 (ENUM 사용)
CREATE TYPE game_status AS ENUM ('IN_PROGRESS', 'COMPLETED');
status game_status DEFAULT 'IN_PROGRESS'

-- 현재 (VARCHAR + CHECK 사용)
status VARCHAR(20) DEFAULT 'IN_PROGRESS' CHECK (status IN ('IN_PROGRESS', 'COMPLETED'))
```

#### 마이그레이션 실행 방법
```bash
# 수동 실행 (권장)
psql -h localhost -U admin -d testdb -f src/main/resources/db/migration/V1__Create_tables.sql

# 또는 Flyway 사용 (설정 필요)
./gradlew flywayMigrate
```

### 4. 환경 변수 설정

```bash
# 환경별 설정 파일 복사
cp env/.env.example env/.env.local

# 로컬 환경 설정 파일 편집
nano env/.env.local
```

**env/.env.local 예시:**
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

### 5. 애플리케이션 실행

```bash
# 의존성 설치 및 빌드
./gradlew build

# 로컬 환경에서 실행
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
./gradlew bootRun --args='--spring.profiles.active=local'
```

**다른 환경에서 실행:**
```bash
# 개발 환경
./gradlew bootRun --args='--spring.profiles.active=development'

# 프로덕션 환경
./gradlew bootRun --args='--spring.profiles.active=production'

# 테스트 환경
./gradlew bootRun --args='--spring.profiles.active=test'
```

### 6. 실행 확인

애플리케이션이 성공적으로 실행되면 다음 메시지가 표시됩니다:
```
Started JungsanBackendApplication in X.XXX seconds
```

### 7. API 문서 확인

- **Swagger UI**: http://localhost:8080/api/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

### 8. API 테스트

```bash
# 참가자 생성 테스트
curl -X POST http://localhost:8080/api/participants \
  -H "Content-Type: application/json" \
  -d '{"name": "홍길동", "avatar": "https://example.com/avatar.jpg"}'

# 참가자 목록 조회 테스트
curl -X GET http://localhost:8080/api/participants

# 게임 생성 테스트
curl -X POST http://localhost:8080/api/games \
  -H "Content-Type: application/json" \
  -d '{"title": "테스트 게임"}'
```

### 9. 문제 해결

#### Java 버전 문제
```bash
# Java 버전 확인
java -version

# JAVA_HOME 설정 확인
echo $JAVA_HOME

# 올바른 Java 버전으로 설정
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
```

#### PostgreSQL 연결 문제
```bash
# PostgreSQL 서비스 상태 확인
brew services list | grep postgresql

# PostgreSQL 서비스 시작
brew services start postgresql@15

# 데이터베이스 연결 테스트
psql -h localhost -U admin -d testdb
```

#### 포트 충돌 문제
```bash
# 8080 포트 사용 중인 프로세스 확인
lsof -i :8080

# 프로세스 종료
kill -9 <PID>
```

### 10. 개발 환경 설정

#### IDE 설정 (IntelliJ IDEA)
1. **File > Open** → 프로젝트 폴더 선택
2. **File > Project Structure** → Project SDK를 Java 17로 설정
3. **File > Settings** → Build Tools > Gradle → Gradle JVM을 Java 17로 설정

#### VS Code 설정
1. **Java Extension Pack** 설치
2. **Spring Boot Extension Pack** 설치
3. **Gradle for Java** 확장 설치

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
