package com.example.mysns.dto.post;

import lombok.Getter;

@Getter
public class PostResultResponse {
    private String message;
    private Long postId;

    private PostResultResponse() {
    }

    public PostResultResponse(String message, Long id) {
        this.message = message;
        this.postId = id;
    }
}
