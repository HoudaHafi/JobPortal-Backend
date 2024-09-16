package org.sid.projetcv.service.impl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.sid.projetcv.dto.JobAppDTO;
import org.sid.projetcv.dto.ScoreDTO;
import org.sid.projetcv.entity.Candidat;
import org.sid.projetcv.entity.Job;
import org.sid.projetcv.entity.JobApplication;
import org.sid.projetcv.enums.StatusEnum;
import org.sid.projetcv.repository.CandidatRepository;
import org.sid.projetcv.repository.JobApplicationRepository;
import org.sid.projetcv.repository.JobRepository;
import org.sid.projetcv.service.JobApplicationService;
import org.sid.projetcv.service.TextExtractorService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {
	
	
	private final JobApplicationRepository jobAppRepository;
	private final CandidatRepository candidatRepository;
	private final JobRepository jobRepository;
	private final TextExtractorService textExtractorService;

    @Override
    public List<ScoreDTO> calculateScore(Long jobId, Long candidateId) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        Optional<JobApplication> optionalJobApplication = jobAppRepository.findByJobIdAndCandidatId(jobId, candidateId);

        if (optionalJob.isEmpty() || optionalJobApplication.isEmpty()) {
            throw new IllegalArgumentException("Invalid job ID or candidate ID");
        }

        Job job = optionalJob.get();
        JobApplication jobApplication = optionalJobApplication.get();

        List<ScoreDTO> scores = new ArrayList<>();

        List<String> requirements = job.getRequirements();

        String cvText = jobApplication.getCv();

        
        for (String requirement : requirements) {
        	try {
                int occurrences = textExtractorService.countWordOccurrences(new File("C:/file/" + cvText), requirement); // Adapter le chemin si nécessaire
                double percentage = (double) occurrences / requirements.size() * 100;
                scores.add(new ScoreDTO(requirement, percentage));
            } catch (IOException e) {
                e.printStackTrace(); 
            }
        }

        return scores;
    }
    
	
	@Override
	public Optional<JobApplication> getJobApplication(Long candidatId, Long jobId) {
        return jobAppRepository.findByCandidatIdAndJobId(candidatId, jobId);
    }
	
	@Override
    public boolean hasCandidateAppliedForJob(Long candidateId, Long jobId) {
        return jobAppRepository.existsByCandidatIdAndJobId(candidateId, jobId);
    }
	
	
	@Override
	public JobApplication createJobApplication(JobAppDTO jobAppDTO, MultipartFile cvFile) {
        
		Optional<JobApplication> existingJobApp = jobAppRepository.findByCandidatIdAndJobId(jobAppDTO.getCandidatId(), jobAppDTO.getJobId());
        if (existingJobApp.isPresent()) {
            throw new IllegalArgumentException("Job application already exists for this candidat and job.");
        }
        
        Job job = jobRepository.findById(jobAppDTO.getJobId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));
        System.out.println(jobAppDTO.toString());
        Candidat candidat = candidatRepository.findById(jobAppDTO.getCandidatId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid candidat ID"));
        
        JobApplication jobApplication = new JobApplication();
        System.out.println("heloo");
        jobApplication.setTitle(candidat.getFullName() + " Applied to " + job.getTitle());
        jobApplication.setStatus(StatusEnum.PENDING);
        jobApplication.setDatecandidateur(new Date());
        jobApplication.setJob(job);
        jobApplication.setCandidat(candidat);
        jobApplication.setCv(this.uploadFile(cvFile));

        
        return jobAppRepository.save(jobApplication);
    }

	
	private String uploadFile(MultipartFile file)  {
	    try {
	        String uploadDir = "C:/file";
	        Date currentDate = new Date();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	        String formattedDate = dateFormat.format(currentDate);

	        Path uploadPath = Paths.get(uploadDir);
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }

	        String fileName = StringUtils.cleanPath(formattedDate + "_" + file.getOriginalFilename());
	        Path filePath = uploadPath.resolve(fileName);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        return fileName;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	@Override
    public JobApplication updateJobAppStatus(Long jobAppId, StatusEnum status) {
        JobApplication jobApplication = jobAppRepository.findById(jobAppId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job application ID"));

        jobApplication.setStatus(status);
        return jobAppRepository.save(jobApplication);
    }
	
	@Override
	public List<JobApplication> getApplicationsByCandidatId(Long candidatId) {
        return jobAppRepository.findByCandidatId(candidatId);
    }
	
	@Override
	public List<JobApplication> getApplicationsByRecruiterId(Long recruiterId) {
        return jobAppRepository.findByJobRecruteurId(recruiterId);
    }
	
	@Override
	public List<JobApplication> ReadJobApp() {
		return jobAppRepository.findAll();
	}
	
	@Override
	public Optional<JobApplication> getJobApplicationById(Long id) {
        return jobAppRepository.findById(id);
    }
	
	@Override
	public JobApplication getJobAppById(Long id) {
        return jobAppRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job Application not found with id: " + id));
    }

	@Override
	public JobApplication updateJobApp(Long id, String title, String status,
			String datecandidateur, MultipartFile cv) throws IOException {
	    JobApplication jobApplication = jobAppRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Job Application not found with id: " + id));

	    jobApplication.setTitle(title);
	    
	    if (cv != null) {
	        jobApplication.setCv(uploadFile(cv));
	    }

	    return jobAppRepository.save(jobApplication);
	}


	@Override
	public String DeleteJobApp(Long id) {
		jobAppRepository.deleteById(id);
		return "JobApp deleted !";
	}
	

}
