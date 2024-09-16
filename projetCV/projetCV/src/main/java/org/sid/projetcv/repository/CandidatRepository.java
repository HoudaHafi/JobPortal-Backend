package org.sid.projetcv.repository;

import java.util.Optional;

import org.sid.projetcv.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CandidatRepository extends JpaRepository<Candidat, Long>{
	
	Optional<Candidat>  findByEmail(String email);
	Optional<Candidat> findByResetToken(String resetToken);
	

}
