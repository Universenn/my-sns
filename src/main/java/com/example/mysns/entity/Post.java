package com.example.mysns.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    private int likes;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void update(Post update) {
        this.title = update.getTitle();
        this.body = update.getBody();
    }

    public synchronized void likes() {
        likes++;
    }

    public synchronized void unlikes() {
        if (likes > 0) likes--;
    }
}
