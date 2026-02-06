# API 구현 방식 비교: `/api/members/{id}` vs `/api/members/spec/{id}`

## 개요

이 문서는 동일한 기능(회원 ID로 조회)을 수행하는 두 API의 구현 방식 차이를 상세히 설명합니다.

## 1. `/api/members/{id}` - Native Query 방식

### 구현 코드

**Controller** (`MemberController.java:19-23`)
```java
@GetMapping("/{id}")
public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
    MemberResponseDto member = memberService.findMemberById(id);
    return ResponseEntity.ok(member);
}
```

**Service** (`MemberService.java:21-24`)
```java
public MemberResponseDto findMemberById(Long id) {
    return memberRepository.findMemberById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
}
```

**Repository** (`MemberRepository.java:16-17`)
```java
@Query(value = "SELECT id, name FROM member WHERE id = :id", nativeQuery = true)
Optional<MemberResponseDto> findMemberById(@Param("id") Long id);
```

### 특징

#### 장점
- **직접적인 DTO 매핑**: 쿼리 결과를 바로 DTO로 반환하여 변환 과정 불필요
- **명시적인 SQL**: 실행되는 SQL을 코드에서 바로 확인 가능
- **성능 최적화**: 필요한 컬럼만 SELECT하여 조회
- **간단한 구조**: Entity 객체를 거치지 않아 메모리 효율적

#### 단점
- **유연성 부족**: 조회 조건이 변경되면 새로운 메서드 작성 필요
- **데이터베이스 종속성**: Native SQL 사용으로 DB 변경 시 쿼리 수정 필요
- **타입 안정성**: 컴파일 타임에 쿼리 오류 검증 불가

#### 실행 쿼리
```sql
SELECT id, name FROM member WHERE id = :id
```

---

## 2. `/api/members/spec/{id}` - JPA Specification 방식

### 구현 코드

**Controller** (`MemberController.java:25-29`)
```java
@GetMapping("/spec/{id}")
public ResponseEntity<MemberResponseDto> getMemberBySpec(@PathVariable Long id) {
    MemberResponseDto member = memberService.findMemberByIdUsingSpecification(id);
    return ResponseEntity.ok(member);
}
```

**Service** (`MemberService.java:26-31`)
```java
public MemberResponseDto findMemberByIdUsingSpecification(Long id) {
    Specification<Member> spec = Specification.where(MemberSpecification.equalId(id));
    Member member = memberRepository.findOne(spec)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    return new MemberResponseDto(member.getId(), member.getName());
}
```

**Specification** (`MemberSpecification.java:8-15`)
```java
public static Specification<Member> equalId(Long id) {
    return (root, query, criteriaBuilder) -> {
        if (id == null) {
            return criteriaBuilder.conjunction();
        }
        return criteriaBuilder.equal(root.get("id"), id);
    };
}
```

**Repository** (`MemberRepository.java:14`)
```java
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member>
```

### 특징

#### 장점
- **동적 쿼리 구성**: 런타임에 조건을 조합하여 유연한 쿼리 생성
- **재사용성**: Specification을 조합하여 복잡한 검색 조건 구현 가능
- **타입 안정성**: Criteria API로 컴파일 타임 검증
- **데이터베이스 독립성**: JPA가 DB 방언(Dialect)에 맞게 SQL 생성
- **조건 조합**: 여러 Specification을 `and()`, `or()`로 조합 가능

#### 단점
- **간접적인 처리**: Entity → DTO 변환 과정 필요
- **코드 복잡도**: Specification 작성이 Native Query보다 복잡
- **성능**: 모든 컬럼을 조회 후 DTO로 변환
- **학습 곡선**: Criteria API 이해 필요

#### 실행 쿼리 (예상)
```sql
SELECT member0_.id, member0_.name
FROM member member0_
WHERE member0_.id = ?
```

---

## 3. 주요 차이점 비교표

| 구분 | `/api/members/{id}` | `/api/members/spec/{id}` |
|------|---------------------|--------------------------|
| **쿼리 방식** | Native SQL | JPA Criteria API |
| **반환 타입** | DTO 직접 반환 | Entity → DTO 변환 |
| **유연성** | 낮음 (고정된 쿼리) | 높음 (동적 조건 조합) |
| **타입 안정성** | 낮음 (문자열 쿼리) | 높음 (컴파일 타임 검증) |
| **DB 독립성** | 낮음 (Native SQL) | 높음 (JPA 방언 사용) |
| **성능** | 우수 (필요 컬럼만) | 보통 (전체 조회) |
| **코드 복잡도** | 낮음 | 중간 |
| **재사용성** | 낮음 | 높음 |

---

## 4. 사용 시나리오별 권장사항

### Native Query 방식 (`/api/members/{id}`)을 선택하는 경우
- ✅ 단순하고 고정된 조회 조건
- ✅ 성능 최적화가 중요한 경우
- ✅ 특정 DB 기능을 활용해야 하는 경우
- ✅ 쿼리 튜닝이 필요한 경우

### Specification 방식 (`/api/members/spec/{id}`)을 선택하는 경우
- ✅ 동적으로 검색 조건을 조합해야 하는 경우
- ✅ 다양한 필터 조건을 재사용해야 하는 경우
- ✅ 데이터베이스 독립성이 중요한 경우
- ✅ 복잡한 검색 API (필터링, 정렬, 페이징)

---

## 5. Specification의 확장 가능성

현재 구현된 Specification은 ID 검색 외에도 이름 검색을 지원합니다:

**MemberSpecification.java:17-24**
```java
public static Specification<Member> likeName(String name) {
    return (root, query, criteriaBuilder) -> {
        if (name == null || name.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        return criteriaBuilder.like(root.get("name"), "%" + name + "%");
    };
}
```

### 조건 조합 예시
```java
// ID와 이름으로 동시 검색
Specification<Member> spec = Specification
    .where(MemberSpecification.equalId(id))
    .and(MemberSpecification.likeName(name));

Member member = memberRepository.findOne(spec).orElseThrow(...);
```

이러한 조합은 Native Query로는 각각의 케이스마다 새로운 메서드를 작성해야 하지만, Specification은 기존 조건을 재사용할 수 있습니다.

---

## 6. 결론

두 방식은 각각의 장단점이 명확하며, **사용 목적과 요구사항에 따라 적절히 선택**해야 합니다.

- **간단한 CRUD**: Native Query
- **복잡한 검색**: JPA Specification
- **하이브리드 접근**: 기본 조회는 Native Query, 검색 기능은 Specification

현재 프로젝트는 두 방식을 모두 구현하여 **각 방식의 특징과 사용법을 학습할 수 있는 좋은 예제**를 제공합니다.
