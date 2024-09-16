package org.sid.projetcv.service.impl;

import org.sid.projetcv.entity.Recruteur;
import org.sid.projetcv.repository.RecruteurRepository;
import org.sid.projetcv.service.RecruteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;


@Service
public class RecruteurServiceImpl implements RecruteurService{
	
	@Autowired
	private RecruteurRepository recruteurRepository;
	
	@Override
	public Recruteur findById(Long id) {
        return recruteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recruteur not found with id " + id));
    }
	
	@Override
	public Recruteur UpdateRec(Long id, Recruteur rec) {
		Recruteur r = recruteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recruteur not found with id: " + id));
        
		r.setFirstName(rec.getFirstName());
		r.setLastName(rec.getLastName());
		r.setUserName(rec.getUsername());
		r.setEmail(rec.getEmail());
		r.setSociete(rec.getSociete());
		r.setAdrsociete(rec.getAdrsociete());
		r.setPoste(rec.getPoste());
		r.setSecteur(rec.getSecteur());
		r.setIdsociete(rec.getIdsociete());
		

        return recruteurRepository.save(r);
	}
	
	
}

  
