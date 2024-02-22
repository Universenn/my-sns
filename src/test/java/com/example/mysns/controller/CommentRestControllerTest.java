package com.example.mysns.controller;

import com.example.mysns.dto.comment.*;
import com.example.mysns.entity.Comment;
import com.example.mysns.entity.Post;
import com.example.mysns.entity.User;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @Autowired
    ObjectMapper objectMapper; // java object 를 json 형식으로 바꿔주는 잭슨의 오브젝트 이다

    CommentRequest commentRequest = new CommentRequest("댓글입니다");

    @Test
    @WithMockUser
    @DisplayName("댓글 등록 성공")
    void comment_success() throws Exception {
        CommentResponse commentResponse = new CommentResponse(1L, "댓글","닉네임",2L, LocalDateTime.now());

        given(commentService.writeComment(any(CommentRequest.class), any(), any())).willReturn(commentResponse);

        mockMvc.perform(post("/api/v1/posts/2/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.comment").value("댓글"))
                .andExpect(jsonPath("$.result.nickname").value("닉네임"))
                .andExpect(jsonPath("$.result.postId").value(2L))
                .andExpect(jsonPath("$.result.createdAt").exists());


        verify(commentService).writeComment(any(CommentRequest.class), any(), any());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("댓글 등록 실패(token 인증 x)")
    void comment_fail_1() throws Exception {
        when(commentService.writeComment(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage() ));


        mockMvc.perform(post("/api/v1/posts/1/comments")
                                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 등록 실패(post not fount)")
    void comment_fail_2() throws Exception {
        when(commentService.writeComment(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage() ));


        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }


    @Test
    @WithMockUser
    @DisplayName("댓글 수정 성공")
    void comment_edit_success() throws Exception {
        CommentEditResponse commentEditResponse = new CommentEditResponse(1L, "댓글 수정", "닉네임", 2L, LocalDateTime.now(), LocalDateTime.now());

        given(commentService.editComment(any(CommentRequest.class), any(), any(), any())).willReturn(commentEditResponse);

        mockMvc.perform(put("/api/v1/posts/2/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.comment").value("댓글 수정"))
                .andExpect(jsonPath("$.result.nickname").value("닉네임"))
                .andExpect(jsonPath("$.result.postId").value(2L))
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());


        verify(commentService).editComment(any(CommentRequest.class), any(), any(), any());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("댓글 수정 실패(권한 인증 실패)")
    void comment_edit_fail_1() throws Exception {
        given(commentService.editComment(any(CommentRequest.class), any(),any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }


    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(포스트 찾을 수 없음)")
    void comment_edit_fail_2() throws Exception {
        given(commentService.editComment(any(CommentRequest.class), any(),any(), any())).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/3/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(작성자 불일치)")
    void comment_edit_fail_3() throws Exception {
        given(commentService.editComment(any(CommentRequest.class), any(),any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/3/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    ////////////////


    @Test
    @WithMockUser
    @DisplayName("삭제 수정 성공")
    void comment_delete_success() throws Exception {
        CommentResultResponse commentResultResponse = new CommentResultResponse("댓글이 삭제되었습니다.", 1L);

        given(commentService.deleteComment(any(), any(), any())).willReturn(commentResultResponse.getId());

        mockMvc.perform(delete("/api/v1/posts/2/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.message").value(commentResultResponse.getMessage()))
                .andExpect(jsonPath("$.result.id").value(1L));


        verify(commentService).deleteComment(any(), any(), any());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("삭제 수정 실패(권한 인증 실패)")
    void comment_delete_fail_1() throws Exception {
        given(commentService.deleteComment(any(),any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }


    @Test
    @WithMockUser
    @DisplayName("삭제 수정 실패(포스트 찾을 수 없음)")
    void comment_delete_fail_2() throws Exception {
        given(commentService.deleteComment(any(),any(), any())).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/3/comments/1")
                        .with(csrf()))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("삭제 수정 실패(작성자 불일치)")
    void comment_delete_fail_3() throws Exception {
        given(commentService.deleteComment(any(),any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/3/comments/1")
                        .with(csrf()))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 리스트 조회 성공")
    @WithMockUser
    void comment_readAll_success() throws Exception {
        User user = new User(1L, "email@gmail.com", "nickname", "password");

        Post post = new Post(1L, "제목1", "내용1", 1, user);

        final Page<CommentDetailResponse> comments = new PageImpl<>(List.of(
                CommentDetailResponse.of(new Comment(1L, "댓글1", user, post)),
                CommentDetailResponse.of(new Comment(2L, "댓글2", user, post)),
                CommentDetailResponse.of(new Comment(3L, "댓글3", user, post))
        ));

        given(commentService.findById(any(Pageable.class), any())).willReturn(comments);

        mockMvc.perform(get("/api/v1/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.pageable").exists())
                .andExpect(jsonPath("$.result.size").exists());

        verify(commentService).findById(any(Pageable.class), any());
    }

}