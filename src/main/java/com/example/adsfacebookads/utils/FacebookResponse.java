package com.example.adsfacebookads.utils;

import java.time.Instant;

public class FacebookResponse<T> {
    private FacebookCodeRsp code;
    private Long timestamp;
    private T data;
    private FacbookError error;

    public FacebookResponse() {
        this.timestamp = Instant.now().getEpochSecond();
    }

    public FacebookResponse(FacebookCodeRsp code) {
        this();
        this.code = code;
    }

    public FacebookResponse(FacebookCodeRsp code, T data) {
        this(code);
        this.data = data;
    }

    public FacebookResponse(FacebookCodeRsp code, FacebookErrorEnum error) {
        this(code);
        this.error = new FacbookError(error.code(), error.content(), error.message(), error.details());
    }

    public FacebookCodeRsp getCode() {
        return this.code;
    }

    public void setCode(FacebookCodeRsp code) {
        this.code = code;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public FacbookError getError() {
        return this.error;
    }

    public void setError(FacbookError error) {
        this.error = error;
    }
}
