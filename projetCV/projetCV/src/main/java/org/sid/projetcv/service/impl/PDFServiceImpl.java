package org.sid.projetcv.service.impl;


import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.sid.projetcv.service.PDFService;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PDFServiceImpl implements PDFService {
	
	
	@Override
	// Méthode pour extraire tout le texte d'un fichier PDF
	public String extractText(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
    
	@Override
	// Méthode pour vérifier si un mot spécifique est présent dans le texte extrait du PDF
    public boolean containsWord(File file, String word) throws IOException {
        String text = extractText(file);
        return text.contains(word);
    }
    
	@Override
	 // Méthode pour compter le nombre d'occurrences d'un mot spécifique dans le texte extrait du PDF
	    public int countWordOccurrences(File file, String word) throws IOException {
	    	// Extraire le texte du fichier
	        String extractedText = extractText(file);
	        
	        // Compter les occurrences du mot
	        return countOccurrences(extractedText, word);
	        }
	    
	    private int countOccurrences(String text, String word) {
	        // Convertir le texte et le mot en minuscules pour une recherche insensible à la casse
	        String lowercaseText = text.toLowerCase();
	        String lowercaseWord = word.toLowerCase();

	        // Compter le nombre d'occurrences du mot
	        int count = 0;
	        int index = 0;
	        while (index != -1) {
	            index = lowercaseText.indexOf(lowercaseWord, index);
	            if (index != -1) {
	                count++;
	                index += lowercaseWord.length();
	            }
	        }

	        return count;
	    }
	    
    

}
