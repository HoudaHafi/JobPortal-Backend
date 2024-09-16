package org.sid.projetcv.dto;

import java.util.List;

import org.sid.projetcv.enums.Contrat;
import org.sid.projetcv.enums.Niveau;
import org.sid.projetcv.enums.TypeJob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class JobDTO {
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
    private RecMinDTO recruteur;

}
