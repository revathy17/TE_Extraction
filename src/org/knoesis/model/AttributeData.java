package org.knoesis.model;

/**
 * Use this class to construct ARFF data which is the input to the classifier
 * "phrase" is the expression which has to be classified as a temporal expression
 * Other attributes are the features derived from the phrase
 * @author revathy
 *
 */

public class AttributeData {
			
			private String phrase;
			private int hasDay;
			private int hasMonth;
			private int hasRefExpr1;
			private int hasRefExpr2;
			private int hasNumString;
			private int hasTimeRef;
			private int hasDatePattern;
			private int hasTimePattern;
			private int hasTemporalModifier;
			private int hasMeasurementUnit;
			
			public AttributeData(String phrase, int hasDay, int hasMonth, int hasNumString, int hasRefExpr1, 
                    int hasRefExpr2, int hasTimeRef, int hasDatePattern, int hasTimePattern, int hasTemporalModifier, 
                    int hasMeasurementUnit) {
					this.phrase = phrase;
					this.hasDay = hasDay;
					this.hasMonth = hasMonth;
					this.hasNumString = hasNumString;
					this.hasRefExpr1 = hasRefExpr1;
					this.hasRefExpr2 = hasRefExpr2;
					this.hasTimeRef = hasTimeRef;
					this.hasDatePattern = hasDatePattern;
					this.hasTimePattern = hasTimePattern;
					this.hasTemporalModifier = hasTemporalModifier;
					this.hasMeasurementUnit = hasMeasurementUnit;
			}
			
			public String getPhrase() {
					return phrase;
			}
			
			public int getDay() {
					return hasDay;
			}
			
			public int getMonth() {
					return hasMonth;
			}
			
			public int getRefExpr1() {
					return hasRefExpr1;
			}
			
			public int getRefExpr2() {
					return hasRefExpr2;
			}
			
			public int getNumString() {
					return hasNumString;
			}
			
			public int getTimeRef() {
					return hasTimeRef;
			}
			
			public int getDatePattern() {
					return hasDatePattern;
			}
			
			public int getTimePattern() {
					return hasTimePattern;
			}
			
			public int getTemporalModifier() {
					return hasTemporalModifier;
			}
			
			public int getMeasurementUnit() {
					return hasMeasurementUnit;
			}
}
