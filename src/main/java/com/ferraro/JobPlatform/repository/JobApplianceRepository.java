package com.ferraro.JobPlatform.repository;

import com.ferraro.JobPlatform.model.document.JobAppliance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplianceRepository extends MongoRepository<JobAppliance, String> {

    @Query("{'idAnnuncio': ?0}")
    List<JobAppliance> findAllByAnnuncioId(String annuncioId);

    @Query(value = "{$and: [{'idAnnuncio': ?0}, {'userId': ?1}]}", exists = true)
    boolean existsByIdAnnuncioAndUserId(String idAnnuncio, String userId);

    @Query("{'idAnnuncio': ?0}")
    Page<JobAppliance> findAllByAnnuncioPaginated(String idAnnuncio, Pageable pageable);
}
