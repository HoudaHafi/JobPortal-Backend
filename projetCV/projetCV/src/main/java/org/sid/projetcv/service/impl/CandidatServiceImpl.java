package org.sid.projetcv.service.impl;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.sid.projetcv.entity.Candidat;
import org.sid.projetcv.repository.CandidatRepository;
import org.sid.projetcv.service.CandidatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CandidatServiceImpl implements CandidatService{
	
	@Autowired
	private CandidatRepository candidatRepository;
	
	
	@Override
	public Candidat findById(Long id) {
        return candidatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id " + id));
    }
	
	@Override
	public Candidat UpdateCan(Long id, Candidat can) {
		Candidat c = candidatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + id));
        
		c.setFirstName(can.getFirstName());
		c.setLastName(can.getLastName());
		c.setUserName(can.getUsername());
		c.setEmail(can.getEmail());
		c.setAdresse(can.getAdresse());
		c.setDateNaissance(can.getDateNaissance());
		c.setNbexperience(can.getNbexperience());
		c.setNiveau(can.getNiveau());
		c.setTelephone(can.getTelephone());
		

        return candidatRepository.save(c);
	}
	

	@Override
	public Resource getCanAvatar(Long id) {
	    try {
	        Optional<Candidat> candidatOptional = candidatRepository.findById(id);
	        
	        if (candidatOptional.isPresent()) {
	            Candidat candidat = candidatOptional.get();
	            
	            String logoUri = candidat.getAvatar();
	            
	            String relativeFilePath = logoUri.replace(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString(), "").replace("%20", " ");

	            Path filePath = Paths.get("C:/images").resolve(relativeFilePath);
	            
	            System.out.println("Chemin du fichier construit: " + filePath.toString());

	            if (Files.exists(filePath)) {
	                Resource resource = new UrlResource(filePath.toUri());

	                if (resource.exists() || resource.isReadable()) {
	                    return resource;
	                } else {
	                    throw new RuntimeException("Could not read the file: " + relativeFilePath);
	                }
	            } else {
	                throw new RuntimeException("File not found: " + relativeFilePath);
	            }
	        } else {
	            throw new RuntimeException("Candidat not found with id: " + id);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error retrieving candidat avatar", e);
	    }
	}
	
	


}
