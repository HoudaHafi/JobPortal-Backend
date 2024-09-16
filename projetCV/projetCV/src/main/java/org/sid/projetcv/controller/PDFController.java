package org.sid.projetcv.controller;


import java.io.File;
import java.io.IOException;

import org.sid.projetcv.service.PDFService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/pdf")
@AllArgsConstructor
public class PDFController {
	
	private final PDFService pdfService;
    
	
	@PostMapping("/extract")
    public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            String text = pdfService.extractText(convFile);
            return new ResponseEntity<>(text, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@PostMapping("/contains")
    public ResponseEntity<Boolean> containsWord(@RequestParam("file") MultipartFile file, @RequestParam("word") String word) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            boolean contains = pdfService.containsWord(convFile, word);
            return new ResponseEntity<>(contains, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@PostMapping("/count")
    public ResponseEntity<Integer> countWordOccurrences(@RequestParam("file") MultipartFile file, @RequestParam("word") String word) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            int count = pdfService.countWordOccurrences(convFile, word);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
	






}
