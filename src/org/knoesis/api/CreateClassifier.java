package org.knoesis.api;

import java.io.File;
import org.apache.log4j.Logger;
import org.knoesis.classification.Classifier;
import org.knoesis.classification.RandomForestClassifier;
import org.knoesis.config.Config;

public class CreateClassifier {
			
		static Logger log = Logger.getLogger(CreateClassifier.class.getName());
		
		/**
		 * Creating a Random Forest Classifier 
		 * The training data is read from data/trainingdata.arff
		 * The classifier is written to : model/RandomForest.model
		 */
		public static void createRFClassifier() throws Exception {
					Classifier classifier = new RandomForestClassifier();			
					/*1. Train the classifier and write it to memory*/
					classifier.trainClassifier(new File(Config.TRAININGDATAFILE));	
					/*2. Write Classifier to the disk*/
					classifier.writeClassifierToDisk();			
		}		
		
}
