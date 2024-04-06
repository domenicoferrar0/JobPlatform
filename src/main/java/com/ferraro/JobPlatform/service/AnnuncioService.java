package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.repository.AnnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnuncioService {
    @Autowired
    private AnnuncioRepository annuncioRepository;


}
