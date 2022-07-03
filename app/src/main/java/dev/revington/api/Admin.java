package dev.revington.api;

import dev.revington.Application;
import dev.revington.entity.User;
import dev.revington.repo.TokenRepository;
import dev.revington.repo.UserRepository;
import dev.revington.security.Crypto;
import dev.revington.status.StatusHandler;
import dev.revington.util.AccessToken;
import dev.revington.util.Roles;
import dev.revington.variables.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/admin")
public class Admin {

    private final String ROLE = Roles.ADMIN.label;

    @Autowired
    TokenRepository accessRepo;

    @Autowired
    UserRepository repo;

    @PutMapping("/create")
    public ResponseEntity<JSONObject> createAccount(HttpServletRequest req, HttpServletResponse res, @RequestBody JSONObject user) {
        User admin = AccessToken.validate(req, res, accessRepo, repo);
        if(admin == null)
            return new ResponseEntity<>(StatusHandler.E1002, HttpStatus.UNAUTHORIZED);

        if(!admin.getAccountType().equals(ROLE))
            return new ResponseEntity<>(StatusHandler.E1003, HttpStatus.UNAUTHORIZED);

        if(!repo.findByEmail(user.getAsString(Parameter.EMAIL)).isEmpty())
            return new ResponseEntity<>(StatusHandler.E1004, HttpStatus.NOT_ACCEPTABLE);

        User rookie = new User(user.getAsString(Parameter.EMAIL), Crypto.getMD5(user.getAsString(Parameter.PASSWORD)));
        rookie.set_id(Application.ID++);
        rookie.setAccountType(user.getAsString(Parameter.ACCOUNT_TYPE));
        rookie.setTemporary(true);
        repo.save(rookie);
        return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
    }

}
