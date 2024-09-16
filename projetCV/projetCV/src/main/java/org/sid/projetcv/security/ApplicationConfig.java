package org.sid.projetcv.security;

import lombok.RequiredArgsConstructor;
import org.sid.projetcv.entity.Candidat;
import org.sid.projetcv.entity.Recruteur;
import org.sid.projetcv.repository.CandidatRepository;
import org.sid.projetcv.repository.RecruteurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final RecruteurRepository recruteurRepository;
    private final CandidatRepository candidatRepository;
    
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Recruteur recruiter = recruteurRepository.findByEmail(username).orElse(null);
            if (recruiter != null) {
                return  (UserDetails) recruiter;
            }

            Candidat candidate = candidatRepository.findByEmail(username).orElse(null);
            if (candidate != null) {
                return (UserDetails) candidate;
            }

            throw new UsernameNotFoundException("User not found with username: " + username);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}