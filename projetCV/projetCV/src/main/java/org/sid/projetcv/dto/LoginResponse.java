package org.sid.projetcv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
public class LoginResponse {
	
	private String token;
	private Long id;
	private String role;

}
