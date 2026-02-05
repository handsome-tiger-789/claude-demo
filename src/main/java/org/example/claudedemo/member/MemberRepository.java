package org.example.claudedemo.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    @Query(value = "SELECT id, name FROM member WHERE id = :id", nativeQuery = true)
    Optional<MemberResponseDto> findMemberById(@Param("id") Long id);
}
