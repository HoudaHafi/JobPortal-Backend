package org.sid.projetcv.mapper;

import org.mapstruct.Mapper;
import org.sid.projetcv.dto.RecMinDTO;
import org.sid.projetcv.entity.Recruteur;

@Mapper
public interface RecMinMapper {
	
	RecMinDTO mapToDto (Recruteur r);
	
	Recruteur mapToEntity (RecMinDTO dto);

}
