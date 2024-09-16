package org.sid.projetcv.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.sid.projetcv.service.TextExtractorService;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class TextExtractorServiceImpl implements TextExtractorService{
	@Override
	public String extractText(File file) {
		try {
            if (file.getName().endsWith(".pdf")) {
                return extractTextFromPdf(file);
            } else if (file.getName().endsWith(".doc")) {
                return extractTextFromDoc(file);
            } else if (file.getName().endsWith(".docx")) {
                return extractTextFromDocx(file);
            } else {
                return extractTextFromImage(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractTextFromPdf(File pdfFile) throws IOException {
    	try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
    
    private String extractTextFromDoc(File docFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(docFile);
             HWPFDocument document = new HWPFDocument(fis);
             WordExtractor extractor = new WordExtractor(document)) {

            return extractor.getText();
        }
    }

    private String extractTextFromDocx(File docxFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(docxFile);
             XWPFDocument document = new XWPFDocument(fis)) {

            StringBuilder text = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                text.append(para.getText()).append("\n");
            }
            return text.toString();
        }
    }

    private String extractTextFromImage(File imageFile) throws IOException, TesseractException {
        BufferedImage image = ImageIO.read(imageFile);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");  // Configurez le chemin de tessdata
        tesseract.setLanguage("eng");  // Configurez la langue
        // Configurer la résolution
        tesseract.setTessVariable("user_defined_dpi", "300");

        return tesseract.doOCR(image);
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        
     // Définir la résolution de l'image
        bimage.getGraphics().drawString("DPI=300", 0, 0);

        return bimage;
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