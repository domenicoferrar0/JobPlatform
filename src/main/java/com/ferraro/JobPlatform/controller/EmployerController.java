package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.dto.AnnuncioDTO;
import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.JobApplianceDTOSimple;
import com.ferraro.JobPlatform.dto.request.AnnuncioRequest;
import com.ferraro.JobPlatform.service.AnnuncioService;
import com.ferraro.JobPlatform.service.JobApplianceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.employerAPI}")
public class EmployerController {

    @Autowired
    private AnnuncioService annuncioService;

    @Autowired
    private JobApplianceService applianceService;

    //TODO SISTEMARE API SU POSTMAN E IMPLEMENTARE SWAGGER

    @PostMapping("/annuncio")
    public ResponseEntity<AnnuncioDTO> saveAnnuncio(@NonNull @CookieValue(name = "token") String authorization,
                                                    @NonNull @Valid @RequestBody AnnuncioRequest requestBody) {
        return ResponseEntity.ok(annuncioService.saveAnnuncio(authorization, requestBody));
    }

    @DeleteMapping("/annuncio/{id}")
    public ResponseEntity<String> deleteAnnuncio(@NonNull @CookieValue(name = "token") String authorization,
                                                 @PathVariable("id") String idAnnuncio) {
        if (!annuncioService.deleteAnnuncio(idAnnuncio, authorization)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore, impossibile eliminare l'annuncio");
        }
        return ResponseEntity.ok("Annuncio rimosso correttamente!");
    }

    @PutMapping("/annuncio/{id}")
    public ResponseEntity<AnnuncioDTO> updateAnnuncio(@NonNull @CookieValue(name = "token") String authorization,
                                                      @PathVariable("id") String idAnnuncio,
                                                      @NonNull @Valid @RequestBody AnnuncioRequest requestBody){
        return ResponseEntity.ok(annuncioService.updateAnnuncio(idAnnuncio, authorization, requestBody));
    }

    @GetMapping("/annunci/{page}")
    public ResponseEntity<Page<AnnuncioDTO>>findAnnunciByEmployer(@NonNull @CookieValue(name = "token") String authorization,
                                                                   @PathVariable("page") int currentPage){
        int pageSize = 10;
        return ResponseEntity.ok(annuncioService.findAnnunciByEmployer(authorization, currentPage, pageSize));
    }

    @GetMapping("/annuncio/{id}/appliances/{page}")
    public ResponseEntity<Page<JobApplianceDTOSimple>> findAppliancesPreviw(@NonNull @CookieValue(name = "token") String authorization,
                                                                            @PathVariable("id") String idAnnuncio,
                                                                            @PathVariable("page") int page){
        int pageSize = 10;
        return ResponseEntity.ok(applianceService.findAllAppliancesByAnnuncio(authorization, idAnnuncio, page, pageSize));
    }

    @GetMapping("/appliance/{id}")
    public ResponseEntity<JobApplianceDTO> findApplianceById(@NonNull @CookieValue(name = "token") String authorization,
                                                             @PathVariable("id")String applianceId){
        return ResponseEntity.ok(applianceService.findApplianceById(authorization, applianceId));
    }


    @GetMapping("/appliance/{id}/file")
    public ResponseEntity<Resource> findFileByAppliance(@NonNull @CookieValue(name = "token") String authorization,
                                                        @PathVariable("id")String applianceId){
        return ResponseEntity.ok(applianceService.findFileByApplianceId(authorization, applianceId));

    }
}
