package com.example.adsfacebookads.utils;

public enum FacebookCodeRsp implements FacbookEnum{
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED"),
    ERROR("ERROR");

    private final String content;

    private FacebookCodeRsp(String content) {
        this.content = content;
    }

    public String code() {
        return this.name();
    }

    public String content() {
        return this.content;
    }
}
