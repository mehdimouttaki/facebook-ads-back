package com.example.adsfacebookads.utils;

import java.util.List;

public class SearchResponse<E> {
    private Long searchCount;
    private List<E> searchValue;

    public SearchResponse() {
    }

    public Long getSearchCount() {
        return this.searchCount;
    }

    public void setSearchCount(Long searchCount) {
        this.searchCount = searchCount;
    }

    public List<E> getSearchValue() {
        return this.searchValue;
    }

    public void setSearchValue(List<E> searchValue) {
        this.searchValue = searchValue;
    }
}
