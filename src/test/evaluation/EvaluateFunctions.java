package test.evaluation;

import java.util.Arrays;
import java.util.List;
import test.data.DataManager;
import test.model.Annotation;

/**
 * Use this class to calculate False Positives and False Negatives for Precision and Recall
 * @author revathy
 *
 */
public class EvaluateFunctions {

				/**
				 * Return False Positives
				 * Phrases annotated by the classifier but not present in the manual annotations
				 */
				public static double getFalsePositives(int documentId1, int documentId2, String teClass, boolean strictMatch) {
							double falsePositive = 0.0;
							DataManager dm = new DataManager();
							List<Annotation> classifierAnnotations = dm.getClassifierAnnotation(documentId1, documentId2, teClass);
							
							dm = new DataManager();
							List<Annotation> manualAnnotations = dm.getManualAnnotation(documentId1, documentId2, teClass);
							
											
							for(Annotation ca : classifierAnnotations) {
									/*Check if this classifier annotation is present in the manual annotations, 
									 * if not then increment the False positive counter*/
									int caDocId = ca.getDocumentId();
									int caSentId = ca.getSentenceId();
									String caPhrase = ca.getPhrase();
									String caClass = ca.getTeClass();
									boolean flag = false;
									for(Annotation ma : manualAnnotations) {
											if(caDocId == ma.getDocumentId() && caSentId == ma.getSentenceId() && 
											  matchPhrase(caPhrase, ma.getPhrase(), strictMatch) && ma.getTeClass().equalsIgnoreCase(caClass)) {
													flag = true;
													break;
											}												
									}
									if(flag == false) {
											falsePositive++;
											//System.out.println(ca.getDocumentId() + "\t" + ca.getSentenceId() + "\t" + ca.getPhraseId() + 
											//"\t" + ca.getPhrase() + "\t" + ca.getTeClass());
									}
							}
													
							return falsePositive;
				}
				
				
				/**
				 * Return False Negatives
				 * Phrases annotated manually but not identified by the classifier
				 */
				public static double getFalseNegatives(int documentId1, int documentId2, String teClass, boolean strictMatch) {
					
							double falseNegative = 0.0;
							DataManager dm = new DataManager();
							List<Annotation> classifierAnnotations = dm.getClassifierAnnotation(documentId1, documentId2, teClass);
							
							dm = new DataManager();
							List<Annotation> manualAnnotations = dm.getManualAnnotation(documentId1, documentId2, teClass);
							
							for(Annotation ma : manualAnnotations) {
									/*Check if this classifier annotation is present in the manual annotations, 
									 *if not then increment the False positive counter*/
									int maDocId = ma.getDocumentId();
									int maSentId = ma.getSentenceId();
									String maPhrase = ma.getPhrase();
									String maClass = ma.getTeClass();
									boolean flag = false;
									for(Annotation ca : classifierAnnotations) {
											if(maDocId == ca.getDocumentId() && maSentId == ca.getSentenceId() && 
													matchPhrase(maPhrase, ca.getPhrase(), strictMatch) && ca.getTeClass().equalsIgnoreCase(maClass)) {
													flag = true;
													break;
											}												
									}
									if(flag == false) {
											falseNegative++;
											//System.out.println(ma.getDocumentId() + "\t" + ma.getSentenceId() + "\t" + ma.getPhraseId() + 
											//		"\t" + ma.getPhrase() + "\t" + ma.getTeClass());
									}
										
							}							
							return falseNegative;
				}	
				
				
				/**
				 * A "false" value for "strictMatch" will ensure that if the classifier has annotated "the three days prior" as RDT 
				 * and manual annotation is "three days prior to admission" then this classifier annotation is not considered as 
				 * a false positive
				 * The sentence for the above example is "Unclear etiology headache over the three days prior to admission in association with fever was concerning for a CNS etiology"
				 * @param classifierPhrase
				 * @param manualPhrase
				 * @param strictMatch
				 * @return
				 */			
				private static boolean matchPhrase(String classifierPhrase, String manualPhrase, boolean strictMatch) {
							
							if(strictMatch) {
									if(classifierPhrase.equals(manualPhrase))
											return true;								
							} else {
									String[] cpWords = classifierPhrase.split("\\s+");
									String[] mpWords = manualPhrase.split("\\s+");
									
									for(String cpw : cpWords) {
										if(Arrays.asList(mpWords).contains(cpw)) 
											return true;
									}
									
									for(String mpw : mpWords) {
										if(Arrays.asList(cpWords).contains(mpw))
											return true;
									}	
							}
							
							return false;							
				}
				
				
}
