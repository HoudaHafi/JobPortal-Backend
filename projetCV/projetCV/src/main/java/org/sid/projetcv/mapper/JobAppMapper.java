package org.sid.projetcv.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.sid.projetcv.dto.JobAppMaxDTO;
import org.sid.projetcv.entity.JobApplication;

@Mapper(uses = {JobMapper.class, CandidatMapper.class})
public interface JobAppMapper {
	
	
	JobAppMaxDTO jobApplicationToDTO(JobApplication jobApplication);
	List<JobAppMaxDTO> mapToDto (List<JobApplication> jobApplication);
	
    JobApplication dtoToJobApplication(JobAppMaxDTO jobAppDTO);

}
