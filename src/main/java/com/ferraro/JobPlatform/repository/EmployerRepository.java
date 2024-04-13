package com.ferraro.JobPlatform.repository;

import com.ferraro.JobPlatform.model.document.Employer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployerRepository extends MongoRepository<Employer, String> {

    boolean existsByEmail(String email);

    Optional<Employer> findByEmail(String email);
}
