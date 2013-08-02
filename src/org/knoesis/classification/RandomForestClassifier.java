package org.knoesis.classification;

/**
 * RandomForest Classifier
 * @author Revathy
 * @version 1.0
 * @date 29-May-2013
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;
import org.knoesis.config.Config;
import org.knoesis.util.WekaOperations;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class RandomForestClassifier implements Classifier {

			RandomForest randomForest;
			FilteredClassifier filteredClassifier;
			StringToWordVector stringToWordVector; 
			static Logger log = Logger.getLogger(RandomForestClassifier.class.getName());
			
			public RandomForestClassifier() {
						randomForest = new RandomForest();		
						filteredClassifier = new FilteredClassifier();
						stringToWordVector = new StringToWordVector();
			}
			
			/**
			 * @desc Classify unlabelled data
			 * @param testData: File containing the unlabeled data
			 *          labeledFile: Name of the file where the labeled data is to be saved
			 */
			public void classifyData(File testData, File labeledFile) throws IOException, Exception {
				
						Instances unlabeledData = WekaOperations.readArffFile(testData);
						// create copy
						Instances labeledData = new Instances(unlabeledData);
						// label instances
						for (int i = 0; i < unlabeledData.numInstances(); i++) {
							double classLabel = filteredClassifier.classifyInstance(unlabeledData.instance(i));
							labeledData.instance(i).setClassValue(classLabel);
						}		
						// save labeled data
						BufferedWriter writer = new BufferedWriter(new FileWriter(labeledFile));
						writer.write(labeledData.toString());
						writer.newLine();
						writer.flush();
						writer.close();
						log.info("Unlabelled data at " + testData + " classified and saved at " + labeledFile);
				
			}
			
			/**
			 * Create a StringToWordVector Filter
			 */
			private void createStringToWordVectorFilter() throws Exception {
						stringToWordVector.setOptions(Config.filteredClassifierOptions);				
			}

			/**
			 * Train a RandomForest Classifier
			 * @param trainingData: arff File containing the labeled training data 
			 */
			public void trainClassifier(File trainingData) throws Exception {				
						
						Instances trainingDataInstance = WekaOperations.readArffFile(trainingData);
						createStringToWordVectorFilter();
						filteredClassifier.setFilter(stringToWordVector);
						filteredClassifier.setClassifier(randomForest);
						filteredClassifier.buildClassifier(trainingDataInstance);
						
			}
			
			/**
			 * @desc Write the classifier to the disk
			 */
			public void writeClassifierToDisk() throws IOException {
						ObjectOutputStream outputStream = null;
						try {
								outputStream = new ObjectOutputStream(new FileOutputStream(Config.RF_CLASSIFIERPATH));
								outputStream.writeObject(filteredClassifier);
						} catch(IOException e) {
								throw(new IOException());
						} finally {
								outputStream.flush();
								outputStream.close();								
						}
						log.info("Classifier written to disk at " + Config.RF_CLASSIFIERPATH);
			}
			
			/**
			 * @desc Load a classifier from the disk
			 * @param classifier: The file where the classifier is stored on the disk
			 */
			public void loadClassifierFromDisk() throws Exception {
						log.info("Loading RandomForest Classifier from disk");
						filteredClassifier = (FilteredClassifier) weka.core.SerializationHelper.read(Config.RF_CLASSIFIERPATH);
			}
			
			
			

}
