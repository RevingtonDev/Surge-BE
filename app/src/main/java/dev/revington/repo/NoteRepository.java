package dev.revington.repo;

import dev.revington.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends MongoRepository<Note, String> {

    @Override
    @Query(value = "{_id: ObjectId(?0)}")
    Optional<Note> findById(String s);

}
