package com.example.mysns.service;

import com.example.mysns.dto.comment.CommentDetailResponse;
import com.example.mysns.dto.comment.CommentEditResponse;
import com.example.mysns.dto.comment.CommentRequest;
import com.example.mysns.dto.comment.CommentResponse;
import com.example.mysns.entity.Comment;
import com.example.mysns.entity.Post;
import com.example.mysns.entity.User;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.observer.events.AlarmEvent;
import com.example.mysns.repository.CommentRepository;
import com.example.mysns.repository.PostRepository;
import com.example.mysns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.mysns.entity.AlarmType.NEW_COMMENT_ON_POST;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;


    @Transactional
    public CommentResponse writeComment(CommentRequest dto, String email, Long id) {
        User user = findUserByEmail(email);

        Post post = findPostById(id);

        publisher.publishEvent(AlarmEvent.of(NEW_COMMENT_ON_POST, post.getUser(), user));

        return CommentResponse.of(commentRepository.save(dto.toEntity(post, user)));
    }

    @Transactional
    public CommentEditResponse editComment(CommentRequest dto, String email, Long postId, Long commentId) {
        Comment comment = findTargetComment(email, commentId, postId);
        comment.update(dto.toEntity());
        return CommentEditResponse.of(comment);
    }

    @Transactional
    public Long deleteComment(String email, Long postId, Long commentId) {
        Comment comment = findTargetComment(email, commentId, postId);
        commentRepository.delete(comment);
        return comment.getId();
    }


    public Page<CommentDetailResponse> findById(Pageable pageable, Long postId) {
        return commentRepository.findAllByPost(pageable, findPostById(postId)).map(CommentDetailResponse::of);
    }

    private Comment findTargetComment(String email, Long commentId, Long postId) {
        Post post = findPostById(postId);
        Comment comment = findCommentById(commentId);
        User user = findUserByEmail(email);
        validRequest(comment, post);
        verifyAccessibleComment(comment, user);
        return comment;
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new AppException(
                ErrorCode.COMMENT_NOT_FOUND,
                ErrorCode.COMMENT_NOT_FOUND.getMessage()
        ));
    }

    private static void verifyAccessibleComment(Comment comment, User user) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AppException(
                    ErrorCode.INVALID_PERMISSION,
                    ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new AppException(
                ErrorCode.POST_NOT_FOUND,
                ErrorCode.POST_NOT_FOUND.getMessage()
        ));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(
                ErrorCode.EMAIL_NOT_FOUND,
                ErrorCode.EMAIL_NOT_FOUND.getMessage()
        ));
    }

    private void validRequest(Comment comment, Post post) {
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new AppException(
                    ErrorCode.INVALID_PERMISSION,
                    ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }
}
