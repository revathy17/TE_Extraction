package org.knoesis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.knoesis.classification.RandomForestClassifier;
import org.knoesis.data.DataManager;
import org.knoesis.model.AttributeData;
import org.knoesis.model.Feature;
import org.knoesis.model.Phrase;

/**
* From each phrase in the chunker output calculate the values of the following features
* hasDay, hasMonth, hasTERef, hasNumString, hasTime, hasTimeRef
* @author revathy
* @date 7-June-2013
*/

public class DeriveFeatures {
			private List<String> days;
			private List<String> months;
			private List<String> numString;
			private List<String> refExpr1;
			private List<String> refExpr2;
			private List<String> time;
			private List<String> temporalModifiers;
			private List<String> measurementUnits;
			
			static Logger log = Logger.getLogger(RandomForestClassifier.class.getName());
			
			public DeriveFeatures() {
						days = new ArrayList<String>();
						months = new ArrayList<String>();
						numString = new ArrayList<String>();
						refExpr1 = new ArrayList<String>();
						refExpr2 = new ArrayList<String>();
						time = new ArrayList<String>();
						temporalModifiers = new ArrayList<String>();
						measurementUnits = new ArrayList<String>();
						getData();
			}
				
			/**
			 * Get the features from the database
			 */
			private void getData() {
						try {
								DataManager dm = new DataManager();
								List<Feature> features = dm.getFeatures();
								for(Feature f : features) {
										String typeName = f.getTypeName();
											if(typeName.equalsIgnoreCase("day")) 
													days.add(f.getFeatureName());
											else if(typeName.equalsIgnoreCase("month"))
													months.add(f.getFeatureName());			
											else if(typeName.equalsIgnoreCase("num_string"))
													numString.add(f.getFeatureName());
											else if(typeName.equalsIgnoreCase("ref_expr1"))
													refExpr1.add(f.getFeatureName());
											else if(typeName.equalsIgnoreCase("ref_expr2"))
													refExpr2.add(f.getFeatureName());
											else if(typeName.equalsIgnoreCase("time"))
													time.add(f.getFeatureName());
											else if(typeName.equalsIgnoreCase("temporal_modifier"))
													temporalModifiers.add(f.getFeatureName());
											else if(typeName.equalsIgnoreCase("measurement_unit"))
													measurementUnits.add(f.getFeatureName());
								}
						} catch (Exception e) {
								log.error("Error in retrieving features from the SQLite database", e);
						}
			}
			
