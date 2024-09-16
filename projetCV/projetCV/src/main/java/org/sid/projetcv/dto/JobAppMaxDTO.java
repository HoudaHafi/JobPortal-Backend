package org.sid.projetcv.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class JobAppMaxDTO {
	private Long id;
    private String title;
    private String status;
    private String cv;
    private Date datecandidateur;
    private JobDTO job;
    private CanMinDTO candidat;

}
