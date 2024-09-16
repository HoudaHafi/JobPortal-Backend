package org.sid.projetcv.controller;

import java.util.List;

import org.sid.projetcv.dto.JobAppMaxDTO;
import org.sid.projetcv.dto.JobDTO;
import org.sid.projetcv.entity.Job;
import org.sid.projetcv.entity.JobApplication;
import org.sid.projetcv.enums.TypeJob;
import org.sid.projetcv.mapper.JobAppMapper;
import org.sid.projetcv.mapper.JobMapper;
import org.sid.projetcv.service.JobService;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/job")
@AllArgsConstructor
@CrossOrigin("*")
public class JobController {
	
	private final JobService jobService;
	private final JobMapper jobMapper;
    private final JobAppMapper jobAppMapper;
	
	@PostMapping("/create")
    public ResponseEntity<JobDTO> CreateJob (@RequestBody JobDTO dto){
		Job job = jobMapper.mapToEntity(dto) ;
		JobDTO dtos = jobMapper.mapToDto(jobService.CreateJob(job));
		return new ResponseEntity<JobDTO> ( dtos , HttpStatus.OK);
	}
	
	@GetMapping("/recruteur/{recruteurId}")
	public ResponseEntity<List<JobDTO>> getJobsByRecruteurId(@PathVariable Long recruteurId) {
		List<Job> jobs = jobService.getJobsByRecruteurId(recruteurId);
		List<JobDTO> jobDTOs = jobMapper.mapToDto(jobs);
		return new ResponseEntity<>(jobDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/applications/byJob/{jobId}")
	public ResponseEntity<List<JobAppMaxDTO>> getApplicationsByJobId(@PathVariable Long jobId) {
        List<JobApplication> jobApplications = jobService.getApplicationsByJobId(jobId);
        List<JobAppMaxDTO> jobAppDTOs = jobAppMapper.mapToDto(jobApplications);
        return new ResponseEntity<>(jobAppDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/read")
    public ResponseEntity<List<JobDTO>> findAll() {
		List<JobDTO> dtos = jobMapper.mapToDto(jobService.ReadJob());
		return new ResponseEntity<List<JobDTO>>( dtos , HttpStatus.OK )  ;
	}
		
	@GetMapping("/read/{id}")
	public ResponseEntity<JobDTO> getJobById(@PathVariable() Long id) {
		JobDTO dto = jobMapper.mapToDto(jobService.getJobById(id));
		return ResponseEntity.ok(dto) ;
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<JobDTO> update(@PathVariable Long id, @RequestBody JobDTO dto) {
        Job job = jobMapper.mapToEntity(dto);
        JobDTO updatedDto = jobMapper.mapToDto(jobService.UpdateJob(id, job));
        return new ResponseEntity<JobDTO>(updatedDto, HttpStatus.OK);
    }

	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
	    jobService.DeleteJob(id);
	    return ResponseEntity.ok("{\"message\":\"Job deleted !\"}");
	}
	
	@GetMapping("/{id}/logo")
    public ResponseEntity<Resource> getRecruteurLogo(@PathVariable Long id) {
        try {
            Resource logo = jobService.getRecLogo(id);

            // Determine the content type based on the file's extension
            String contentType = "application/octet-stream"; // Default content type
            if (logo.getFilename().endsWith(".png")) {
                contentType = "image/png";
            } else if (logo.getFilename().endsWith(".jpg") || logo.getFilename().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (logo.getFilename().endsWith(".gif")) {
                contentType = "image/gif";
            }

            // Create the response entity
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + logo.getFilename() + "\"")
                    .body(logo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@GetMapping("/filter")
    public ResponseEntity<List<JobDTO>> filterJobs(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String datepub,
        @RequestParam(required = false) TypeJob type) {
        
        List<Job> jobs = jobService.filterJobs(location, datepub, type);
        List<JobDTO> jobDTOs = jobMapper.mapToDto(jobs);
        return new ResponseEntity<>(jobDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/search")
    public ResponseEntity<List<JobDTO>> searchJobs(
            @RequestParam String title,
            @RequestParam String location) {
        List<Job> jobs = jobService.searchJobs(title, location);
        List<JobDTO> jobDTOs = jobMapper.mapToDto(jobs);
        return new ResponseEntity<>(jobDTOs, HttpStatus.OK);
    }

}
