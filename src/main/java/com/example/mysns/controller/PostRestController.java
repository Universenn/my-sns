package com.example.mysns.controller;

import com.example.mysns.dto.post.PostDetailResponse;
import com.example.mysns.dto.post.PostRequest;
import com.example.mysns.dto.post.PostResultResponse;
import com.example.mysns.entity.Response;
import com.example.mysns.service.PostService;
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
@Api(tags = {"2_Post API"})
public class PostRestController {

    private final PostService postService;

    @PostMapping
    @ApiOperation(value="포스트 작성", notes="로그인을 하면 포스트를 작성할 수 있습니다.")
    public Response<PostResultResponse> create(@ApiIgnore Principal principal, @RequestBody PostRequest postRequest){
        String email = principal.getName();
        Long postId = postService.create(email, postRequest);

        return Response.success(new PostResultResponse("게시글 등록 완료", postId));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="포스트 수정", notes="로그인을 하면 나의 포스트를 수정할 수 있습니다.")
    public Response<PostResultResponse> update(@ApiIgnore Principal principal, @PathVariable Long id, @RequestBody PostRequest updateRequest) {
        Long postId = postService.update(principal.getName(), id, updateRequest);
        return Response.success(new PostResultResponse("게시글 수정 완료", postId));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="포스트 삭제", notes="로그인을 하면 나의 포스트를 삭제할 수 있습니다.")
    public Response<PostResultResponse> delete(@ApiIgnore Principal principal, @PathVariable Long id) {
        Long postId = postService.delete(principal.getName(), id);

        return Response.success(new PostResultResponse("게시글 삭제 완료", postId));
    }

    @GetMapping("")
    @ApiOperation(value="포스트 조회", notes="전체 포스트를 정보를 조회할 수 있습니다.")
    public Response<Page<PostDetailResponse>> findAll(@PageableDefault(size = 20, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDetailResponse> responses = postService.findAll(pageable);
        return Response.success(responses);
    }


    @GetMapping("/{id}")
    @ApiOperation(value="포스트 상세조회", notes="포스트를 상제정보를 조회할 수 있습니다.")
    public Response<PostDetailResponse> findById(@PathVariable Long id) {
        return Response.success(postService.findById(id));
    }


    @GetMapping("/my")
    @ApiOperation(value="마이피드", notes="로그인된 유저만의 피드목록을 조회 할 수 있습니다.")
    public Response<Page<PostDetailResponse>> myPage(@PageableDefault(size = 20, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Principal principal) {
        Page<PostDetailResponse> responses = postService.findByUserAll(principal.getName(), pageable);
        return Response.success(responses);
    }


    @PostMapping("/{id}/likes")
    @ApiOperation(value="좋아요 누르기", notes="해당 포스트에 좋아요를 누를 수 있습니다.")
    public Response<String> likes(@ApiIgnore Principal principal, @PathVariable Long id) {
        boolean likes = postService.likes(id, principal.getName());

        return Response.success(likes ? "좋아요를 눌렀습니다" : "좋아요를 취소했습니다");
    }
    @GetMapping("/{id}/likes")
    @ApiOperation(value="좋아요 조회", notes="해당 포스트에 대한 좋아요 개수를 조회할 수 있습니다.")
    public Response<Integer> getLikes(@PathVariable Long id) {
        return Response.success(postService.findById(id).getLikes());
    }
}
