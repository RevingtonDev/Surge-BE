package dev.revington.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CookieUtil {

    public static void createCookie(HttpServletResponse res, String name, String value, String path, String domain, boolean http, boolean secure, long max) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, Base64.getUrlEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8)))
                .httpOnly(http)
                .secure(secure)
                .path(path)
                .domain(domain);
        if (max > 0) {
            cookieBuilder = cookieBuilder.maxAge(max);
        }
        res.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
    }

    public static void clearCookie(HttpServletResponse res,  String name, String domain, String path, boolean http, boolean secure) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, "")
                .httpOnly(http)
                .maxAge(0)
                .secure(secure)
                .path(path)
                .domain(domain);
        res.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
    }

    public static String getDomain(HttpServletRequest req) {
        String domain = req.getRequestURL().toString().replaceAll(req.getRequestURI(), "");
        if(domain.contains("/"))
            domain = domain.substring(domain.lastIndexOf('/') + 1, /* ':' uses in local servers. */ (domain.contains(":") ? domain.lastIndexOf(':') : domain.length()));

        return domain;
    }

}
