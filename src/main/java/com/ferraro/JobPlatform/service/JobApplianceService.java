package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.repository.JobApplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobApplianceService {
    @Autowired
    private JobApplianceRepository applianceRepository;
}
