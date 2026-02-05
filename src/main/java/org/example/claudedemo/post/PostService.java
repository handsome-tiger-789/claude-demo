package org.example.claudedemo.post;

import lombok.RequiredArgsConstructor;
import org.example.claudedemo.global.exception.BusinessException;
import org.example.claudedemo.global.exception.ErrorCode;
import org.example.claudedemo.member.Member;
import org.example.claudedemo.member.MemberRepository;
import org.example.claudedemo.post.dto.PostCreateRequestDto;
import org.example.claudedemo.post.dto.PostResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public List<PostResponseDto> findAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::from)
                .toList();
    }

    public PostResponseDto findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        return PostResponseDto.from(post);
    }

    public List<PostResponseDto> findPostsByMemberId(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return postRepository.findByMemberId(memberId).stream()
                .map(PostResponseDto::from)
                .toList();
    }

    @Transactional
    public PostResponseDto createPost(PostCreateRequestDto request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setMember(member);
        post.setViewCount(0L);

        Post savedPost = postRepository.save(post);
        return PostResponseDto.from(savedPost);
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        postRepository.deleteById(id);
    }
}
