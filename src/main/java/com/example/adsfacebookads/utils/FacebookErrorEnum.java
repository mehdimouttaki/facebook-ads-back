package com.example.adsfacebookads.utils;

import java.util.HashMap;
import java.util.Map;

public interface FacebookErrorEnum extends FacbookEnum{
    String message();

    default Map<String, String> details() {
        return new HashMap();
    }
}
