package org.knoesis.util;

import java.util.Arrays;
import java.util.List;

import org.knoesis.config.Config;
import org.knoesis.model.AttributeData;
import org.knoesis.model.Phrase;

/**
 * Format the user input, get their features and create the test file in ARFF format at data/unlabeled_dataset.arff
 * Return the list of phrases used to create the ARFF file
 * @author revathy 
 */

public class CreateDataSet {
		
		/**
		 * Create ARFF file for a single sentence
		 * @param chunkerOutput - Chunker Output of the sentence
		 * @return List<Phrase> - Phrases from the ARFF file
		 * @throws Exception
		 */
		public static List<Phrase> createDataSet(String chunkerOutput) throws Exception {
				List<Phrase> phrases = Preprocess.processChunkerOutput(chunkerOutput);
				DeriveFeatures df = new DeriveFeatures();
				List<AttributeData> attributeData = df.getFeaturesForPhrases(phrases);
				WekaOperations.createArffFile(attributeData, Arrays.asList(Config.TEMPORAL_CLASSES));
				return phrases;
		}		
				
}
