package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.request.JobApplianceRequest;
import com.ferraro.JobPlatform.service.JobApplianceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${app.userAPI}")
public class UserController {

    @Autowired
    private JobApplianceService applianceService;

    @PostMapping(value = "/appliance/{annuncioId}",consumes = {"multipart/form-data","application/json"})
    public ResponseEntity<JobApplianceDTO> applyForJob(@RequestPart("data") @Valid @NonNull JobApplianceRequest applianceRequest,
                                                       @PathVariable("annuncioId") String annuncioId,
                                                       @NonNull @RequestHeader("Authorization") String authorization,
                                                       @RequestPart("file")MultipartFile file) {
        System.out.println("SONO NEL METODO");
        return ResponseEntity.ok(applianceService.saveAppliance(applianceRequest, annuncioId, authorization, file));
    }

    @DeleteMapping("appliance/{applianceId}")
    public ResponseEntity<String> deleteAppliance(@PathVariable("applianceId") String applianceId,
                                                  @NonNull @RequestHeader("Authorization") String authorization) {
        if (!applianceService.deleteAppliance(applianceId, authorization)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Spiacente, si è verificato un problema con la rimozione della richiesta");
        }
        return ResponseEntity.ok("Richiesta rimossa correttamente!");
    }

}
