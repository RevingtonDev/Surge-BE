package dev.revington.repo;

import dev.revington.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TokenRepository extends MongoRepository<Token, Integer> {

    public List<Token> findByToken(String token);

}
