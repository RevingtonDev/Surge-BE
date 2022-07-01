package dev.revington.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static void createCookie(HttpServletResponse res, String name, String value, String path, String domain, boolean http, boolean secure, long max) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, value)
                .httpOnly(http)
                .maxAge(max)
                .secure(secure)
                .path(path)
                .domain(domain);
        res.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
    }

    public static void clearCookie(HttpServletResponse res, String path, String domain, String name, boolean http, boolean secure) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, "")
                .httpOnly(http)
                .maxAge(0)
                .secure(secure)
                .path(path)
                .domain(domain);
        res.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
    }

}
