package org.sid.projetcv.repository;

import java.util.List;

import org.sid.projetcv.entity.Job;
import org.sid.projetcv.enums.TypeJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface JobRepository extends JpaRepository<Job, Long> {
	List<Job> findByRecruteurId(Long recruteurId);
	List<Job> findByLocation(String location);
    List<Job> findByDatepub(String datepub);
    List<Job> findByType(TypeJob type);
    List<Job> findByLocationAndDatepub(String location, String datepub);
    List<Job> findByLocationAndType(String location, TypeJob type);
    List<Job> findByDatepubAndType(String datepub, TypeJob type);
    List<Job> findByLocationAndDatepubAndType(String location, String datepub, TypeJob type);
    @Query("SELECT j FROM Job j WHERE j.title LIKE %:title% AND j.location = :location")
    List<Job> findByTitleAndLocation(@Param("title") String title, @Param("location") String location);

}
