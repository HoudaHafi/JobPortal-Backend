package org.sid.projetcv.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.sid.projetcv.dto.JobDTO;
import org.sid.projetcv.entity.Job;

@Mapper(uses = {RecMinMapper.class})
public interface JobMapper {
	
	JobDTO mapToDto (Job j);
	
	List<JobDTO> mapToDto (List<Job> j);
	
	Job mapToEntity (JobDTO dto);

}
