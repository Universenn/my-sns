package com.example.mysns.service;

import com.example.mysns.dto.post.PostRequest;
import com.example.mysns.dto.post.PostDetailResponse;
import com.example.mysns.entity.Post;
import com.example.mysns.entity.User;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.repository.PostRepository;
import com.example.mysns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public Long create(String email, PostRequest dto) {
        User user = findUserByEmail(email);
        Post post = postRepository.save(dto.toEntity(user));
        log.info("create service post :{}", post.getUser().getNickname());
        return post.getId();
    }

    // 게시글 수정
    @Transactional
    public Long update(String email, Long id , PostRequest updateRequest) {
        Post post = findTargetPost(email, id);

        post.update(updateRequest.toEntity());
        log.info("update service post :{}", post.getUser().getNickname());

        return post.getId();
    }

    // 게시글 삭제
    @Transactional
    public Long delete(String email, Long id) {
        Post post = findTargetPost(email, id);
        postRepository.delete(post);
        log.info("delete service post :{}", post.getUser().getNickname());

        return post.getId();
    }


    // 단권 게시글 찾기
    public PostDetailResponse findById(Long id) {
        return PostDetailResponse.of(findPostById(id));
    }

    // 모든 게시글 찾기
    public Page<PostDetailResponse> findAll(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostDetailResponse::of);
    }

    // 내 모든 게시물 찾기
    public Page<PostDetailResponse> findByUserAll(String email, Pageable pageable) {
        User user = findUserByEmail(email);
        return postRepository.findAllByUser(user,pageable).map(PostDetailResponse::of);
    }
    // 본인 글인지 확인
    private Post findTargetPost(String email, Long id) {
        Post post = findPostById(id);

        User user = findUserByEmail(email);

        verifyAccessiblePost(post, user);

        return post;
    }


    // 글쓴 유저와, 접근하는 유저가 같은지 확인
    private static void verifyAccessiblePost(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    // 이메일 찾기
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(
                    ErrorCode.EMAIL_NOT_FOUND,
                    ErrorCode.EMAIL_NOT_FOUND.getMessage());
        });
    }
    // 게시글 찾기
    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new AppException(
                    ErrorCode.POST_NOT_FOUND,
                    ErrorCode.POST_NOT_FOUND.getMessage());
        });
    }

}
