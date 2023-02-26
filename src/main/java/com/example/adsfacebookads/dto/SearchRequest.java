package com.example.adsfacebookads.dto;

public class SearchRequest {
    private String field;
    private String value;

    public SearchRequest() {
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "SearchResponse{field='" + this.field + '\'' + ", value='" + this.value + '\'' + '}';
    }
}
