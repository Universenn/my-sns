package com.example.mysns.entity;

import lombok.Getter;

@Getter
public class Response<T> {
    private final String resultCode;
    private final T result;

    private Response(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }


    public static <T> Response<T> error(String resultCode, T result) {
        return new Response<>(resultCode, result);
    }

    public static <T> Response<T> success (T result) {
        return new Response<>("SUCCESS", result);
    }
}
