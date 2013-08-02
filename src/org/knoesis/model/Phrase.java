package org.knoesis.model;

/**
 * Use this class to represent each phrase from the chunker output of a sentence
 * @author revathy 
 */

public class Phrase {
		
			int phraseId;
			String phrase;
			String formattedPhrase;
			String classifierAnnotation;
			
			public Phrase(int phraseId, String phrase, String formattedPhrase, String classifierAnnotation) {
					this.phraseId = phraseId;
					this.phrase = phrase;
					this.formattedPhrase = formattedPhrase;
					this.classifierAnnotation = classifierAnnotation;
			}
			
			public int getPhraseId() {
					return this.phraseId;
			}
			
			public String getPhrase() {
					return this.phrase;
			}
			
			public void setPhrase(String phrase) {
					this.phrase = phrase;
			}
			
			public String getFormattedPhrase() {
					return this.formattedPhrase;
			}
			
			public String getClassifierAnnotation() {
					return this.classifierAnnotation;
			}
			
			public void setClassifierAnnotation(String classifierAnnotation) {
					this.classifierAnnotation = classifierAnnotation;
			}
			
			
}
