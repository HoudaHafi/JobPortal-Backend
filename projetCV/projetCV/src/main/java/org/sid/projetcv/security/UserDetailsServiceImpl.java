package org.sid.projetcv.security;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sid.projetcv.entity.Candidat;
import org.sid.projetcv.entity.Recruteur;
import org.sid.projetcv.repository.CandidatRepository;
import org.sid.projetcv.repository.RecruteurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RecruteurRepository recruteurRepository;
    private final CandidatRepository candidatRepository;

        @Override
        @Transactional
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Candidat candidate = candidatRepository.findByEmail(email).get();
            if (candidate != null) {
                return   candidate;
            }

            Recruteur recruiter = recruteurRepository.findByEmail(email).get();
            if (recruiter != null) {
                return     recruiter;
            }

            throw new UsernameNotFoundException("User not found with email: " + email);
        }

}