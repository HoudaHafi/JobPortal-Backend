package org.sid.projetcv.repository;

import java.util.Optional;

import org.sid.projetcv.entity.Recruteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RecruteurRepository extends JpaRepository<Recruteur, Long>{
	
	Optional<Recruteur> findByEmail(String email);
	Optional<Recruteur> findByResetToken(String resetToken);

}
