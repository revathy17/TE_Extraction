package org.knoesis.optimize;

import java.util.Map;
import java.util.TreeMap;

import org.knoesis.config.Config;
import org.knoesis.optimize.OptimizeFunctions;


/**
 * For an expression like "3 months ago", the chunker output annotates "3 months" as NP and "ago" as ADVP
 * The classifier then tags "3 months" as DURATION, when this expression is actually an "RDT"
 * To fix this class of errors we reconsider all the expressions annotated as "DURATION" and check the unigram and bigram 
 * of the sentence to see of the DURATION is actually an RDT.
 * This class currently relies on a dictionary contain words like 'ago', 'before', 'after' etc. This module can be improved
 * to check for such temporal modifiers in a different way. 
 * @author revathy
 * @date 26-July-2013
 */

public class Optimize {

			String sentence;
			String temporalExpression;
			
			public Optimize(String sentence, String temporalExpression) {
					this.sentence = sentence;
					this.temporalExpression = temporalExpression;
			}
			
			public int optimizeTemporalExpression() {
					
					String[] sentenceWords = sentence.split("\\s+");
					String[] teWords = temporalExpression.split("\\s+");
					if(!OptimizeFunctions.checkModifierInTE(temporalExpression)) {
							Map<Integer, String> adjacentWords = OptimizeFunctions.getAdjacentWords(sentenceWords, teWords);
							Map<Integer, String> teIndex = OptimizeFunctions.getIndexOfExpression(sentenceWords, teWords);
							Map<Integer, String> modifier = containsModifier(adjacentWords);
							Map<Integer, String> rtModifier = containsRTModifer(adjacentWords);
							if(!modifier.isEmpty()) {
									if(modifier.entrySet().iterator().next().getKey() < teIndex.entrySet().iterator().next().getKey()) 
											setTemporalExpression(modifier.entrySet().iterator().next().getValue() + " " + temporalExpression);											
									else 
											setTemporalExpression(temporalExpression + " " + modifier.entrySet().iterator().next().getValue());
									return 1;
							} else if(!rtModifier.isEmpty()) {
									setTemporalExpression(rtModifier.entrySet().iterator().next().getValue() + " " + temporalExpression);
									/*Change the temporal class from DURATION to RT*/									
									return 2;
							}
					} 
					return -1;
			}
			
			/**
			 * Check if the adjacent words contain a temporal modifier
			 */
			private Map<Integer, String> containsModifier(Map<Integer, String> adjacentWords) {					
					Map<Integer, String> modifier = new TreeMap<Integer,String>(); 
					for(Map.Entry<Integer, String> aw : adjacentWords.entrySet()) {
							for(int i=0; i<Config.TEMPORAL_MODIFIERS.length; i++) {
								if(Config.TEMPORAL_MODIFIERS[i].equalsIgnoreCase(aw.getValue()))
											modifier.put(aw.getKey(), aw.getValue());
							}
					}
					return modifier;
			}
			
			/**
			 * Check if adjacent words contain a Recurring Time modifier
			 */
			private Map<Integer, String> containsRTModifer(Map<Integer, String> adjacentWords) {
					
					Map<Integer, String> modifier = new TreeMap<Integer,String>(); 
					for(Map.Entry<Integer, String> aw : adjacentWords.entrySet()) {
							for(int i=0; i<Config.RT_MODIFIERS.length; i++) {
								if(Config.RT_MODIFIERS[i].equalsIgnoreCase(aw.getValue()))
										modifier.put(aw.getKey(), aw.getValue());
							}
					}
					return modifier;
			}
			
			private void setTemporalExpression(String temporalExpression) {
					this.temporalExpression = temporalExpression;
			}
			
			public String getTemporalExpression() {
					return this.temporalExpression;
			}
}
