package org.example.claudedemo.service;

import lombok.RequiredArgsConstructor;
import org.example.claudedemo.dto.MemberResponseDto;
import org.example.claudedemo.entity.Member;
import org.example.claudedemo.exception.BusinessException;
import org.example.claudedemo.exception.ErrorCode;
import org.example.claudedemo.repository.MemberRepository;
import org.example.claudedemo.specification.MemberSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDto findMemberById(Long id) {
        return memberRepository.findMemberById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public MemberResponseDto findMemberByIdUsingSpecification(Long id) {
        Specification<Member> spec = Specification.where(MemberSpecification.equalId(id));
        Member member = memberRepository.findOne(spec)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberResponseDto(member.getId(), member.getName());
    }
}
