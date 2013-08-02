package org.knoesis.optimize;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.knoesis.config.Config;

/**
 * This class contains functions used by the objects of Optimize class
 * @author revathy 
 */

public class OptimizeFunctions {
			
			/**
			 * Check if the temporal expression already contains a temporal modifier. If yes, we do not need to look for 
			 * one in the sentence
			 * @param temporalExpression
			 * @return true if the temporal expression already contains a temporal modifier else false
			 */
			static boolean checkModifierInTE(String temporalExpression) {
						String[] teWords = temporalExpression.split("\\s+");
						List<String> temporalModifiers = Arrays.asList(Config.TEMPORAL_MODIFIERS);
						
						for(String tew : teWords) {
								if(temporalModifiers.contains(tew))
									return true;
						}
						return false;
			}
			
			/**
			 * Return adjacent words from either side of the temporal expression
			 * Remove any punctuation marks from the adjacent word
			 * @param sentence
			 * @param temporalExpression
			 * @return Map<Integer, String> - <index of the adjacent word in the sentence, word>
			 */
			static Map<Integer, String> getAdjacentWords(String[] wordsOfSentence, String[] wordsOfTE) {
						Map<Integer, String> adjacentWords = new TreeMap<Integer, String>();
						Map<Integer, String> indexOfTE = getIndexOfExpression(wordsOfSentence, wordsOfTE);
										
						int begIndex = -1;
						int endIndex = -1;
						for(Map.Entry<Integer, String> te : indexOfTE.entrySet()) {
								
								//String adjacentWord = removePunctuation(wordsOfTE[0]);
								String adjacentWord = wordsOfTE[0];
								if(te.getValue().equalsIgnoreCase(adjacentWord))
									begIndex = te.getKey();
								
								//adjacentWord = removePunctuation(wordsOfTE[wordsOfTE.length-1]);
								adjacentWord = wordsOfTE[wordsOfTE.length-1];
								if(te.getValue().equalsIgnoreCase(adjacentWord))
									endIndex = te.getKey();
								
						}
						
						if(begIndex > 0)
								adjacentWords.put(begIndex - 1, removePunctuation(wordsOfSentence[begIndex - 1]));
						if(endIndex < (wordsOfSentence.length-1))
								adjacentWords.put(endIndex + 1, removePunctuation(wordsOfSentence[endIndex + 1]));	
						
						return adjacentWords;
			}
			
			private static String removePunctuation(String word) {
						for(String punct : Config.PUNCTUATION) {
					 		if(word.contains(punct)) {
					 			word = word.replace(punct,"");
					 		}
					 	}			
					 	return word;	
			}
			
			
			static Map<Integer, String> getIndexOfExpression(String[] sentence, String[] expression) {
						Map<Integer, String> indexOfExpression = new TreeMap<Integer, String>();
						
						int j =0;
						for(int i=0; i<sentence.length && j<expression.length; i++) {
								if (sentence[i].equalsIgnoreCase(expression[j])) {
										indexOfExpression.put(i, sentence[i]);		
										j++;
								} else {
										indexOfExpression.clear();
										j = 0;
								}
						}
						return indexOfExpression;					
			}
	
}
