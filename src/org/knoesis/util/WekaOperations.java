package org.knoesis.util;

/**
* Perform operations common to all WEKA classifiers
* @version 1.0
* @date 29-May-2013
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.knoesis.config.Config;
import org.knoesis.model.AttributeData;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class WekaOperations {
			
			/**
			 * read input arff file
			 * @param fileName - ARFF file
			 * @return Instances 
			 * @throws IOException
			 */
			public static Instances readArffFile(File file) throws FileNotFoundException, IOException {
						BufferedReader reader = new BufferedReader(new FileReader(file));
						Instances data = new Instances(reader);
						reader.close();		 
						// setting the last column as class attribute 
						data.setClassIndex(data.numAttributes() - 1);		
						return data;
			}
			
			/**
			 * Filter input file using StringToWordVector Filter
			 * @param Instances
			 * @throws Exception
			 */
			public static Instances applyStringToWordFilter(Instances dataFileInstance) throws Exception {
					
						StringToWordVector stringtoword = new StringToWordVector();
						Standardize filter = new Standardize();
						filter.setInputFormat(dataFileInstance);		
						stringtoword.setOptions(Config.filteredClassifierOptions);
						stringtoword.setInputFormat(dataFileInstance);
						Instances newDataFile = Filter.useFilter(dataFileInstance, stringtoword);
						return newDataFile;
				
			}
			
			/**
			 * Print summary results of the classifier 
			 */
			public static void printResults(Evaluation evaluate) throws Exception {						
						System.out.println("=== Summary ===");
						System.out.println(evaluate.toSummaryString());
						System.out.println(evaluate.toClassDetailsString());
						System.out.println(evaluate.toMatrixString());				
			}		
			
			
			/**
			 * Create ARFF file using all the features and annotation classes
			 * @param phrases: List of attributes
			 * @param annotationClasses: List of class labels
			 */
			public static void createArffFile(List<AttributeData> attributeData, List<String> annotationClasses) throws Exception {
				
					 	 FastVector attributes = new FastVector();
					     FastVector attValRelations = new FastVector();
					     FastVector attVals = new FastVector();
					     Instances  data;
					     double[]  values;	     
					   
						 attVals.addElement("0");
						 attVals.addElement("1");
						    
						 attributes.addElement(new Attribute("phrase", (FastVector) null));
					     attributes.addElement(new Attribute("hasDay", attVals));
					     attributes.addElement(new Attribute("hasMonth", attVals));
					     attributes.addElement(new Attribute("hasNumString", attVals));
					     attributes.addElement(new Attribute("hasRefExpr1", attVals));
					     attributes.addElement(new Attribute("hasRefExpr2", attVals));
					     attributes.addElement(new Attribute("hasTimeRef", attVals));
					     attributes.addElement(new Attribute("hasDatePattern", attVals));
					     attributes.addElement(new Attribute("hasTimePattern", attVals));
					     attributes.addElement(new Attribute("hasTemporalModifier", attVals));
					     attributes.addElement(new Attribute("hasMeasurementUnit", attVals));
					     
					     for(String annotationClass : annotationClasses) {
						    	attValRelations.addElement(annotationClass);
						 }  
					     
					     attributes.addElement(new Attribute("class", attValRelations));	
					     
					     //Create Instances Object
					     data = new Instances("temporalexpression", attributes, 0);	 
					     
					     //fill with data
					     for(AttributeData ad : attributeData) {
						    	 	values = new double[data.numAttributes()];
						    	 	String formattedPhrase = ad.getPhrase();
						    	 	values[0] = data.attribute(0).addStringValue(formattedPhrase);
						    	 	values[1] = ad.getDay();
						    	 	values[2] = ad.getMonth();
						    	 	values[3] = ad.getNumString();
						    	 	values[4] = ad.getRefExpr1();
						    	 	values[5] = ad.getRefExpr2();
						    	 	values[6] = ad.getTimeRef();
						    	 	values[7] = ad.getDatePattern();
						    	 	values[8] = ad.getTimePattern();
						    	 	values[9] = ad.getTemporalModifier();
						    	 	values[10] = ad.getMeasurementUnit();
						    	 	values[11] = Instance.missingValue();
						    	 	data.add(new Instance(1.0,values));
					     }
					    
					     File testFile = new File(Config.UNLABELED_FILE_ARFF);
						 FileWriter fw = new FileWriter(testFile);
						 fw.write(data.toString());
						 fw.close();
			}
			
			/**
			 * Read the labeled ARFF file and convert it to CSV 
			 * @throws Exception
			 * @param arffFile  - input file 
			 * @param csvFile - file where the csv output is saved
			 */
			public static void convertArffToCsv(String arffFile, String csvFile) throws Exception {
						
						String[] options = new String[6];
						options[0] = "-F";
						options[1] = ";";
						options[2] = "-i";
						options[3] = arffFile;
						options[4] = "-o";
						options[5] = csvFile;		
						CSVSaver.main(options);		
			}

			
}
