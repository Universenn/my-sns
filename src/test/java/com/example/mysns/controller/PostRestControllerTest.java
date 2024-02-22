package com.example.mysns.controller;

import com.example.mysns.dto.post.PostDetailResponse;
import com.example.mysns.dto.post.PostRequest;
import com.example.mysns.dto.post.PostResultResponse;
import com.example.mysns.entity.Post;
import com.example.mysns.entity.User;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostRestController.class)
class PostRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper; // java object 를 json 형식으로 바꿔주는 잭슨의 오브젝트 이다

    PostRequest postRequest = new PostRequest("글 제목", "내용내용");


    @Test
    @WithMockUser
    @DisplayName("게시글 등록 성공")
    void post_success() throws Exception {
        PostResultResponse postResultResponse = new PostResultResponse("게시글 등록 완료", 0L);

        given(postService.create(any(),any(PostRequest.class))).willReturn(postResultResponse.getPostId());

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.message").value("게시글 등록 완료"))
                .andExpect(jsonPath("$.result.postId").value(0L));


        verify(postService).create(any(String.class), any());
    }


    @Test
    @WithAnonymousUser
    @DisplayName("게시글 등록 실패(token 인증 x)")
    void post_fail() throws Exception {
        when(postService.create(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage() ));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 수정 성공")
    void post_edit_success() throws Exception {
        PostResultResponse postResultResponse = new PostResultResponse("게시글 수정 완료", 1L);

        given(postService.update(any(), any(),any(PostRequest.class))).willReturn(postResultResponse.getPostId());

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.message").value("게시글 수정 완료"))
                .andExpect(jsonPath("$.result.postId").value(1L));


        verify(postService).update(any(), any(), any());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("게시글 수정 실패(권한 인증 실패)")
    void post_edit_fail_1() throws Exception {
        given(postService.update(any(), any(),any(PostRequest.class))).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }


    @Test
    @WithMockUser
    @DisplayName("게시글 수정 실패(포스트 찾을 수 없음)")
    void post_edit_fail_2() throws Exception {
        given(postService.update(any(), any(),any(PostRequest.class))).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/3")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 수정 실패(작성자 불일치)")
    void post_edit_fail_3() throws Exception {
        given(postService.update(any(), any(),any(PostRequest.class))).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/3")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }



    @Test
    @WithMockUser
    @DisplayName("게시글 삭제 성공")
    void post_delete_success() throws Exception {
        PostResultResponse postResultResponse = new PostResultResponse("게시글 삭제 완료", 1L);

        given(postService.delete(any(), any())).willReturn(postResultResponse.getPostId());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf()))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.message").value("게시글 삭제 완료"))
                .andExpect(jsonPath("$.result.postId").value(1L));


        verify(postService).delete(any(), any());
    }


    @Test
    @WithAnonymousUser
    @DisplayName("게시글 삭제 실패(권한 인증 실패)")
    void post_delete_fail_1() throws Exception {
        given(postService.delete(any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf()))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }


    @Test
    @WithMockUser
    @DisplayName("게시글 삭제 실패(포스트 찾을 수 없음)")
    void post_delete_fail_2() throws Exception {
        given(postService.delete(any(), any())).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/3")
                        .with(csrf()))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 삭제 실패(작성자 불일치)")
    void post_delete_fail_3() throws Exception {
        given(postService.delete(any(), any())).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/3")
                        .with(csrf()))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }


    @Test
    @WithMockUser
    @DisplayName("게시글 단권 조회 성공")
    void post_findById_success() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        PostDetailResponse postDetailResponse = new PostDetailResponse(1L, "제목", "내용", "nickname", 2, now, now);

        given(postService.findById(any())).willReturn(postDetailResponse);

        mockMvc.perform(get("/api/v1/posts/1"))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(postDetailResponse.getId()))
                .andExpect(jsonPath("$.result.title").value(postDetailResponse.getTitle()))
                .andExpect(jsonPath("$.result.body").value(postDetailResponse.getBody()))
                .andExpect(jsonPath("$.result.nickname").value(postDetailResponse.getNickname()))
                .andExpect(jsonPath("$.result.likes").value(postDetailResponse.getLikes()))
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());


        verify(postService).findById(any());
    }
    @Test
    @WithMockUser
    @DisplayName("게시글 조회 실패(포스트 찾을 수 없음)")
    void post_findById_fail_1() throws Exception {
        given(postService.findById(any())).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/posts/3"))
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 리스트 조회 성공")
    @WithMockUser
    void readAll_success() throws Exception {
        mockMvc.perform(get("/api/v1/posts")
                        .param("size", "20")
                        .param("sort", "createdAt, DESC"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).findAll(pageableArgumentCaptor.capture());
        PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

        assertEquals(20, pageRequest.getPageSize());
        assertEquals(Sort.by("createdAt", "DESC"), pageRequest.withSort(Sort.by("createdAt", "DESC")).getSort());
    }


    @Test
    @WithMockUser
    @DisplayName("게시글 좋아요 성공")
    void post_like_success() throws Exception {

        String message = "좋아요를 눌렀습니다";

        given(postService.likes(any(), any())).willReturn(true);

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value(message));


        verify(postService).likes(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 좋아요 취소")
    void post_unlike_success() throws Exception {

        String message = "좋아요를 취소했습니다";

        given(postService.likes(any(), any())).willReturn(false);

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value(message));


        verify(postService).likes(any(), any());
    }
    @Test
    @WithMockUser
    @DisplayName("게시글 좋아요 카운트 성공")
    void post_like_count_success() throws Exception {

        User user = new User(1L, "email@gmail.com", "nickname", "password");
        PostDetailResponse postDetailResponse = PostDetailResponse.of(new Post(1L, "제목", "내용", 3, user));
        given(postService.findById(any())).willReturn(postDetailResponse);

        mockMvc.perform(get("/api/v1/posts/1/likes")
                        .with(csrf()))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value(3));


        verify(postService).findById(any());
    }


    @Test
    @WithAnonymousUser
    @DisplayName("게시글 좋아요 카운트 실패()")
    void post_like_count_fail() throws Exception {

            given(postService.findById(any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

            mockMvc.perform(get("/api/v1/posts/1/likes")
                            .with(csrf()))
                    .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("마이피드 조회 성공")
    void my_feed_success() throws Exception {

        User user = new User(1L, "email@gmail.com", "nickname", "password");

        final Page<PostDetailResponse> posts = new PageImpl<>(List.of(
                PostDetailResponse.of(new Post(1L, "제목1" , "내용1", 1, user)),
                PostDetailResponse.of(new Post(2L, "제목2" , "내용2", 2, user)),
                PostDetailResponse.of(new Post(3L, "제목3" , "내용3", 3, user))
        ));

        given(postService.findByUserAll(any(), any())).willReturn(posts);

        mockMvc.perform(get("/api/v1/posts/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.pageable").exists())
                .andExpect(jsonPath("$.result.size").exists());


        verify(postService).findByUserAll(any(), any(Pageable.class));

    }

    @Test
    @WithAnonymousUser
    @DisplayName("마이피드 조회 실패")
    void my_feed_fail() throws Exception {

        when(postService.findByUserAll(any(), any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(get("/api/v1/posts/my"))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }
}