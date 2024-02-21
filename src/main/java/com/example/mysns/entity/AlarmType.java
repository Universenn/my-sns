package com.example.mysns.entity;

public enum AlarmType {
    NEW_COMMENT_ON_POST("새로운 댓글"),
    NEW_LIKE_ON_POST("좋아요");

    private final String message;

    AlarmType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
