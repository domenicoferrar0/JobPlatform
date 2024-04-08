package com.ferraro.JobPlatform.repository;

import com.ferraro.JobPlatform.model.document.Annuncio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnuncioRepository extends MongoRepository<Annuncio, String> {


    Page<Annuncio> findAllByEmployerId(String id, Pageable pageable);

    @Query("{$or: [{'title': { $regex: ?0, $options: 'i' }}, " +
            "{'description': { $regex: ?0, $options: 'i' }}, " +
            "{'localita': { $regex: ?0, $options: 'i' }}, " +
            "{'modalita': ?0 }, " +
            "{'disponibilita': ?0 }, " +
            "{'country': ?0 }]}")
    Page<Annuncio> findAllByText(String searchTerm, Pageable pageable);
}
