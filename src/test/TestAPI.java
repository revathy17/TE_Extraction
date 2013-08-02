package test;

import java.util.List;

import org.apache.log4j.Logger;
import org.knoesis.api.*;
import org.knoesis.model.Phrase;


public class TestAPI {

			static Logger log = Logger.getLogger(TestAPI.class.getName());
			public static void main(String[] args) {
				
					/**
					 * User Input: Sentence and its Chunker output				
					 */
					//String sentence = "She has also had right ovary was removed that was emerged in December 2006";
					//String chunkerOutput = "(NP She)(VP has)(ADVP also)(VP had)(NP right ovary)(VP was)(VP removed)(SBAR that)(VP was)(VP emerged)(PP in)(NP December 2006)";
					//String sentence = "Pt was then discharged to Arbour-Fuller Hospital Senior Life where she was in normal state of health until three days ago";
					//String chunkerOutput = "(NP Pt)(VP was)(ADVP then)(VP discharged)(PP to)(NP Arbour-Fuller Hospital Senior Life)(WHADVP where)(NP she)(VP was)(PP in)(NP her normal state)(PP of)(NP health)(PP until)(NP three days)(ADVP ago)";
					String sentence = "She is a 35-pack-year smoker but quit 15 years ago.";
					String chunkerOutput = "(NP She)(VP is)(NP a 35-pack-year smoker)(VP quit)(NP 15 years)(ADVP ago)";

					
					/** Train a Random Forest Classifier
					 *  Training Data is at data\trainingdata.arff
					 *  The Classifier is written to the disk at model\RandomForest.model
					 *  This need not be done everytime, once the model is written to the disk, the same model can be used to classify 
					 *  unlabeled data
					 *  Uncomment if you want to create a new model
					 **/
					try {
						 	//CreateClassifier.createRFClassifier();
					} catch (Exception e) {
						 	log.error("Error in training the Random Forest Classifier",e);
					}
					
					
					/** Use the classifier written at model\RandomForest.model to classify expressions 
					 *  from user input
					 **/
					try {
							List<Phrase> temporalExpressions = RunClassifier.annotateTemporalExpression(sentence, chunkerOutput);
							/*Print the extracted temporal expressions to the console*/
							for(Phrase p : temporalExpressions) {
									System.out.println(p.getPhraseId() + "\t" + p.getPhrase() + "\t" + p.getClassifierAnnotation());
							}
					} catch (Exception e) {
							log.error("Error in classifying temporal expression", e);
					}			
				
											 
			}
}
