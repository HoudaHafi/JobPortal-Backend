package org.sid.projetcv.controller;

import org.sid.projetcv.dto.CanMinDTO;
import org.sid.projetcv.entity.Candidat;
import org.sid.projetcv.mapper.CandidatMapper;
import org.sid.projetcv.service.CandidatService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/candidat")
@AllArgsConstructor
@CrossOrigin("*")
public class CandidatController {
	
	private final CandidatService candidatService;
	private final CandidatMapper candidatMapper;
	
	
	@GetMapping("/{id}")
    public ResponseEntity<CanMinDTO> findById(@PathVariable Long id) {
		CanMinDTO candto = candidatMapper.mapToDto(candidatService.findById(id));
        return ResponseEntity.ok(candto);
    }
	
	@PutMapping("/update/{id}")
	public ResponseEntity<CanMinDTO> updateCandidate(@PathVariable Long id, @RequestBody CanMinDTO canMinDTO) {
        Candidat can = candidatMapper.mapToEntity(canMinDTO);
        CanMinDTO updatedDto = candidatMapper.mapToDto(candidatService.UpdateCan(id, can));
        return new ResponseEntity<CanMinDTO>(updatedDto, HttpStatus.OK);
    }
	
	@GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> getCandidatAvatar(@PathVariable Long id) {
        try {
            Resource avatar = candidatService.getCanAvatar(id);

            // Determine the content type based on the file's extension
            String contentType = "application/octet-stream"; // Default content type
            if (avatar.getFilename().endsWith(".png")) {
                contentType = "image/png";
            } else if (avatar.getFilename().endsWith(".jpg") || avatar.getFilename().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (avatar.getFilename().endsWith(".gif")) {
                contentType = "image/gif";
            }

            // Create the response entity
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + avatar.getFilename() + "\"")
                    .body(avatar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	

}
