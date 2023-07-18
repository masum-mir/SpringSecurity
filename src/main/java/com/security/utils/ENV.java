package com.security.utils;

public class ENV {

    public static class DB {
        public static class AUTH {
            public static final String USERNAME = "DB_USERNAME";
            public static final String PASSWORD = "DB_PASSWORD";
            public static final String URL = "DB_URL";
            public static final String DRIVER = "DRIVER";
        }

        public static class CORE {
            public static final String USERNAME = "DB_USERNAME";
            public static final String PASSWORD = "DB_PASSWORD";
            public static final String URL = "DB_URL";
            public static final String DRIVER = "DRIVER";
        }

    }

    private static final String TOKEN = "25442A472D4B6150645367566B597033733676397924423F4528482B4D625165";

    public static String getToken() {
        return TOKEN;
    }

}
