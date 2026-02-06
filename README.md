# Claude Demo - Member & Post Management API

Spring Boot 기반의 회원 및 게시글 관리 API 데모 프로젝트입니다.

## 기술 스택

- **Java**: 17
- **Spring Boot**: 4.0.1
- **Database**: H2 (In-memory)
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle

## 프로젝트 구조

```
src/main/java/org/example/claudedemo/
├── ClaudeDemoApplication.java     # 애플리케이션 진입점
├── global/
│   └── exception/                 # 전역 예외 처리
│       ├── BusinessException.java
│       ├── ErrorCode.java
│       ├── ErrorResponse.java
│       └── GlobalExceptionHandler.java
├── member/
│   ├── Member.java                # JPA 엔티티
│   ├── MemberController.java      # REST 컨트롤러
│   ├── MemberRepository.java      # 데이터 접근 계층
│   ├── MemberService.java         # 비즈니스 로직
│   ├── MemberSpecification.java   # JPA Specification
│   └── dto/
│       └── MemberResponseDto.java
└── post/
    ├── Post.java                  # JPA 엔티티
    ├── PostController.java        # REST 컨트롤러
    ├── PostRepository.java        # 데이터 접근 계층
    ├── PostService.java           # 비즈니스 로직
    └── dto/
        ├── PostCreateRequestDto.java
        └── PostResponseDto.java
```

## API 명세

### 회원 (Member)

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

### 게시글 (Post)

#### 1. 전체 게시글 조회
```http
GET /api/posts
```

#### 2. 게시글 단건 조회
```http
GET /api/posts/{id}
```

**응답 예시**
```json
{
  "id": 1,
  "title": "제목",
  "content": "내용",
  "memberId": 1,
  "memberName": "홍길동",
  "viewCount": 0,
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

#### 3. 회원별 게시글 조회
```http
GET /api/posts/member/{memberId}
```

#### 4. 게시글 생성
```http
POST /api/posts
Content-Type: application/json

{
  "title": "제목",
  "content": "내용",
  "memberId": 1
}
```

#### 5. 게시글 삭제
```http
DELETE /api/posts/{id}
```

### 에러 응답

```json
{
  "code": "MEMBER-001",
  "message": "회원을 찾을 수 없습니다.",
  "status": 404
}
```

| 에러 코드 | 상태 | 메시지 |
|-----------|------|--------|
| MEMBER-001 | 404 | 회원을 찾을 수 없습니다. |
| POST-001 | 404 | 게시글을 찾을 수 없습니다. |
| COMMON-001 | 400 | 잘못된 입력값입니다. |
| COMMON-002 | 500 | 서버 내부 오류가 발생했습니다. |

## 데이터 모델

### Member
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 회원 ID (자동 생성) |
| name | String | 회원 이름 |
| createdAt | LocalDateTime | 생성일시 (자동) |
| updatedAt | LocalDateTime | 수정일시 (자동) |

### Post
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 게시글 ID (자동 생성) |
| title | String | 게시글 제목 |
| content | String (TEXT) | 게시글 내용 |
| member | Member (FK) | 작성자 |
| viewCount | Long | 조회수 (기본값: 0) |
| createdAt | LocalDateTime | 생성일시 (자동) |
| updatedAt | LocalDateTime | 수정일시 (자동) |

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

- 회원 ID로 조회
- JPA Specification을 활용한 동적 쿼리
- 게시글 CRUD (생성, 조회, 삭제)
- 회원별 게시글 조회
- JPA Auditing (생성일시/수정일시 자동 관리)
- 전역 예외 처리
- H2 인메모리 데이터베이스
- GitHub Actions CI/CD 워크플로우

## 개발 환경

### 필수 요구사항
- JDK 17 이상
- Gradle 7.x 이상

### IDE 설정
- IntelliJ IDEA 또는 Eclipse 사용 권장
- Lombok 플러그인 설치 필요
