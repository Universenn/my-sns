package com.example.mysns.dto.comment;

import com.example.mysns.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDetailResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;


    public static CommentDetailResponse of(Comment comment) {
        return CommentDetailResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getEmail())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedDateTime())
                .lastModifiedAt(comment.getLastModifiedDateTime())
                .build();
    }
}
