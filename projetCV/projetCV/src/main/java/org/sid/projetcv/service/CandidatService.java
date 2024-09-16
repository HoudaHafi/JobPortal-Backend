package org.sid.projetcv.service;


import org.sid.projetcv.entity.Candidat;
import org.springframework.core.io.Resource;

public interface CandidatService {
	
	Candidat findById(Long id);
	Resource getCanAvatar(Long id);
	Candidat UpdateCan(Long id, Candidat can);

}
