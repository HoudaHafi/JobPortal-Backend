package org.sid.projetcv.entity;

import java.util.List;

import org.sid.projetcv.enums.Contrat;
import org.sid.projetcv.enums.Niveau;
import org.sid.projetcv.enums.TypeJob;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Job {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	private String title;
	private String description;
	private List<String> requirements;
	private String skills;
	private String location;
	private Niveau niveau;
	private String avantages;
	private String deadline;
	private String datepub;
    private TypeJob type;
    private Contrat contrat;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruteur_id", nullable = false)
    private Recruteur recruteur;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    
    private List<JobApplication> jobApplications;

}
