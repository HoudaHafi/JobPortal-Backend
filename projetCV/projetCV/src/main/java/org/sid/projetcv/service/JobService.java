package org.sid.projetcv.service;

import java.util.List;

import org.sid.projetcv.entity.Job;
import org.sid.projetcv.entity.JobApplication;
import org.sid.projetcv.enums.TypeJob;
import org.springframework.core.io.Resource;

public interface JobService {
	
	Job CreateJob(Job job);
	List<Job> ReadJob();
	Job getJobById(Long id);
	Job UpdateJob(Long id, Job job);
	String DeleteJob(Long id);
	Resource getRecLogo(Long id);
	List<Job> getJobsByRecruteurId(Long recruteurId);
	List<JobApplication> getApplicationsByJobId(Long jobId);
	List<Job> filterJobs(String location, String datepub, TypeJob type);
	List<Job> searchJobs(String title, String location);

}
