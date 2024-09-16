package org.sid.projetcv.controller;

import java.io.File;
import java.io.IOException;

import org.sid.projetcv.service.TextExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/extract")
public class TextExtractionController {
	@Autowired
    private TextExtractorService textExtractor;
	
	 @PostMapping("/text")
	 public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file) {
		 try {
	            // Convertir MultipartFile en File
	            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
	            file.transferTo(convFile);

	            // Extraire le texte
	            String extractedText = textExtractor.extractText(convFile);

	            // Supprimer le fichier temporaire
	            convFile.delete();

	            // Retourner le texte extrait
	            return new ResponseEntity<>(extractedText, HttpStatus.OK);

	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}
