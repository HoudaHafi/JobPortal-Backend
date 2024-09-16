package org.sid.projetcv.mapper;

import org.mapstruct.Mapper;
import org.sid.projetcv.dto.CanMinDTO;
import org.sid.projetcv.entity.Candidat;

@Mapper
public interface CandidatMapper {
	
    CanMinDTO mapToDto (Candidat c);
	
	Candidat mapToEntity (CanMinDTO dto);

}
