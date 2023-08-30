package com.vcloudapi.utils;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

    private final static String TOKEN_PREFIX = "Bearer";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
