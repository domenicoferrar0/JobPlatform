package com.ferraro.JobPlatform.repository;

import com.ferraro.JobPlatform.model.document.Annuncio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnuncioRepository extends MongoRepository<Annuncio, String> {
}
