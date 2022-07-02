package dev.revington.repo;

import dev.revington.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, Integer> {

    @Query(value = "{'_id': '0'}")
    public List<Note> findById(int id);

}
