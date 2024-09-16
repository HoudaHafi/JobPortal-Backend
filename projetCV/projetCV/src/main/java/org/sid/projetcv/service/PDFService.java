package org.sid.projetcv.service;

import java.io.File;
import java.io.IOException;

public interface PDFService {
	
	String extractText(File file) throws IOException;
	boolean containsWord(File file, String word) throws IOException;
	int countWordOccurrences(File file, String word) throws IOException;

}
