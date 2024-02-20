package com.example.mysns.controller;

import com.example.mysns.dto.comment.*;
import com.example.mysns.entity.Response;
import com.example.mysns.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = {"Comment API"})
public class CommentRestController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    @ApiOperation(value="댓글 등록", notes="로그인 후 포스트에 댓글을 등록 할 수 있습니다.")
    public Response<CommentResponse> create(@ApiIgnore Principal principal, @RequestBody CommentRequest commentRequest, @PathVariable Long postId){
        String email = principal.getName();
        CommentResponse commentResponse = commentService.writeComment(commentRequest, email, postId);

        return Response.success(commentResponse);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @ApiOperation(value="댓글 수정", notes="로그인 후 나의 댓글을 수정 할 수 있습니다.")
    public Response<CommentEditResponse> update(@ApiIgnore Principal principal, @RequestBody CommentRequest commentRequest, @PathVariable Long postId, @PathVariable Long commentId){
        String email = principal.getName();
        CommentEditResponse commentEditResponse = commentService.editComment(commentRequest, email, postId, commentId);

        return Response.success(commentEditResponse);
    }


    @DeleteMapping("/{postId}/comments/{commentId}")
    @ApiOperation(value="댓글 삭제", notes="로그인 후 나의 댓글을 삭제 할 수 있습니다.")
    public Response<CommentResultResponse> delete(@ApiIgnore Principal principal, @PathVariable Long postId, @PathVariable Long commentId){
        String email = principal.getName();
        return Response.success(new CommentResultResponse("댓글이 삭제되었습니다.", commentService.deleteComment(email, postId, commentId)));
    }

    @GetMapping("/{postId}/comments")
    @ApiOperation(value="댓글 조회", notes="해당 포스터 댓글 정보를 조회할 수 있습니다.")
    public Response<Page<CommentDetailResponse>> findByPostId(@PathVariable Long postId, @PageableDefault(size = 15, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(commentService.findById(pageable, postId));
    }
}
