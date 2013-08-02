package test.model;

public class Sentence {

				int documentId;
				int sentenceId;
				String documentName;
				String sentence;
				String chunkerOutput;
				int annotationId;
				
				public Sentence(int documentId, int sentenceId, String sentence, String chunkerOutput) {
						this.documentId = documentId;
						this.sentenceId = sentenceId;
						this.documentName = "";
						this.sentence = sentence;
						this.chunkerOutput = chunkerOutput;
						this.annotationId = -1;
				}
				
				public int getDocumentId() {
						return documentId;
				}
				
				public int getSentenceId() {
						return sentenceId;
				}
				
				public void setDocumentName(String documentName) {
						this.documentName = documentName; 
				}
				public String getDocumentName() {
						return documentName;
				}
				public String getSentence() {
						return sentence;
				}
				
				public String getChunkerOutput() {
						return chunkerOutput;
				}
				
				public int getAnnotationId() {
						return annotationId;
				}
}
