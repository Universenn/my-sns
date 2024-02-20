package com.example.mysns.controller;

import com.example.mysns.dto.post.PostRequest;
import com.example.mysns.dto.post.PostDetailResponse;
import com.example.mysns.dto.post.PostResultResponse;
import com.example.mysns.entity.Response;
import com.example.mysns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostRestController {

    private final PostService postService;

    @PostMapping
    public Response<PostResultResponse> create(Principal principal, @RequestBody PostRequest postRequest){
        String email = principal.getName();
        Long postId = postService.create(email, postRequest);
        log.info("delete controller post :{}", postId);

        return Response.success(new PostResultResponse("게시글 등록 완료", postId));
    }

    @PutMapping("/{id}")
    public Response<PostResultResponse> update(Principal principal, @PathVariable Long id, @RequestBody PostRequest updateRequest) {
        Long postId = postService.update(principal.getName(), id, updateRequest);
        log.info("delete controller post :{}", postId);
        return Response.success(new PostResultResponse("게시글 수정 완료", postId));
    }

    @DeleteMapping("/{id}")
    public Response<PostResultResponse> delete(Principal principal, @PathVariable Long id) {
        Long postId = postService.delete(principal.getName(), id);
        log.info("delete controller post :{}", postId);

        return Response.success(new PostResultResponse("게시글 삭제 완료", postId));
    }

    @GetMapping("")
    public Response<Page<PostDetailResponse>> findAll(@PageableDefault(size = 20, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDetailResponse> responses = postService.findAll(pageable);
        return Response.success(responses);
    }


    @GetMapping("/{id}")
    public Response<PostDetailResponse> findById(@PathVariable Long id) {
        return Response.success(postService.findById(id));
    }


    @GetMapping("/my")
    public Response<Page<PostDetailResponse>> myPage(@PageableDefault(size = 20, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {
        Page<PostDetailResponse> responses = postService.findByUserAll(principal.getName(), pageable);
        return Response.success(responses);
    }


    @PostMapping("/{id}/likes")
    public Response<String> likes(Principal principal, @PathVariable Long id) {
        boolean likes = postService.likes(id, principal.getName());

        return Response.success(likes ? "좋아요를 눌렀습니다" : "좋아요를 취소했습니다");
    }
    @GetMapping("/{id}/likes")
    public Response<Integer> getLikes(@PathVariable Long id) {
        return Response.success(postService.findById(id).getLikes());
    }
}
