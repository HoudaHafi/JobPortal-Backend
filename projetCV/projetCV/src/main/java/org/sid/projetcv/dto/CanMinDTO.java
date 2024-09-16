package org.sid.projetcv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CanMinDTO {
	
	private Long id;
	private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String userName;
    private String niveau;
    
    private String nbexperience;
    private String dateNaissance;
    private String adresse;

}
