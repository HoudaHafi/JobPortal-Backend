package org.sid.projetcv.service;

import org.sid.projetcv.entity.Recruteur;

public interface RecruteurService {
	
	Recruteur findById(Long id);
	Recruteur UpdateRec(Long id, Recruteur rec);
	
	
}
