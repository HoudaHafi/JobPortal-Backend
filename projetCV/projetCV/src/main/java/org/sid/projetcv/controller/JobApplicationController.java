package org.sid.projetcv.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.sid.projetcv.dto.JobAppDTO;
import org.sid.projetcv.dto.JobAppMaxDTO;
import org.sid.projetcv.dto.ScoreDTO;
import org.sid.projetcv.entity.JobApplication;
import org.sid.projetcv.enums.StatusEnum;
import org.sid.projetcv.mapper.JobAppMapper;
import org.sid.projetcv.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/jobApplication")
@CrossOrigin("*")
public class JobApplicationController {
	
	@Autowired
    private JobApplicationService jobAppService;
	@Autowired
	private JobAppMapper jobAppMapper;
	
	
	@GetMapping("/calculateScore")
    public ResponseEntity<List<ScoreDTO>> calculateScore(
            @RequestParam Long jobId,
            @RequestParam Long candidateId) {

        try {
            List<ScoreDTO> scores = jobAppService.calculateScore(jobId, candidateId);
            return ResponseEntity.ok(scores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@GetMapping("/status")
    public Optional<JobApplication> getJobApplicationStatus(@RequestParam Long candidatId, @RequestParam Long jobId) {
        return jobAppService.getJobApplication(candidatId, jobId);
    }
	
	@GetMapping("/check")
    public ResponseEntity<Boolean> hasCandidateAppliedForJob(@RequestParam Long candidateId, @RequestParam Long jobId) {
        boolean hasApplied = jobAppService.hasCandidateAppliedForJob(candidateId, jobId);
        return ResponseEntity.ok(hasApplied);
    }
	
	
	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createJobApplication(
        @RequestPart("jobAppDTO") String requestJson,
        @RequestPart("cv") MultipartFile cvFile) {
        
		try {
            System.out.println("helooo");
            ObjectMapper objectMapper = new ObjectMapper();
            JobAppDTO jobAppDTO = objectMapper.readValue(requestJson, JobAppDTO.class);
            System.out.println(jobAppDTO.toString());
           JobApplication createdJobApplication = jobAppService.createJobApplication(jobAppDTO, cvFile);
            return ResponseEntity.ok(createdJobApplication.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
	
	@PutMapping(value = "/{id}/status")
    public ResponseEntity<JobAppMaxDTO> updateJobApplicationStatus(
            @PathVariable Long id, @RequestParam StatusEnum status) {
        try {
            JobApplication updatedJobApplication = jobAppService.updateJobAppStatus(id, status);
            JobAppMaxDTO jobAppDTO = jobAppMapper.jobApplicationToDTO(updatedJobApplication);
            return ResponseEntity.ok(jobAppDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
	
	@GetMapping("/byCandidat/{candidatId}")
    public ResponseEntity<List<JobAppMaxDTO>> getApplicationsByCandidatId(@PathVariable Long candidatId) {
		List<JobApplication> jobApps = jobAppService.getApplicationsByCandidatId(candidatId);
		List<JobAppMaxDTO> jobAppDTOs = jobAppMapper.mapToDto(jobApps);
		return new ResponseEntity<>(jobAppDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/byRecruteur/{recruteurId}")
	public ResponseEntity<List<JobAppMaxDTO>> getApplicationsByRecruteurId(@PathVariable Long recruteurId) {
	    List<JobApplication> jobApps = jobAppService.getApplicationsByRecruiterId(recruteurId);
	    List<JobAppMaxDTO> jobAppDTOs = jobAppMapper.mapToDto(jobApps);
	    return new ResponseEntity<>(jobAppDTOs, HttpStatus.OK);
	}
	
	
	@GetMapping("/read")
	public List<JobApplication> readJobApplication(){
		return jobAppService.ReadJobApp();
	}
	
	@GetMapping("/read/{id}")
    public ResponseEntity<JobApplication> getJobApplicationById(@PathVariable("id") Long id) {
        JobApplication jobApplication = jobAppService.getJobAppById(id);
        return ResponseEntity.ok().body(jobApplication);
    }
	
	@PutMapping("/update/{id}")
	public ResponseEntity<JobApplication> updateJobApplication(@PathVariable("id") Long id,
            @RequestParam("title") String title,@RequestParam("status") String status,
            @RequestParam("datecandidateur") String datecandidateur,
            @RequestParam(value = "cv") MultipartFile cv) throws IOException {
        return new ResponseEntity<>(jobAppService.updateJobApp(id, title, status,datecandidateur, cv), HttpStatus.OK);
}
	
	@DeleteMapping("/delete/{id}")
	public String deleteJobApplication(@PathVariable Long id) {
		return jobAppService.DeleteJobApp(id);
	}

	
	@GetMapping("/downloadCv/{jobApplicationId}")
	public ResponseEntity<Resource> downloadCv(@PathVariable Long jobApplicationId) {
	    JobApplication jobApplication = jobAppService.getJobApplicationById(jobApplicationId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job application not found"));

	    String fileName = jobApplication.getCv();
	    if (fileName == null || fileName.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not found for this job application");
	    }

	    String uploadDir = "C:/file"; // Assurez-vous que ce chemin est le même que celui utilisé dans uploadFile
	    Path filePath = Paths.get(uploadDir).resolve(fileName);
	    Resource resource;
	    try {
	        resource = new UrlResource(filePath.toUri());
	        if (!resource.exists() || !resource.isReadable()) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CV file not found or not readable");
	        }
	    } catch (MalformedURLException e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading CV file", e);
	    }

	    String contentType;
	    try {
	        contentType = Files.probeContentType(filePath);
	    } catch (IOException e) {
	        contentType = "application/octet-stream";
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	            .body(resource);
	}
}


