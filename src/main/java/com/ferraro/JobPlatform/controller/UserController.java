package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.JobApplianceDTOSimple;
import com.ferraro.JobPlatform.dto.request.JobApplianceRequest;
import com.ferraro.JobPlatform.service.JobApplianceService;
import jakarta.servlet.http.Cookie;
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
                                                                     @NonNull @CookieValue("token") String authorization){
        int pageSize = 10;
        return ResponseEntity
                .ok(applianceService.findAppliancesByUser(authorization, currentPage, pageSize));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, @NonNull HttpServletRequest request) {
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

}
