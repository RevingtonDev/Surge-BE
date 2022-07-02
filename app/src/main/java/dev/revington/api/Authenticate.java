package dev.revington.api;

import dev.revington.entity.Token;
import dev.revington.entity.User;
import dev.revington.repo.TokenRepository;
import dev.revington.repo.UserRepository;
import dev.revington.security.Crypto;
import dev.revington.status.StatusHandler;
import dev.revington.util.AccessToken;
import dev.revington.util.CookieUtil;
import dev.revington.variables.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/auth")
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:3000"})
public class Authenticate {

    @Autowired
    UserRepository repo;

    @Autowired
    TokenRepository accessRepo;

    // API for user authentication
    @PostMapping(value = "/cred", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> authenticate(HttpServletRequest req, HttpServletResponse res, @RequestBody User user, @RequestParam(required = false, defaultValue = "no") String memorize) {
        List<User> list = repo.findByEmail(user.getEmail());

        if(list.isEmpty())
            return new ResponseEntity<>(StatusHandler.E401, HttpStatus.UNAUTHORIZED);

        User owner = list.get(0);
        if(owner.getPassword().equals(Crypto.getMD5(user.getPassword()))) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            Token token;
            try {
                token = AccessToken.generateToken(owner, timestamp);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return new ResponseEntity<>(StatusHandler.E500, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            accessRepo.deleteById(token.getId());
            accessRepo.save(token);
            CookieUtil.createCookie(res, Parameter.COOKIE_TOKEN, token.getToken(), "/", CookieUtil.getDomain(req), true, false, (memorize.equals("yes") ? 365 * 24 * 60 * 60 : -1));

            return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
        }

        return new ResponseEntity<>(StatusHandler.E500, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
