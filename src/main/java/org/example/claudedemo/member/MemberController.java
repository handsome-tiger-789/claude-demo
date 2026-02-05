package org.example.claudedemo.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        MemberResponseDto member = memberService.findMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/spec/{id}")
    public ResponseEntity<MemberResponseDto> getMemberBySpec(@PathVariable Long id) {
        MemberResponseDto member = memberService.findMemberByIdUsingSpecification(id);
        return ResponseEntity.ok(member);
    }
}
