package test.evaluation;

import test.data.DataManager;

/**
 * Calculate Precision, Recall and F-Measure for each class {DATE, DURATION, TIME, RT, RDT}
 * @author Revathy
 * @version 1.0
 */

public class Evaluate {
		double truePositives; 
		double falseNegatives;
		double falsePositives;	
		DataManager dm;
	
		public Evaluate(int documentId1, int documentId2, String annotationClass, boolean strictMatch) {
				dm = new DataManager();
				truePositives = (double)dm.getTruePositives(documentId1, documentId2, annotationClass, strictMatch);
				falseNegatives = EvaluateFunctions.getFalseNegatives(documentId1, documentId2, annotationClass, strictMatch);			
				falsePositives = EvaluateFunctions.getFalsePositives(documentId1, documentId2, annotationClass, strictMatch);
			
		}
	
		/**
		 * Calculate Precision = TP/(TP + FP)
		 */
		public double getPrecision() {	
				double p = (truePositives / (truePositives + falsePositives));
				if(Double.isNaN(p))
					return 0.0;
				else
					return p;			
		}	
	
		/**
		 * Calculate Recall = TP / (TP + FN)
		 */
		public double getRecall() {	
				double r = truePositives / (truePositives + falseNegatives);
				if(Double.isNaN(r))
					return 0.0;
				return r;			
		}

		/**
		 * Calculate F-Measure = 2 * [ (Precision * Recall) / (Precision + Recall) ]
		 */
		public double getFMeasure() {			
				double precision = getPrecision();
				double recall = getRecall();
				double fMeasure =  (2 * ( (precision * recall) / (precision + recall) ));
				if(Double.isNaN(fMeasure)) 
					return 0.0;
				else
					return fMeasure;
		}
}
