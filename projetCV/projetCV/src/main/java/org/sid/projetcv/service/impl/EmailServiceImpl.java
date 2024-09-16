package org.sid.projetcv.service.impl;

import org.sid.projetcv.entity.Candidat;
import org.sid.projetcv.entity.Recruteur;
import org.sid.projetcv.repository.CandidatRepository;
import org.sid.projetcv.repository.RecruteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {
	
	@Autowired
    private JavaMailSender mailSender;
	@Autowired
    private CandidatRepository candidatRepository;
    @Autowired
    private RecruteurRepository recruteurRepository;
    @Value("${spring.mail.username}")
    private String fromAddress;
    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddress);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailSender.send(mailMessage);
    }
    
    public void sendEmailToCandidatAndRecruteur(Long candidatId, Long recruteurId, String date, String time, String link) {
        Candidat candidat = this.candidatRepository.findById(candidatId).orElseThrow();
        Recruteur recruteur = this.recruteurRepository.findById(recruteurId).orElseThrow();
        
        String candidatEmail = candidat.getEmail();
        String recruteurEmail = recruteur.getEmail();
        
        String subject = "Meeting Invitation";
        
        
        String candidatBody = "We are pleased to inform you that you have been selected for the first phase of our recruitment process. Congratulations!\n\n" +
                "We would like to invite you to a virtual interview via Google Meet on " + date + " at " + time + ".\n\n" +
                "Here is the link to join the meeting : " + link + "\n\n" +
                "Please confirm your attendance for this interview by replying to this email.\n\n" +
                "Best regards";
        
        
        String recruteurBody = "We are pleased to inform you that a virtual interview has been scheduled for " + date + " at " + time + ".\n\n" +
                "Here is the link to join the meeting : " + link + "\n\n" +
                "Best regards";
        
        sendEmail(candidatEmail, subject, candidatBody);
        sendEmail(recruteurEmail, subject, recruteurBody);
    }

}
