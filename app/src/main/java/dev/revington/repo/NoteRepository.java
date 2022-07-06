package dev.revington.repo;

import dev.revington.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface NoteRepository extends MongoRepository<Note, String> {

    @Query(value = "{studentId: ?0}", sort = "{time: 1}")
    public Page<Note> findByClientId(int clientId, Pageable pageable);

    @Query(value = "{studentId: ?0}", sort = "{time: -1}")
    public Page<Note> findByClientIdOrderDescending(int clientId, Pageable pageable);

    @Override
    @Query(value = "{_id: ObjectId(?0)}")
    Optional<Note> findById(String s);

}
