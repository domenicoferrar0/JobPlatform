package com.ferraro.JobPlatform.repository;

import com.ferraro.JobPlatform.model.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);

    boolean existsByCf(String cf);

    Optional<User> findByEmail(String email);

}
