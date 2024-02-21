package com.example.mysns.dto.comment;

import lombok.Getter;

@Getter
public class CommentResultResponse {
    private String message;
    private Long id;

    private CommentResultResponse() {
    }

    public CommentResultResponse(String message, Long id) {
        this.message = message;
        this.id = id;
    }
}