package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.dto.AnnuncioDTO;
import com.ferraro.JobPlatform.dto.request.AnnuncioRequest;
import com.ferraro.JobPlatform.service.AnnuncioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/annuncio")
    public ResponseEntity<AnnuncioDTO> saveAnnuncio(@NonNull @RequestHeader("Authorization") String authorization,
                                                    @NonNull @Valid @RequestBody AnnuncioRequest requestBody) {
        return ResponseEntity.ok(annuncioService.saveAnnuncio(authorization, requestBody));
    }

    @DeleteMapping("/annuncio/{id}")
    public ResponseEntity<String> deleteAnnuncio(@NonNull @RequestHeader("Authorization") String authorization,
                                                 @PathVariable("id") String idAnnuncio) {
        if (!annuncioService.deleteAnnuncio(idAnnuncio, authorization)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore, impossibile eliminare l'annuncio");
        }
        return ResponseEntity.ok("Annuncio rimosso correttamente!");
    }

    @PutMapping("/annuncio/{id}")
    public ResponseEntity<AnnuncioDTO> updateAnnuncio(@NonNull @RequestHeader("Authorization") String authorization,
                                                      @PathVariable("id") String idAnnuncio,
                                                      @NonNull @Valid @RequestBody AnnuncioRequest requestBody){
        return ResponseEntity.ok(annuncioService.updateAnnuncio(idAnnuncio, authorization, requestBody));
    }

    @GetMapping("/annunci/{page}")
    public ResponseEntity<Page<AnnuncioDTO>>findAnnunciByRecruiter(@NonNull @RequestHeader("Authorization") String authorization,
                                                                   @PathVariable("page") int currentPage){
        int pageSize = 10;
        return ResponseEntity.ok(annuncioService.findAnnunciByRecruiter(authorization, currentPage, pageSize));
    }
}
