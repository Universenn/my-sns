package com.example.mysns.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean liked;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime lastModifiedDateTime;

    @Builder
    public PostLike(Long id, Post post, User user) {
        this.id = id;
        this.post = post;
        this.user = user;
    }

    public static PostLike of(Post post, User user) {
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }

    public boolean likes() {
        if (liked) post.unlikes();
        else post.likes();
        toggleLikes();
        return liked;
    }

    private void toggleLikes() {
        liked = !liked;
    }
}
