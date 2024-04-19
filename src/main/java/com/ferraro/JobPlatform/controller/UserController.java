package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.dto.AnnuncioDTOSimple;
import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.JobApplianceDTOSimple;
import com.ferraro.JobPlatform.dto.request.JobApplianceRequest;
import com.ferraro.JobPlatform.service.AnnuncioService;
import com.ferraro.JobPlatform.service.JobApplianceService;
import com.ferraro.JobPlatform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("${app.userAPI}")
public class UserController {
    @Autowired
    private JobApplianceService applianceService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnnuncioService annuncioService;

    @PostMapping(value = "/appliance/{annuncioId}", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<JobApplianceDTO> applyForJob(@RequestPart("data") @Valid @NonNull JobApplianceRequest applianceRequest,
                                                       @PathVariable("annuncioId") String annuncioId,
                                                       @NonNull @CookieValue(name = "token") String authorization,
                                                       @RequestPart("file") MultipartFile file) {
        return ResponseEntity
                .ok(applianceService.saveAppliance(applianceRequest, annuncioId, authorization, file));
    }

    @DeleteMapping("/appliance/{applianceId}")
    public ResponseEntity<String> deleteAppliance(@PathVariable("applianceId") String applianceId,
                                                  @NonNull @CookieValue(name = "token") String authorization) {
        if (!applianceService.deleteAppliance(applianceId, authorization)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Spiacente, si è verificato un problema con la rimozione della richiesta");
        }
        return ResponseEntity
                .ok("Richiesta rimossa correttamente!");
    }

    @GetMapping("/appliances/{page}")
    public ResponseEntity<Page<JobApplianceDTOSimple>> getAppliances(@PathVariable("page") int currentPage,
                                                                     @NonNull @CookieValue("token") String authorization) {
        int pageSize = 10;
        return ResponseEntity
                .ok(applianceService.findAppliancesByUser(authorization, currentPage, pageSize));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, @NonNull HttpServletRequest request) {
        //Impostando la durata a 0 sovrascrivo il cookie contenente il token e lo faccio scadere
        ResponseCookie cookie = ResponseCookie.from("token", null)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity
                .ok("Logout succesfully!");
    }


    @PostMapping("/favourites/{annuncioId}")
    public ResponseEntity<String> addFavouriteAnnuncio(@CookieValue("token") String authorization,
                                                       @PathVariable("annuncioId") String annuncioId) {
        if(!userService.addFavouriteAnnuncio(authorization, annuncioId)){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Spiacente, l'annuncio è già tra i tuoi preferiti!");
        }
        return ResponseEntity
                .ok("Annuncio aggiunto correttamente ai preferiti!");
    }

    @GetMapping("/favourites/{page}")
    public ResponseEntity<Page<AnnuncioDTOSimple>> findFavourites(@PathVariable("page") int page,

                                                                  @CookieValue("token") String authorization) {
        int pageSize = 10;
        return ResponseEntity
                .ok(annuncioService.findFavourites(authorization, page, pageSize));
    }

    @DeleteMapping("/favourites/{annuncioId}")
    public ResponseEntity<String> removeFavourite(@PathVariable("annuncioId")String annuncioId,
                                                  @CookieValue("token")String authorization){
        if(!userService.removeFavourite(authorization, annuncioId)){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Impoossibile rimuoverlo, l'annuncio non è tra i tuoi preferiti!");
        }
        return ResponseEntity
                .ok("Annuncio rimosso correttamente dai preferiti!");
    }

}
