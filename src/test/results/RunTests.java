package test.results;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.knoesis.api.RunClassifier;


import org.knoesis.model.Phrase;
import test.data.DataManager;
import test.evaluation.Evaluate;
import test.model.Sentence;

/**
 * Use this class to run the tests (for 3 datasets) that give the results posted in the technical report
 * @author revathy
 * @date 24-July-2013
 */
public class RunTests {
		
	/**
	 * 1. Read sentences from database
	 * 2. Process Chunker output
	 * 3. Classify each phrase
	 * 4. Insert classified phrases in the database			  
	 */			
	
	public static void classifyData(int documentId1, int documentId2) {
			
			DataManager dm = new DataManager();					
			/*Step 1 : Read sentences from the database*/
			List<Sentence> sentences = dm.getSentences(documentId1, documentId2);
						
			/*Step 2 : Process each sentence*/
			for(Sentence s : sentences) {
					int documentId = s.getDocumentId();
					int sentenceId = s.getSentenceId();					
					String sentence = s.getSentence();
					//String chunkerOutput = s.getChunkerOutput();
					String chunkerOutput = s.getChunkerOutput().replace("(, ,)","");
					List<Phrase> temporalExpressions = new ArrayList<Phrase>();
					
					System.out.println("Processing document : " + documentId + " sentence : " + sentenceId);
					try {
						 temporalExpressions = RunClassifier.annotateTemporalExpression(sentence, chunkerOutput);
					} catch (Exception e) {
						System.out.println("Error in classifying the temporal expression");
						e.printStackTrace();
					}
					
					/*Update the temporal expression in the database*/
					for(Phrase p : temporalExpressions) {
							dm = new DataManager();
							dm.insertClassifierAnnotation(documentId, sentenceId, p.getPhraseId(), p.getPhrase(), p.getClassifierAnnotation());
					}
			}							
			
	}
	
	
	public static void printResults(int documentId1, int documentId2, boolean strictMatch) {
			String[] annotationScheme = {"DURATION","DATE","TIME","RDT","RT","ALL"};
			
			System.out.println("Class\tPrecision\tRecall\tF Measure");				
			for(int i=0;i<annotationScheme.length;i++) {
					Evaluate eval = new Evaluate(documentId1, documentId2, annotationScheme[i], strictMatch);
					double precision = eval.getPrecision();
					double recall = eval.getRecall();
					double fMeasure = eval.getFMeasure();
					System.out.println(annotationScheme[i]+"\t"+formatDouble(precision)+"\t"+formatDouble(recall)+"\t"+formatDouble(fMeasure));
			}			
	}

	public static String formatDouble(double number) {
			DecimalFormat df = new DecimalFormat("#.##");
			return df.format(number);
	}
	
	public static void main(String[] args) {			
			//classifyData(116, 140);
			printResults(116, 140, true);
			//classifyData(141, 166);
			printResults(141, 166, true);
			//classifyData(167,186);
			printResults(167, 186, true);
			//classifyData(1, 115);	
			//printResults(1, 115, true);			
	}
}
