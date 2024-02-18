package com.example.mysns.dto.comment;

import com.example.mysns.entity.Comment;
import com.example.mysns.entity.Post;
import com.example.mysns.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CommentRequest {

    private String comment;

    public Comment toEntity(Post post, User user) {
        return Comment.builder()
                .comment(comment)
                .post(post)
                .user(user)
                .build();
    }
    public Comment toEntity() {
        return Comment.builder()
                .comment(comment)
                .build();
    }
}
