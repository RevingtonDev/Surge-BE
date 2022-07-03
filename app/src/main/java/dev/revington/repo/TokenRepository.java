package dev.revington.repo;

import dev.revington.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, Integer> {

    public Optional<Token> findByToken(String token);

}
