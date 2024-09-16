package org.sid.projetcv.service;

import java.io.File;
import java.io.IOException;

public interface TextExtractorService {
	String extractText(File file);
	int countWordOccurrences(File file, String word) throws IOException;

}
