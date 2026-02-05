package org.example.claudedemo.member;

import lombok.RequiredArgsConstructor;
import org.example.claudedemo.global.exception.BusinessException;
import org.example.claudedemo.global.exception.ErrorCode;
import org.example.claudedemo.member.dto.MemberResponseDto;
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
