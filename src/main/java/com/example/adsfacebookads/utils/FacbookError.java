package com.example.adsfacebookads.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Map;
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
public class FacbookError implements FacebookErrorEnum{
    private String code;
    private String content;
    private String message;
    private Map<String, String> details;

    public FacbookError() {
    }

    public FacbookError(String code, String content, String message) {
        this();
        this.code = code;
        this.content = content;
        this.message = message;
    }

    public FacbookError(String code, String content, String message, Map<String, String> details) {
        this(code, content, message);
        this.details = details;
    }

    public String code() {
        return this.code;
    }

    public String content() {
        return this.content;
    }

    public String message() {
        return this.message;
    }

    public Map<String, String> details() {
        return this.details;
    }
}
