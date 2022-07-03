package dev.revington.util;

import dev.revington.entity.Token;
import dev.revington.entity.User;
import dev.revington.repo.TokenRepository;
import dev.revington.repo.UserRepository;
import dev.revington.security.Crypto;
import dev.revington.status.StatusHandler;
import dev.revington.variables.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AccessToken {

    public static Token generateToken(User user, Timestamp timestamp) throws NoSuchAlgorithmException {
        Token token = new Token();
        token.setId(user.getId());
        token.setKey(Crypto.generateKey());
        token.setToken(Crypto.getMD5(Base64.getEncoder().encodeToString(user.getEmail().getBytes(StandardCharsets.UTF_8))) +
                Crypto.getMD5(user.getFirstName() + " " + user.getLastName()) +
                Crypto.getMD5(Base64.getEncoder().encodeToString(timestamp.toString().getBytes(StandardCharsets.UTF_8))));
        token.setExpires(new Timestamp(timestamp.getTime() + (365L * 24 * 60 * 60 * 1000)));
        return token;
    }

    public static User validate(HttpServletRequest req, HttpServletResponse res, TokenRepository repo, UserRepository userRepo) {
        String token = retrieveClientToken(req);
        Optional<Token> accessToken = repo.findByToken(token);
        Optional<User> user;
        if(accessToken.isEmpty() || new Timestamp(new Date().getTime()).compareTo(accessToken.get().getExpires()) > 0 || (user = userRepo.findById(accessToken.get().getId())).isEmpty()) {
            CookieUtil.clearCookie(res, Parameter.COOKIE_TOKEN, CookieUtil.getDomain(req), "/", true, false);
            return null;
        }

         return user.get();
    }

    public static String retrieveClientToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if(cookies == null || cookies.length < 1)
            return "";

        for (Cookie cookie : cookies)
            if(cookie.getName().equals(Parameter.COOKIE_TOKEN))
                return new String(Base64.getUrlDecoder().decode(cookie.getValue().getBytes(StandardCharsets.UTF_8)));

        return "";
    }

    public static ResponseEntity<JSONObject> tokenAuthorization(User user, String role) {
        if(user == null)
        return new ResponseEntity<>(StatusHandler.E1002, HttpStatus.UNAUTHORIZED);

        if(!user.getAccountType().equals(role))
            return new ResponseEntity<>(StatusHandler.E1003, HttpStatus.UNAUTHORIZED);

        return null;
    }

}
