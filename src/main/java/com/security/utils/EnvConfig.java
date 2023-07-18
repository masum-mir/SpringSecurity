package com.security.utils;

public class EnvConfig {

    public static String getString(String key, String defaultValue) {
        String value = System.getenv(key);
        if(value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }
}
