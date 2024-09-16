package org.sid.projetcv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sid.projetcv.dto.*;
import org.sid.projetcv.service.impl.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping(value ="/condidate/register" , consumes = MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<?> registerCondidate(@RequestPart("avatar") MultipartFile avatar ,
                                                @RequestPart("request") String requestJson)  throws IOException{
        CandidatDTO request = new ObjectMapper().readValue(requestJson, CandidatDTO.class);
       return  ResponseEntity.ok(service.registerCondidate(request, avatar));

    }
    @PostMapping(value = "/recreteur/register", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerRecreteur(@RequestPart("logo") MultipartFile logo ,

                                               @RequestPart("request") String requestJson) throws IOException {

        RecruteurDTO request = new ObjectMapper().readValue(requestJson, RecruteurDTO.class);
        return  ResponseEntity.ok(service.registerRecruteur(request,  logo));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    
    @PostMapping("/send-reset-password-link")
    public Token sendResetPasswordLink(@RequestBody EmailRequest email) {
        
        return service.sendResetPasswordLink(email.getEmail());
    }
    
    @PostMapping("/reset-password")
    public ResetPasswordRequest resetPassword(@RequestBody ResetPasswordRequest request) {
        
        return service.resetPassword(request.getToken(), request.getNewPassword());
    }


}
