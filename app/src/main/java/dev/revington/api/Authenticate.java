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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/auth")
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:3000/"})
public class Authenticate {

    @Autowired
    UserRepository repo;

    @Autowired
    TokenRepository accessRepo;

    @PostMapping("/authorize")
    public ResponseEntity<JSONObject> getInfo(HttpServletRequest req, HttpServletResponse res,
                                              @RequestParam(required = false, defaultValue = "false") boolean content) {
        User admin = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(admin, Parameter.UNIFIED)) != null)
            return response;
        else {
            JSONObject result = (JSONObject) StatusHandler.S200.clone();
            if(content)
                result.put(Parameter.RESULTS, admin);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    // API for user authentication
    @PostMapping(value = "/credentials", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> authenticate(HttpServletRequest req, HttpServletResponse res, @RequestBody User user, @RequestParam(required = false, defaultValue = "no") String memorize) {
        Optional<User> client = repo.findByEmail(user.getEmail());

        if(!client.isPresent())
            return new ResponseEntity<>(StatusHandler.E401, HttpStatus.UNAUTHORIZED);

        User owner = client.get();
        String encryptedPass = Crypto.getMD5(user.getPassword());
        if(owner.getPassword().equals(encryptedPass)) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            Token token;
            try {
                token = AccessToken.generateToken(owner, timestamp);
            } catch (NoSuchAlgorithmException e) {
                LoggerFactory.getLogger(Authenticate.class.getName()).error(e.getMessage());
                return new ResponseEntity<>(StatusHandler.E500, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            JSONObject result =  (JSONObject) StatusHandler.S200.clone();
            if(owner.isTemporary())
                result.put(Parameter.TEMPORARY_TYPE, true);

            accessRepo.deleteById(token.getId());
            accessRepo.save(token);
            CookieUtil.createCookie(res, Parameter.COOKIE_TOKEN, token.getToken(), "/", CookieUtil.getDomain(req), true, false, (memorize.equals("yes") && !owner.isTemporary() ? 365 * 24 * 60 * 60 : -1));

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else
            return new ResponseEntity<>(StatusHandler.E401, HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update")
    public ResponseEntity<JSONObject> updateAccount(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) {
        User client = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if ((response = AccessToken.tokenAuthorization(user, Parameter.UNIFIED)) != null)
            return response;

        Optional<User> prev = repo.findById(client.getId());
        if(prev.isEmpty() || !prev.get().isTemporary())
            return new ResponseEntity<>(StatusHandler.S200, HttpStatus.UNAUTHORIZED);

        user.setTemporary(false);
        repo.save(user);

        accessRepo.deleteById(user.getId());
        CookieUtil.clearCookie(res, Parameter.COOKIE_TOKEN, CookieUtil.getDomain(req), "/", true, false);

        return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
    }

    @DeleteMapping("/credentials")
    public ResponseEntity<JSONObject> logOut(HttpServletRequest req, HttpServletResponse res) {
        User user = AccessToken.validate(req, res, accessRepo, repo);

        if(user != null)
            accessRepo.deleteById(user.getId());

        CookieUtil.clearCookie(res, Parameter.COOKIE_TOKEN, CookieUtil.getDomain(req), "/", true, false);

        return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
    }

}
