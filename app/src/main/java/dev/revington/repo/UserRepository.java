package dev.revington.repo;

import dev.revington.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Integer> {

    @Query(value =
        "{ $or : [" +
                "{ $and: [" +
                    "{" +
                        "$expr: {" +
                            "$regexMatch: {" +
                                "\"input\": {\"$toString\": \"$_id\"}, " +
                                "\"regex\": '?0'" +
                            "}" +
                        "}" +
                    "}," +
                    "{" +
                        "_id: { $not: { $eq: ?1 } }" +
                    "}" +
                "]},"+
                "{" +
                        "firstName: {$regex: /?0/i}" +
                "}," +
                "{" +
                        "lastName: {$regex: /?0/i}" +
                "}," +
                "{" +
                    "$and: [" +
                        "{ email: { $regex: /?0/i } }, " +
                        "{ email: { $not: { $eq: ?2 } } }" +
                    "]" +
                "}" +
            "]" +
        "}", fields = "{password: 0}")
    public Page<User> find(String param, int id, String email, Pageable pageable);

    @Query(fields = "{password: 0}")
    public Page<User> findById(int param, Pageable pageable);

    @Query(fields = "{password: 0}")
    public Page<User> findByFirstName(String param, Pageable pageable);

    @Query(fields = "{password: 0}")
    public Page<User> findByLastName(String param, Pageable pageable);

    @Query(fields = "{password: 0}")
    public Page<User> findByEmail(String param, Pageable pageable);

    public Optional<User> findByEmail(String param);

    @Query(fields = "{ password: 0 }")
    public Optional<User> findById(int param);

    @Query(value = "{}", sort = "{ '_id': -1 }")
    List<User> findAllByIdSortDescending();

    @Query(value = "{ _id: { $not: { $eq : ?0 } } }", fields = "{ password: 0 }")
    Page<User> findAll(int id, Pageable pageable);

    @Query(value = "{ accountType: \"student\" }", fields = "{ password: 0, accountType: 0 }")
    Page<User> findStudents(Pageable pageable);

    @Query(value =
            "{ $and: [ " +
                "{ " +
                    " _id: { $not: { $eq : ?0 } } " +
                "}," +
                "{ " +
                    "accountType: \"admin\"" +
                "} " +
            "]}", fields = "{ password: 0, accountType: 0 }")
    Page<User> findAdmins(int id, Pageable pageable);
}
