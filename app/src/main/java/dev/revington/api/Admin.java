package dev.revington.api;

import dev.revington.Application;
import dev.revington.entity.User;
import dev.revington.repo.TokenRepository;
import dev.revington.repo.UserRepository;
import dev.revington.security.Crypto;
import dev.revington.status.StatusHandler;
import dev.revington.util.AccessToken;
import dev.revington.variables.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:3000"})
public class Admin {

    private final String ROLE = Parameter.ADMIN;

    @Autowired
    TokenRepository accessRepo;

    @Autowired
    UserRepository repo;

    @PostMapping("/authorize")
    public ResponseEntity<JSONObject> getInfo(HttpServletRequest req, HttpServletResponse res,
                                              @RequestParam(required = false, defaultValue = "false") boolean content) {
        User admin = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(admin, ROLE)) != null)
            return response;
        else {
            JSONObject result = (JSONObject) StatusHandler.S200.clone();
            if(content)
                result.put(Parameter.RESULTS, admin);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PutMapping("/create")
    public ResponseEntity<JSONObject> createAccount(HttpServletRequest req, HttpServletResponse res, @RequestBody JSONObject user) {
        User admin = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(admin, ROLE)) != null)
            return response;

        if(!repo.findByEmail(user.getAsString(Parameter.EMAIL)).isEmpty())
            return new ResponseEntity<>(StatusHandler.E1004, HttpStatus.NOT_ACCEPTABLE);

        User rookie = new User(user.getAsString(Parameter.EMAIL), Crypto.getMD5(user.getAsString(Parameter.PASSWORD)));
        rookie.setId(Application.ID++);
        rookie.setAccountType(user.getAsString(Parameter.ACCOUNT_TYPE));
        rookie.setTemporary(true);
        repo.save(rookie);
        return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<JSONObject> getUsers(HttpServletRequest req, HttpServletResponse res,
                                               @RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = ".") String type,
                                               @RequestParam(required = false, defaultValue = "10") int limit) {
        User admin = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(admin, ROLE)) != null)
            return response;

        if (page > 0)
            page--;
        else
            page = 0;

        if(limit < 1)
            limit = 10;

        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        Page<User> users;
        if (type.equals(Parameter.STUDENT))
            users = repo.findStudents(pageable);
        else if(type.equals(Parameter.ADMIN))
            users = repo.findAdmins(admin.getId(), pageable);
        else
            users = repo.findAll(admin.getId(), pageable);

        JSONObject result = (JSONObject) StatusHandler.S200.clone();
        result.put(Parameter.ELEMENTS, users.getTotalElements());
        result.put(Parameter.PAGES, users.getTotalPages());
        result.put(Parameter.RESULTS, users.getContent());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<JSONObject> searchUser(HttpServletRequest req, HttpServletResponse res,
                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "10") int limit,
                                                 @RequestParam String query) {
        User admin = AccessToken.validate(req, res, accessRepo, repo);

        if (page > 0)
            page--;
        else
            page = 0;

        if(limit < 1)
            limit = 10;

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(admin, ROLE)) != null)
            return response;

        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        Page<User> users = repo.find(query, admin.getId(), admin.getEmail(), pageable);

        JSONObject result = (JSONObject) StatusHandler.S200.clone();
        result.put(Parameter.PAGES, users.getTotalPages());
        result.put(Parameter.ELEMENTS, users.getTotalElements());
        result.put(Parameter.RESULTS, users.getContent());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
