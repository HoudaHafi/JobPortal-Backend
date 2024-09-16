package org.sid.projetcv.controller;

import org.sid.projetcv.dto.EmailRequestDto;
import org.sid.projetcv.service.impl.EmailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin("*")
public class EmailController {
	
    private final EmailServiceImpl emailService;
    
    @PostMapping("/send-email")
    public EmailRequestDto sendEmail(@RequestBody EmailRequestDto emailRequest) {
        Long candidatId = emailRequest.getCandidatId();
        Long recruteurId = emailRequest.getRecruteurId();
        String date = emailRequest.getDate();
        String time = emailRequest.getTime();
        String link = emailRequest.getLink();
        
        emailService.sendEmailToCandidatAndRecruteur(candidatId, recruteurId, date, time, link);
        
        return emailRequest;
    }

}
