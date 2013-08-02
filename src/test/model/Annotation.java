package test.model;

public class Annotation {
		
			int documentId;
			int sentenceId;
			int phraseId;
			String phrase;
			String teClass;
			
			public Annotation(int documentId, int sentenceId, int phraseId, String phrase, String teClass) {
					this.documentId = documentId;
					this.sentenceId = sentenceId;
					this.phraseId = phraseId;
					this.phrase = phrase;
					this.teClass = teClass;
			}
			
			public int getDocumentId() {
					return this.documentId;
			}
			
			public int getSentenceId() {
					return this.sentenceId;
			}
			
			public int getPhraseId() {
					return this.phraseId;
			}
			
			public String getPhrase() {
					return this.phrase;
			}
			
			public String getTeClass() {
					return this.teClass;
			}
			
}
