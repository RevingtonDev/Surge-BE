package dev.revington.api;

import dev.revington.entity.Note;
import dev.revington.entity.User;
import dev.revington.repo.NoteRepository;
import dev.revington.repo.TokenRepository;
import dev.revington.repo.UserRepository;
import dev.revington.status.StatusHandler;
import dev.revington.util.AccessToken;
import dev.revington.variables.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class Student {

    private final String ROLE = Parameter.STUDENT;

    @Autowired
    TokenRepository accessRepo;

    @Autowired
    UserRepository repo;

    @Autowired
    NoteRepository noteRepo;

    @PostMapping("/authorize")
    public ResponseEntity<JSONObject> getInfo(HttpServletRequest req, HttpServletResponse res,
                                              @RequestParam(required = false, defaultValue = "false") boolean content) {
        User student = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(student, ROLE)) != null)
            return response;
        else {
            JSONObject result = (JSONObject) StatusHandler.S200.clone();
            if(content)
                result.put(Parameter.RESULTS, student);
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/note")
    public ResponseEntity<JSONObject> createNote(HttpServletRequest req, HttpServletResponse res, @RequestBody Note note) {
        User student = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(student, ROLE)) != null)
            return response;

        note.setStudentID(student.getId());
        note.setTime(new Date().getTime());

        noteRepo.save(note);

        return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
    }

    @DeleteMapping("/note")
    public ResponseEntity<JSONObject> removeNote(HttpServletRequest req, HttpServletResponse res, @RequestParam String id) {
        User student = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(student, ROLE)) != null)
            return response;

        Optional<Note> noteObject;
        if((noteObject = noteRepo.findById(new ObjectId(id).toString())).isPresent())
            noteRepo.delete(noteObject.get());

        return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
    }

    @PutMapping("/note/update")
    public ResponseEntity<JSONObject> updateNote(HttpServletRequest req, HttpServletResponse res,
                                                 @RequestBody Note note) {
        User student = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(student, ROLE)) != null)
            return response;

        note.setTime(new Date().getTime());
        note.setStudentID(student.getId());

        if(noteRepo.findById(note.getId()).isPresent())
            noteRepo.save(note);

        return new ResponseEntity<>(StatusHandler.S200, HttpStatus.OK);
    }

    @PostMapping("/notes")
    public ResponseEntity<JSONObject> getNotes(HttpServletRequest req, HttpServletResponse res,
                                               @RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "10") int limit,
                                               @RequestParam(required = false, defaultValue = "false") boolean oldest) {
        User student = AccessToken.validate(req, res, accessRepo, repo);

        ResponseEntity<JSONObject> response;
        if((response = AccessToken.tokenAuthorization(student, ROLE)) != null)
            return response;

        if (page > 0)
            page--;
        else
            page = 0;

        if(limit < 1)
            limit = 10;

        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        Page<Note> notes;
        if(oldest)
            notes = noteRepo.findByClientIdOrderDescending(student.getId(), pageable);
        else
            notes = noteRepo.findByClientId(student.getId(), pageable);

        JSONObject result = (JSONObject) StatusHandler.S200.clone();
        result.put(Parameter.PAGES, notes.getTotalPages());
        result.put(Parameter.ELEMENTS, notes.getTotalElements());
        result.put(Parameter.RESULTS, notes.getContent());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
