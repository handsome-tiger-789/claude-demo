# Claude Demo - Member Management API

Spring Boot 기반의 회원 관리 API 데모 프로젝트입니다.

## 기술 스택

- **Java**: 17
- **Spring Boot**: 4.0.1
- **Database**: H2 (In-memory)
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle

## 프로젝트 구조

```
src/main/java/org/example/claudedemo/
├── controller/          # REST API 컨트롤러
├── service/            # 비즈니스 로직
├── repository/         # 데이터 접근 계층
├── entity/             # JPA 엔티티
├── dto/                # 데이터 전송 객체
├── specification/      # JPA Specification
└── exception/          # 예외 처리
```

## API 명세

### 회원 조회

#### 1. 기본 조회
```http
GET /api/members/{id}
```

**응답 예시**
```json
{
  "id": 1,
  "name": "홍길동"
}
```

#### 2. Specification을 사용한 조회
```http
GET /api/members/spec/{id}
```

**응답 예시**
```json
{
  "id": 1,
  "name": "홍길동"
}
```

### 에러 응답
```json
{
  "code": "MEMBER_NOT_FOUND",
  "message": "회원을 찾을 수 없습니다.",
  "status": 404
}
```

## 데이터 모델

### Member
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 회원 ID (자동 생성) |
| name | String | 회원 이름 |

## 실행 방법

### 1. 프로젝트 빌드
```bash
./gradlew build
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. H2 Console 접속
```
http://localhost:8080/h2-console
```

## 주요 기능

- ✅ 회원 ID로 조회
- ✅ JPA Specification을 활용한 동적 쿼리
- ✅ 전역 예외 처리
- ✅ H2 인메모리 데이터베이스
- ✅ GitHub Actions CI/CD 워크플로우

## 개발 환경

### 필수 요구사항
- JDK 17 이상
- Gradle 7.x 이상

### IDE 설정
- IntelliJ IDEA 또는 Eclipse 사용 권장
- Lombok 플러그인 설치 필요
