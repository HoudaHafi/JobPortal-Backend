package org.sid.projetcv.service.impl;


import lombok.RequiredArgsConstructor;
import org.sid.projetcv.dto.*;
import org.sid.projetcv.entity.Candidat;
import org.sid.projetcv.entity.Recruteur;
import org.sid.projetcv.repository.CandidatRepository;
import org.sid.projetcv.repository.RecruteurRepository;
import org.sid.projetcv.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RecruteurRepository recruteurRepository;
    private final CandidatRepository candidatRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromAddress;


    public Long registerRecruteur(RecruteurDTO request,MultipartFile logo) {

        var recretur = Recruteur.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .userName(request.getUserName())
                .poste(request.getPoste())
                .logo(this.uploadFile(logo))
                .password(passwordEncoder.encode(request.getPassword()))
                .societe(request.getSociete())
                .secteur(request.getSecteur())
                .idsociete(request.getIdsociete())
                .adrsociete(request.getAdrsociete()).build();

        return recruteurRepository.save(recretur).getId();
    }

    public Long registerCondidate(CandidatDTO request,MultipartFile file) {

        var recretur = Candidat.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .telephone(request.getTelephone())
                .niveau(request.getNiveau())
                .avatar(this.uploadFile(file))
                .nbexperience(request.getNbexperience())
                .dateNaissance(request.getDateNaissance())
                .adresse(request.getAdresse()).build();

        return candidatRepository.save(recretur).getId();
    }


    private String uploadFile(MultipartFile file)  {
        try {

            String uploadDir = "C:/images";
            Date currentDate = new Date();
            // Format the date to append to the filename
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formattedDate = dateFormat.format(currentDate);

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Get the filename of the uploaded file
            String fileName = StringUtils.cleanPath(formattedDate+file.getOriginalFilename());

            // Construct the path where the file will be saved
            Path filePath = uploadPath.resolve(fileName);

            // Copy the contents of the uploaded file to the specified path
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Construct the URI of the uploaded file
            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(fileName)
                    .toUriString();

            // Print the URI of the uploaded file
            System.out.println("File uploaded successfully. URI: " + fileUri);

            // Return the URI of the uploaded file
            return fileUri;
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return an internal server error response
            return null;
        }

}
    public LoginResponse authenticate(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String jwtToken ="";
        String role="";
        Long id=0L;
        var claims = new HashMap<String, Object>();
        if (auth.getPrincipal() instanceof Recruteur) {
            Recruteur recruteur = (Recruteur) auth.getPrincipal();
            claims.put("fullName", recruteur.getFullName());
             role="Recruteur";
             id=recruteur.getId();
            jwtToken  = jwtService.generateToken(claims, (UserDetails) auth.getPrincipal());
        } else if (auth.getPrincipal() instanceof Candidat) {
            Candidat condidate = (Candidat) auth.getPrincipal();
            claims.put("fullName", condidate.getFullName());
            role="Condidate";
            id=condidate.getId();
            jwtToken = jwtService.generateToken(claims, (UserDetails) auth.getPrincipal());
        }

        return LoginResponse.builder()
                .token(jwtToken)
                .id(id)
                .role(role)

                .build();
    }
    
    public Token sendResetPasswordLink(String email) {
    	System.out.println("Processing email: " + email);
        
    	Optional<Candidat> candidat = candidatRepository.findByEmail(email);
        Optional<Recruteur> recruteur = recruteurRepository.findByEmail(email);
        
        System.out.println("Candidat present: " + candidat.isPresent());
        System.out.println("Recruteur present: " + recruteur.isPresent());

        if (candidat.isPresent()) {
            Candidat user = candidat.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            candidatRepository.save(user);
            String resetLink = "http://localhost:4200/resetpass/" + token;
            System.out.println("test");
            sendEmail(email, resetLink);
            Token tokenresponse = new Token(token);
            return tokenresponse;
        } else if (recruteur.isPresent()) {
            Recruteur user = recruteur.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            recruteurRepository.save(user);
            String resetLink = "http://localhost:4200/resetpass/" + token;
            sendEmail(email, resetLink);
            Token tokenresponse = new Token(token);
            return tokenresponse;
        } else {
            throw new RuntimeException("Email not found");
        }
    }
    
    public ResetPasswordRequest resetPassword(String token, String newPassword) {
        
    	Optional<Candidat> candidat = candidatRepository.findByResetToken(token);
        Optional<Recruteur> recruteur = recruteurRepository.findByResetToken(token);

        if (candidat.isPresent()) {
            Candidat user = candidat.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            candidatRepository.save(user);
        } else if (recruteur.isPresent()) {
            Recruteur user = recruteur.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            recruteurRepository.save(user);
        } else {
            throw new RuntimeException("Invalid token");
        }

        return new ResetPasswordRequest(token, newPassword);
    }
    
    private void sendEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink);
        mailSender.send(message);
    }

}
