package org.sid.projetcv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EmailRequestDto {
	private Long candidatId;
    private Long recruteurId;
    private String date;
    private String time;
    private String link;

}
