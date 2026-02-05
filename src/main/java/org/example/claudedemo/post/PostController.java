package org.example.claudedemo.post;

import lombok.RequiredArgsConstructor;
import org.example.claudedemo.post.dto.PostCreateRequestDto;
import org.example.claudedemo.post.dto.PostResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.findAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto post = postService.findPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByMember(@PathVariable Long memberId) {
        List<PostResponseDto> posts = postService.findPostsByMemberId(memberId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateRequestDto request) {
        PostResponseDto post = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
