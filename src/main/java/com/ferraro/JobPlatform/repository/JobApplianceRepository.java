package com.ferraro.JobPlatform.repository;

import com.ferraro.JobPlatform.model.document.JobAppliance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplianceRepository extends MongoRepository<JobAppliance, String> {

    @Query("{'idAnnuncio': ?1}")
    List<JobAppliance> findAllByAnnuncioId(String annuncioId);
}
