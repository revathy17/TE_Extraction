package org.knoesis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.knoesis.classification.RandomForestClassifier;
import org.knoesis.config.Config;
import org.knoesis.model.Phrase;

/**
 * This class processes the chunker output to extract the phrases which will be used to create the input file for the RandomForest
 * Classifier
 * @author revathy
 */

public class Preprocess {
		
			static Logger log = Logger.getLogger(RandomForestClassifier.class.getName());			
			
			/**
			 * Process chunker output and extract phrases from it
			 * These phrases are input to the Random Forest classifier and will be classified as temporal
			 * expression or not temporal expression
			 * @param chunkerOutput chunker output for a sentence
			 * @return List<String> List of phrases extracted from chunker output that will be used as input for the classifier
			 */
			public static List<Phrase> processChunkerOutput(String chunkerOutput) throws Exception {
				
					try {
							List<String> chunkerPhrases = extractPhrase(chunkerOutput);
							List<Phrase> phrases = new ArrayList<Phrase>();
							int count = 1;
							for(String originalPhrase : chunkerPhrases) {
									String formattedPhrase = replaceDigits(originalPhrase);
									Phrase p = new Phrase(count++,originalPhrase,formattedPhrase,"");
									phrases.add(p);
							}				
							return phrases;
					} catch (Exception e) {
							log.error("Error in processing chunker output : " , e);
							throw(e);
					}
			}
			
			/**
			 * Format the data - replace all digits by X
			 */
			private static String replaceDigits(String phrase) {
					String formattedPhrase = phrase.replaceAll("[0-9]", "X");
					return formattedPhrase;
			}
			
			/**
			 * Extract the preconfigured types of phrases from the chunker output that will be the input
			 * to the Random Forest Classifier
			 * @param chunkerOutput
			 * @return List<String> - List of Phrases
			 */
			private static List<String> extractPhrase(String chunkerOutput) {
					List<String> patterns = Arrays.asList(Config.PHRASES_FROM_CHUNKEROUTPUT);
					List<String> phrases = new ArrayList<String>();
					
					for(String pattern : patterns) {
							Pattern p = Pattern.compile("\\(" + pattern + "(.*?)\\)+");
							Matcher matcher = p.matcher(chunkerOutput);				
							while(matcher.find()) {
								String chunk = matcher.group();				
								phrases.add(extractText(chunk,pattern.length()));
							}
					}
					return phrases;		
			}
			
			/**
			 * Extract actual text from the chunker output
			 * Example for the input "[NP 3 months]", the output will be "3 months"  
			 * @param chunkerOutput chunker output of a sentence
			 * @param chunkerTokenLength length of the tokens indicating the phrase type. Example: "NP", "ADVP", "QP", "ADJP"
			 * @return the text
			 */
			private static String extractText(String chunkerOutput, int chunkerTokenLength) {
				
					int length = chunkerOutput.length();
					int startIndex = 1 + chunkerTokenLength + 1;
					String text = "";
					if(length > 1) { 
							text = chunkerOutput.substring(startIndex,(length-1));
					} else 
							log.error("Error in extracting text from the chunker output: " + chunkerOutput);
					
					return text;
				
			}
	
			
}
