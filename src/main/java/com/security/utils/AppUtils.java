package com.security.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
public class AppUtils {

    public static String getTokenValue(HttpServletRequest request) {

        try {
            return request.getHeader("authorization").split(" ")[1];
        } catch (Exception ex) {
            log.error("!!! Token not available on header.");
        }

        try {
            return request.getHeader("Authorization").split(" ")[1];
        } catch (Exception ex) {
            log.error("!!! Token not available on header.");
        }
        return "";
    }

    public static String toString(Object str) {
        if (str == null) {
            return "";
        }
        try {
            return String.valueOf(str);
        } catch (Exception ex) {
            // ex.printStackTrace();
        }

        return "";
    }

    public static long toLong(Object number) {
        try {
            return Long.parseLong(number + "");
        } catch (Exception ex) {
            // ex.printStackTrace();
        }

        return 0;
    }

    public static long getLong(Map<?, ?> obj, String key) {
        if (obj == null || key == null) {
            return 0;
        }
        try {
            return toLong(obj.get(key));
        } catch (Exception ex) {
            // ex.printStackTrace();
        }

        return 0;
    }
}
