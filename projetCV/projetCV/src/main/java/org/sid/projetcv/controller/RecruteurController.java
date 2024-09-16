package org.sid.projetcv.controller;

import org.sid.projetcv.dto.RecMaxDTO;
import org.sid.projetcv.entity.Recruteur;
import org.sid.projetcv.mapper.RecMaxMapper;
import org.sid.projetcv.service.RecruteurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/recruteur")
@AllArgsConstructor
@CrossOrigin("*")
public class RecruteurController {
	
	private final RecruteurService recruteurService;
	private final RecMaxMapper recruteurMapper;
	
	@GetMapping("/{id}")
    public ResponseEntity<RecMaxDTO> findById(@PathVariable Long id) {
		RecMaxDTO recdto = recruteurMapper.mapToDto(recruteurService.findById(id));
        return ResponseEntity.ok(recdto);
    }
	
	@PutMapping("/update/{id}")
	public ResponseEntity<RecMaxDTO> updateRecruiter(@PathVariable Long id, @RequestBody RecMaxDTO recMaxDTO) {
        Recruteur rec = recruteurMapper.mapToEntity(recMaxDTO);
        RecMaxDTO updatedDto = recruteurMapper.mapToDto(recruteurService.UpdateRec(id, rec));
        return new ResponseEntity<RecMaxDTO>(updatedDto, HttpStatus.OK);
    }
	

}
