package com.ferraro.JobPlatform.repository;

import com.ferraro.JobPlatform.model.document.JobAppliance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplianceRepository extends MongoRepository<JobAppliance, String> {
}