			/**
			 * Check if the phrase contains a day
			 * @param phrase
			 * @return
			 */
			private int checkDay(String phrase) {
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String day : days) {
								if(word.equalsIgnoreCase(day))
									return 1;
							}
						}
						return 0;
			}
			
			/**
			 * Check if the phrase contains a month
			 * @param phrase
			 * @return
			 */
			private int checkMonth(String phrase) {
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String month : months) {
								if(word.equalsIgnoreCase(month))
									return 1;
							}
						}
						return 0;
			}
			
			/**
			 * Check if the phrase contains ref_expr1
			 * @param phrase
			 * @return
			 */
			private int checkRefExpr1(String phrase) {
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String te : refExpr1) {
								if(word.equalsIgnoreCase(te))
									return 1;
							}
						}
						return 0;
			}

			/**
			 * Check if the phrase contains ref_expr1
			 * @param phrase
			 * @return
			 */
			private int checkRefExpr2(String phrase) {
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String te : refExpr2) {
								if(word.equalsIgnoreCase(te))
									return 1;
							}
						}
						return 0;
			}
			
			/**
			 * Check if the phrase contains a number (in words, not digits)
			 * @param phrase
			 * @return
			 */
			private int checkNumString(String phrase) {
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String num : numString) {
								if(word.equalsIgnoreCase(num))
									return 1;
							}
						}
						return 0;
			}
			
			/**
			 * Check if the phrase contains time phrases (minutes,seconds, hours and their variations)
			 * @param phrase
			 * @return	  
			 */
			private int checkTimePhrase(String phrase) {				
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String tm : time) {
								if(word.equalsIgnoreCase(tm))
									return 1;
							}
						}
						return 0;				
			}
			
			/**
			 * Check if the phrase contains any date patterns
			 * @param phrase
			 * @return
			 */
			private static int checkForDatePattern(String phrase) {
				String datePattern = "[X]{1,2}-[X]{1,2}-[X]{1,2}"
						+ "|[X]{4}-[X]{1,2}-[X]{1,2}"
						+ "|[X]{1,2}-[X]{1,2}-[X]{4}"
						+ "|[X]{4}-[X]{1,2}"
						+ "|[X]{1,2}-[X]{4}"
						+ "|[X]{2}-[X]{2}"
						+ "|[X]{1,2}/[X]{1,2}/[X]{1,2}"
						+ "|[X]{4}/[X]{1,2}/[X]{1,2}"
						+ "|[X]{1,2}/[X]{1,2}/[X]{4}"
						+ "|[X]{4}/[X]{1,2}"
						+ "|[X]{1,2}/[X]{4}"
						+ "|[X]{2}/[X]{2}";											

						Pattern p = Pattern.compile(datePattern);
						Matcher matcher = p.matcher(phrase);
						while(matcher.find()) {
							return 1;
						}
						return 0;
			}
			
			/**
			 * Check if the phrase contains any time patterns
			 * @param phrase
			 * @return
			 */
			private static int checkForTimePattern(String phrase) {
				
						String timePattern = "\\s[X]{1,2}:[X]{1,2}\\s|[X]{1,2}:[X]{1,2}\\s|\\s[X]{1,2}:[X]{1,2}";
						Pattern p = Pattern.compile(timePattern);
						Matcher matcher = p.matcher(phrase);
						while(matcher.find()) {
							return 1;
						}
						return 0;
			}
			
			/**
			 * Check if the phrase contains any temporal modifiers
			 * @param phrase
			 */
			private int checkForTemporalModifiers(String phrase) {
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String tm : temporalModifiers) {
								if(word.equalsIgnoreCase(tm))
									return 1;
							}
						}
						return 0;						
			}
			
			
			/**
			 * Check if the phrase contains any temporal modifiers
			 * @param phrase
			 */
			private int checkForMeasurementUnits (String phrase) {
						String[] words = phrase.split("\\s+");
						for(String word : words) {
							for(String mu : measurementUnits) {
								if(word.equalsIgnoreCase(mu))
									return 1;
							}
						}
						return 0;						
			}
				
			/**
			 * For each phrase check for hasDay, hasMonth, hasRefExpr1, hasRefExpr2, hasNumString,
			 * hasTimePhrase, hasDatePattern, hasTimePattern 
			 * Create AttributeData using the above information
			 * @parm Phrase
			 * */
			private AttributeData checkFeatures(String phrase) {
				
						int hasDay = checkDay(phrase);
						int hasMonth = checkMonth(phrase);
						int hasRefExpr1 = checkRefExpr1(phrase);
						int hasRefExpr2 = checkRefExpr2(phrase);
						int hasNumString = checkNumString(phrase);
						int hasTimePhrase = checkTimePhrase(phrase);
						int hasDatePattern = checkForDatePattern(phrase);
						int hasTimePattern = checkForTimePattern(phrase);
						int hasTemporalModifier = checkForTemporalModifiers(phrase);
						int hasMeasurementUnit = checkForMeasurementUnits(phrase);
						AttributeData attrData = new AttributeData(phrase, hasDay, hasMonth, hasNumString,
												hasRefExpr1, hasRefExpr2, hasTimePhrase, hasDatePattern, hasTimePattern,
												hasTemporalModifier, hasMeasurementUnit);
						return attrData;
				
			}
			
			
			public List<AttributeData> getFeaturesForPhrases(List<Phrase> phrases) {
				
						List<AttributeData> classifierFeatures = new ArrayList<AttributeData>();
						for(Phrase p : phrases) {
								AttributeData ad = checkFeatures(p.getFormattedPhrase());
								classifierFeatures.add(ad);
						}
						return classifierFeatures;
				
			}
			
}
