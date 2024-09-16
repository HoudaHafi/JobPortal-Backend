package org.sid.projetcv.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.sid.projetcv.entity.Job;
import org.sid.projetcv.entity.JobApplication;
import org.sid.projetcv.entity.Recruteur;
import org.sid.projetcv.enums.TypeJob;
import org.sid.projetcv.repository.JobApplicationRepository;
import org.sid.projetcv.repository.JobRepository;
import org.sid.projetcv.repository.RecruteurRepository;
import org.sid.projetcv.service.JobService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobServiceImpl implements JobService {
	
	private final JobRepository jobRepository;
	private final RecruteurRepository recruteurRepository;
	private final JobApplicationRepository jobAppRepository;

	@Override
	public Job CreateJob(Job job) {
		if (job.getRecruteur() == null) {
			throw new EntityNotFoundException("Recruteur is required");
		}
		Recruteur recruteur = recruteurRepository.findById(job.getRecruteur().getId())
				.orElseThrow(() -> new EntityNotFoundException("Recruteur not found with id: " + job.getRecruteur().getId()));

		job.setRecruteur(recruteur);
		var createdJob = jobRepository.save(job);

		return createdJob;
	}
	
	@Override
	public List<Job> getJobsByRecruteurId(Long recruteurId) {
        return jobRepository.findByRecruteurId(recruteurId);
    }
	
	@Override
	public List<JobApplication> getApplicationsByJobId(Long jobId) {
        return jobAppRepository.findByJobId(jobId);
    }

	@Override
	public List<Job> ReadJob() {
		return jobRepository.findAll();
	}
	
	@Override
	public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + id));
    }

	@Override
	public Job UpdateJob(Long id, Job job) {
		Job j = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + id));
        
		j.setTitle(job.getTitle());
		j.setDescription(job.getDescription());
		j.setRequirements(job.getRequirements());
		j.setType(job.getType());
		j.setContrat(job.getContrat());
		j.setSkills(job.getSkills());
		j.setLocation(job.getLocation());
		j.setAvantages(job.getAvantages());
		j.setNiveau(job.getNiveau());
		j.setDeadline(job.getDeadline());
		j.setDatepub(job.getDatepub());

        Recruteur recruteur = recruteurRepository.findById(job.getRecruteur().getId())
                .orElseThrow(() -> new EntityNotFoundException("Recruteur not found with id: " + job.getRecruteur().getId()));
        j.setRecruteur(recruteur);

        return jobRepository.save(j);
	}

	@Override
	public String DeleteJob(Long id) {
		jobRepository.deleteById(id);
		return "Job deleted !";
	}
	
	@Override
	public Resource getRecLogo(Long id) {
	    try {
	        Optional<Recruteur> recruteurOptional = recruteurRepository.findById(id);
	        
	        if (recruteurOptional.isPresent()) {
	            Recruteur recruteur = recruteurOptional.get();
	            
	            String logoUri = recruteur.getLogo();
	            
	            String relativeFilePath = logoUri.replace(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString(), "");

	            Path filePath = Paths.get("C:/images").resolve(relativeFilePath);

	            if (Files.exists(filePath)) {
	                Resource resource = new UrlResource(filePath.toUri());

	                if (resource.exists() || resource.isReadable()) {
	                    return resource;
	                } else {
	                    throw new RuntimeException("Could not read the file: " + relativeFilePath);
	                }
	            } else {
	                throw new RuntimeException("File not found: " + relativeFilePath);
	            }
	        } else {
	            throw new RuntimeException("Recruteur not found with id: " + id);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error retrieving recruteur logo", e);
	    }
	}
	
	
	@Override
    public List<Job> filterJobs(String location, String datepub, TypeJob type) {
        // Implement the filtering logic
        if (location != null && datepub != null && type != null) {
            return jobRepository.findByLocationAndDatepubAndType(location, datepub, type);
        } else if (location != null && datepub != null) {
            return jobRepository.findByLocationAndDatepub(location, datepub);
        } else if (location != null && type != null) {
            return jobRepository.findByLocationAndType(location, type);
        } else if (datepub != null && type != null) {
            return jobRepository.findByDatepubAndType(datepub, type);
        } else if (location != null) {
            return jobRepository.findByLocation(location);
        } else if (datepub != null) {
            return jobRepository.findByDatepub(datepub);
        } else if (type != null) {
            return jobRepository.findByType(type);
        } else {
            return jobRepository.findAll();
        }
    }
	
	@Override
	public List<Job> searchJobs(String title, String location) {
        return jobRepository.findByTitleAndLocation(title, location);
    }

}
