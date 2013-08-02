package org.knoesis.classification;

import java.io.File;

/**
 * Interface for classifier
 * @version 1.0
 * @date 29-May-2013  
 * @author Revathy  
 */

public interface Classifier {
		
		public void trainClassifier(File trainingData) throws Exception;
		public void writeClassifierToDisk() throws Exception;
		public void classifyData(File testData, File labeledFile) throws Exception;		
		public void loadClassifierFromDisk() throws Exception;
			
}