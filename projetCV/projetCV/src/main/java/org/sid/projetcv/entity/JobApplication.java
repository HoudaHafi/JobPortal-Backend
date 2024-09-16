package org.sid.projetcv.entity;


import java.util.Date;

import org.sid.projetcv.enums.StatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class JobApplication {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private StatusEnum status;
    private String cv;
    private Date datecandidateur;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id")
     
    private Job job;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "condidate_id")
    
     
    private Candidat candidat;

}
