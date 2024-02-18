package com.example.mysns.controller;

import com.example.mysns.dto.comment.*;
import com.example.mysns.entity.Response;
import com.example.mysns.service.CommentService;
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
public class CommentRestController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public Response<CommentResponse> create(Principal principal, @RequestBody CommentRequest commentRequest, @PathVariable Long postId){
        String email = principal.getName();
        CommentResponse commentResponse = commentService.writeComment(commentRequest, email, postId);

        return Response.success(commentResponse);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public Response<CommentEditResponse> update(Principal principal, @RequestBody CommentRequest commentRequest, @PathVariable Long postId, @PathVariable Long commentId){
        String email = principal.getName();
        CommentEditResponse commentEditResponse = commentService.editComment(commentRequest, email, postId, commentId);

        return Response.success(commentEditResponse);
    }


    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<CommentResultResponse> delete(Principal principal, @PathVariable Long postId, @PathVariable Long commentId){
        String email = principal.getName();
        return Response.success(new CommentResultResponse("댓글이 삭제되었습니다.", commentService.deleteComment(email, postId, commentId)));
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentDetailResponse>> findByPostId(@PathVariable Long postId, @PageableDefault(size = 15, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(commentService.findById(pageable, postId));
    }
}
