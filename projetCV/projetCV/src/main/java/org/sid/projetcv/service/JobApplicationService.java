package org.sid.projetcv.service;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.sid.projetcv.dto.JobAppDTO;
import org.sid.projetcv.entity.JobApplication;
import org.sid.projetcv.enums.StatusEnum;
import org.sid.projetcv.dto.ScoreDTO;
import org.springframework.web.multipart.MultipartFile;

public interface JobApplicationService {
	
	Optional<JobApplication> getJobApplication(Long candidatId, Long jobId);
	boolean hasCandidateAppliedForJob(Long candidateId, Long jobId);
	JobApplication createJobApplication(JobAppDTO jobAppDTO, MultipartFile cvFile);
	JobApplication updateJobAppStatus(Long jobAppId, StatusEnum status);
	List<JobApplication> getApplicationsByCandidatId(Long candidatId);
	List<JobApplication> getApplicationsByRecruiterId(Long recruiterId);
	List<JobApplication> ReadJobApp();
	JobApplication getJobAppById(Long id);
	JobApplication updateJobApp(Long id, String title, String status,
			String datecandidateur, MultipartFile cv) throws IOException;
	String DeleteJobApp(Long id);
	Optional<JobApplication> getJobApplicationById(Long id);
	List<ScoreDTO> calculateScore(Long jobId, Long candidateId);
	

}
