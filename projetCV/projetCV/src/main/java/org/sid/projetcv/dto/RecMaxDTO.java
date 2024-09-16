package org.sid.projetcv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class RecMaxDTO {
	
	private Long id;
	private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String societe;
    private String poste;
    private String secteur;
    private String idsociete;
    private String adrsociete;

}
