package dev.revington.util;

import dev.revington.entity.Token;
import dev.revington.entity.User;
import dev.revington.repo.TokenRepository;
import dev.revington.repo.UserRepository;
import dev.revington.security.Crypto;
import dev.revington.variables.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
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

    public static User validate(HttpServletRequest req, HttpServletResponse res, TokenRepository repo, UserRepository userRepo, String token) {
        List<Token> tokens = repo.findByToken(token);
        List<User> users = null;
        if(tokens.isEmpty() || new Timestamp(new Date().getTime()).compareTo(tokens.get(0).getExpires()) > 0 || (users = userRepo.findById(tokens.get(0).getId()).stream().toList()).isEmpty()) {
            CookieUtil.clearCookie(res, Parameter.COOKIE_TOKEN, CookieUtil.getDomain(req), "/", true, false);
            return null;
        }

         return users.get(0);
    }

}
