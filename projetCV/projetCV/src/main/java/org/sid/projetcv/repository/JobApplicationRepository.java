package org.sid.projetcv.repository;

import java.util.List;
import java.util.Optional;

import org.sid.projetcv.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>{
	Optional<JobApplication> findByCandidatIdAndJobId(Long candidatId, Long jobId);
	boolean existsByCandidatIdAndJobId(Long candidatId, Long jobId);
	List<JobApplication> findByCandidatId(Long candidatId);
	List<JobApplication> findByJobRecruteurId(Long recruiterId);
	List<JobApplication> findByJobId(Long jobId);
	Optional<JobApplication> findByJobIdAndCandidatId( Long jobId, Long candidateId);
}
