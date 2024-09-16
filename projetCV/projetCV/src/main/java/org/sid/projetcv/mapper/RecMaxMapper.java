package org.sid.projetcv.mapper;

import org.mapstruct.Mapper;
import org.sid.projetcv.dto.RecMaxDTO;
import org.sid.projetcv.entity.Recruteur;

@Mapper
public interface RecMaxMapper {
    
	RecMaxDTO mapToDto (Recruteur r);
	
	Recruteur mapToEntity (RecMaxDTO dto);
}
