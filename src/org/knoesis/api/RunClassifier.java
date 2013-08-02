package org.knoesis.api;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.knoesis.classification.Classifier;
import org.knoesis.classification.RandomForestClassifier;
import org.knoesis.config.Config;
import org.knoesis.model.Phrase;
import org.knoesis.optimize.Optimize;
import org.knoesis.util.CreateDataSet;
import org.knoesis.util.WekaOperations;
import au.com.bytecode.opencsv.CSVReader;

public class RunClassifier {		
	
		static Logger log = Logger.getLogger(RunClassifier.class.getName());
		
			
		/**
		 * Create an ARFF file from the chunker output
		 * @param chunkerOutput - chunker output of the sentence whose temporal expressions need to be annotated
		 * @return List<Phrase> - List of phrases (from the sentence) used to create the input file
		 * @throws Exception 
		 */
		public static List<Phrase> createInputFile(String chunkerOutput) throws Exception {
					List<Phrase> phrases = new ArrayList<Phrase>();
					phrases = CreateDataSet.createDataSet(chunkerOutput);			
					return phrases;
		}
		
		
		/**
		 * Run the Random Forest Classifier to classify the phrases in ARFF file
		 * 1. Load the Classifier from disk
		 * 2. Label the unlabeled dataset
		 * 3. Convert labeled ARFF file to CSV file		
		 * @throws Exception		 
		 */
		public static void runRFClassifier() throws Exception {				
					Classifier classifier = new RandomForestClassifier();				
					classifier.loadClassifierFromDisk();					
					classifier.classifyData(new File(Config.TESTINGDATAFILE), new File(Config.LABELED_FILE_ARFF));					
					WekaOperations.convertArffToCsv(Config.LABELED_FILE_ARFF, Config.LABELED_FILE_CSV);								
		}
		
		/**
		 * Read the labeled CSV file - Formatted Temporal Expression from Column 1 and Temporal Class from Column 10
		 * @param List<Phrase> List of unformatted phrases
		 * Return the unformatted phrase and corresponding class
		 */
		private static List<Phrase> readLabeledCsvFile(List<Phrase> phrases) throws Exception {
					CSVReader reader = null;				
					reader = new CSVReader(new FileReader(Config.LABELED_FILE_CSV),',','\'');
					String [] nextLine;
					int id = 0;
					int lineNo = 0;
					int phraseId = 1;
					List<Phrase> temporalExpressions = new ArrayList<Phrase>();
					while ((nextLine = reader.readNext()) != null) {
							if(lineNo++ > 0) {
								String formattedPhrase = nextLine[Config.PHRASE_INDEX_ARFF];
								String teClass = nextLine[Config.CLASS_INDEX_ARFF];
								String phrase = phrases.get(id++).getPhrase();
								if(!teClass.equalsIgnoreCase("NONE"))
									temporalExpressions.add(new Phrase(phraseId++,phrase,formattedPhrase,teClass));								
							}						
					}	
					reader.close();
					return temporalExpressions;			
		}
		
		
		/**
		 * The current optimization rules only identify the RDT/RT classes mis-annotated as DURATION
		 * This is hard-coded in the following function. This should be modified if other classes are need to be optimized
		 * @param sentence
		 * @param temporalExpression
		 * @return
		 */
		public static List<Phrase> optimizeTemporalExpressions(String sentence, List<Phrase> temporalExpression) {
					for(Phrase p : temporalExpression) {
							if(p.getClassifierAnnotation().equals("DURATION")) {
									Optimize o = new Optimize(sentence, p.getPhrase());
									int returnClass = o.optimizeTemporalExpression();
									if(returnClass == 1) {
											p.setPhrase(o.getTemporalExpression());
											p.setClassifierAnnotation("RDT");
									} else if(returnClass == 2) {
											p.setClassifierAnnotation("RT");
									}
							}		
							if(p.getClassifierAnnotation().equals("RDT")) {
									Optimize o = new Optimize(sentence, p.getPhrase());
									int returnClass = o.optimizeTemporalExpression();
									if(returnClass == 2)
											p.setClassifierAnnotation("RT");
							}
							
					}
					return temporalExpression;
		}
		
		
		/**
		 * @param chunkerOutput - chunker output of the sentence whose temporal expressions need to be annotated
		 * @return List<Phrase> - List of temporal expressions and their class
		 * @throws Exception
		 */
		public static List<Phrase> annotateTemporalExpression (String sentence, String chunkerOutput) throws Exception {
					List<Phrase> phrases = createInputFile(chunkerOutput);
					runRFClassifier();		
					List<Phrase> temporalExpressions = readLabeledCsvFile(phrases);
					List<Phrase> optimizedTemporalExpressions = optimizeTemporalExpressions(sentence, temporalExpressions);
					return optimizedTemporalExpressions;
		}
		
}
