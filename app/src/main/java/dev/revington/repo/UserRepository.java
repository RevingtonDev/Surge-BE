package dev.revington.repo;

import dev.revington.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value =
        "{ $or : [" +
                "{" +
                    "$expr: {" +
                        "$regexMatch: {" +
                            "\"input\": {\"$toString\": \"$_id\"}," +
                            "\"regex\": '?0'" +
                        "}" +
                    "}" +
                "}," +
                "{" +
                        "firstName: {$regex: /?0/i}" +
                "}," +
                "{" +
                        "lastName: {$regex: /?0/i}" +
                "}," +
                "{" +
                    "email: {$regex: '?0'}" +
                "}" +
            "]" +
        "}")
    public Page<User> find(String param, Pageable pageable);

    public Page<User> findById(String param, Pageable pageable);
    public Page<User> findByFirstName(String param, Pageable pageable);
    public Page<User> findByLastName(String param, Pageable pageable);
    public Page<User> findByEmail(String param, Pageable pageable);

}
