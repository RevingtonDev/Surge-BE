package dev.revington.repo;

import dev.revington.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, Integer> {

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
        "}", fields = "{'firstName': 1, 'lastName': 1, 'dateOfBirth': 1, 'email': 1, 'mobile': 1}")
    public Page<User> find(String param, Pageable pageable);

    @Query(fields = "{'firstName': 1, 'lastName': 1, 'dateOfBirth': 1, 'email': 1, 'mobile': 1}")
    public Page<User> findById(int param, Pageable pageable);
    @Query(fields = "{'firstName': 1, 'lastName': 1, 'dateOfBirth': 1, 'email': 1, 'mobile': 1}")
    public Page<User> findByFirstName(String param, Pageable pageable);
    @Query(fields = "{'firstName': 1, 'lastName': 1, 'dateOfBirth': 1, 'email': 1, 'mobile': 1}")
    public Page<User> findByLastName(String param, Pageable pageable);
    @Query(fields = "{'firstName': 1, 'lastName': 1, 'dateOfBirth': 1, 'email': 1, 'mobile': 1}")
    public Page<User> findByEmail(String param, Pageable pageable);

    public List<User> findByEmail(String param);

    @Query(value = "{}", sort = "{ '_id': -1 }")
    List<User> findAllByIdSortDescending();
}
