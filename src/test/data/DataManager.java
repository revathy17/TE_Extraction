package test.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.knoesis.classification.RandomForestClassifier;

import test.model.Annotation;
import test.model.Sentence;


public class DataManager {
		
			private Connection connection = null;	
			static Logger log = Logger.getLogger(RandomForestClassifier.class.getName());
		
			public DataManager() {
						try {
								Class.forName("com.mysql.jdbc.Driver");
								connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test_temporal_relationship?user=root&password=admin");			
						} catch(Exception e) {
								e.printStackTrace();
						}
			}
			
			/**
			 * Select documents and sentences to process from database
			 * @param documentId1 Range of the documents to process from the database
			 * @param documentId2 
			 */
			public List<Sentence> getSentences(int documentId1, int documentId2) {
				
						List<Sentence> sentences = new ArrayList<Sentence>();
						String sql = "SELECT document_id, sentence_id, sentence, chunker_output FROM sentences " + 
								     "WHERE document_id BETWEEN " + documentId1 + " AND " + documentId2;
						Statement stmt = null;					
						try {
								stmt = connection.createStatement();
								ResultSet rs = stmt.executeQuery(sql);
								while(rs.next()) {
									int documentId = rs.getInt("document_id");
									int sentenceId = rs.getInt("sentence_id");
									String sent = rs.getString("sentence").trim();
									String chunkerOutput = rs.getString("chunker_output").trim();
									Sentence sentence = new Sentence(documentId, sentenceId, sent, chunkerOutput);
									sentences.add(sentence);
								}			
						
						} catch(Exception e) {
								log.error("Error in retrieving sentences from ",e);			
						} finally {
								cleanUpDatabase(stmt);
						}
						log.info("Processing " + sentences.size() + " Sentences ");				
						return sentences;	
			}
			
			
			/**
			 * 
			 */
			public void insertClassifierAnnotation(int documentId, int sentenceId, int phraseId, String phrase, String teClass) {
						String sql = "INSERT INTO sentences_classifier_annotated(document_id, sentence_id," +
								     " phrase_id, phrase, class) VALUES (?,?,?,?,?)";
						
					
						PreparedStatement ps = null;
						try {
								connection.setAutoCommit(false);
								ps = connection.prepareStatement(sql);
								ps.setInt(1, documentId);
								ps.setInt(2, sentenceId);
								ps.setInt(3, phraseId);
								ps.setString(4, phrase);
								ps.setString(5, teClass);
								ps.execute();
						} catch (Exception e) {
								log.error("Error in inserting classifier annotations: ",e);
						} finally {
								cleanUpDatabase(ps);
						}
			}

			/**
			 * @desc Return the count of correctly annotated phrases, for the given class, from the database
			 *       (TP: True Positives)
			 */
			public int getTruePositives(int documentId1, int documentId2, String annotationClass, boolean strictMatch) {
				
				int count = 0;

				String sql = "SELECT COUNT(sca.document_id) AS count FROM sentences_classifier_annotated sca, " +
							  "sentences_manual_annotated sma" +
						       " WHERE sca.document_id BETWEEN " + documentId1 + " AND " + documentId2 +
							   " AND sca.document_id = sma.document_id " +
						       " AND sca.sentence_id = sma.sentence_id " +							   
							   " AND sca.class LIKE sma.class ";
				if(!strictMatch)
				   	   sql = sql + " AND (sca.phrase LIKE CONCAT('%',sma.phrase,'%') OR sma.phrase LIKE CONCAT('%',sca.phrase,'%')) ";
				else
					   sql = sql + " AND sca.phrase LIKE sma.phrase " ;
						       
				
				if(!annotationClass.equalsIgnoreCase("ALL"))   
					   sql = sql + " AND sca.class LIKE '" + annotationClass + "'";
				else
					   sql = sql + " AND (sca.class LIKE 'DATE' OR sca.class LIKE 'RDT' or sca.class LIKE 'DURATION' or sca.class LIKE 'RT' or sca.class LIKE 'TIME') "; 
				
				Statement stmt = null;		
				try {
					stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					while(rs.next()) {
						count = rs.getInt("count");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					cleanUpDatabase(stmt);
				}
				return count;
				
			}
				
			
			
			/**
			 * Get all the classifier annotation for a class
			 */
			public List<Annotation> getClassifierAnnotation(int documentId1, int documentId2, String teClass) {
						String sql = "SELECT document_id, sentence_id, phrase_id, phrase, class FROM sentences_classifier_annotated " +
									 "WHERE document_id BETWEEN " + documentId1 + " AND " + documentId2;
						
						if(!teClass.equalsIgnoreCase("ALL"))
							 sql = sql + " AND class LIKE '" + teClass + "'";								
						else
						     sql = sql + " AND (class LIKE 'DATE' OR class LIKE 'RDT' OR class LIKE 'DURATION' OR class LIKE 'RT' OR class LIKE 'TIME') "; 
						
						
						Statement stmt = null;
						List<Annotation> classifierAnnotations = new ArrayList<Annotation>();
						try {
								stmt = connection.createStatement();
								ResultSet rs = stmt.executeQuery(sql);
								while(rs.next()) {
									  int documentId = rs.getInt("document_id");
									  int sentenceId = rs.getInt("sentence_id");
									  int phraseId = rs.getInt("phrase_id");
									  String phrase = rs.getString("phrase");
									  String annotationClass = rs.getString("class");
									  Annotation ca = new Annotation(documentId, sentenceId, phraseId, phrase, annotationClass);
									  classifierAnnotations.add(ca);
								}
										
						} catch(Exception e) {
								e.printStackTrace();
						} finally {
								cleanUpDatabase(stmt);
						}
						
						return classifierAnnotations;					
						
			}
			
			/**
			 * Get all the manual annotation for a class
			 */
			public List<Annotation> getManualAnnotation(int documentId1, int documentId2, String teClass) {
						String sql = "SELECT document_id, sentence_id, phrase_id, phrase, class FROM sentences_manual_annotated " +
									 "WHERE document_id BETWEEN " + documentId1 + " AND " + documentId2;
						
						if(!teClass.equalsIgnoreCase("ALL"))
									 sql = sql + " AND class LIKE '" + teClass + "'";
						else
							   		 sql = sql + " AND (class LIKE 'DATE' OR class LIKE 'RDT' OR class LIKE 'DURATION' OR class LIKE 'RT' OR class LIKE 'TIME') "; 
						
						Statement stmt = null;
						List<Annotation> manualAnnotations = new ArrayList<Annotation>();
						try {
								stmt = connection.createStatement();
								ResultSet rs = stmt.executeQuery(sql);
								while(rs.next()) {
									  int documentId = rs.getInt("document_id");
									  int sentenceId = rs.getInt("sentence_id");
									  int phraseId = rs.getInt("phrase_id");
									  String phrase = rs.getString("phrase");
									  String annotationClass = rs.getString("class");
									  Annotation ca = new Annotation(documentId, sentenceId, phraseId, phrase, annotationClass);
									  manualAnnotations.add(ca);
								}
										
						} catch(Exception e) {
								e.printStackTrace();
						} finally {
								cleanUpDatabase(stmt);
						}
						
						return manualAnnotations;					
						
			}
			
			/**
			 * Database cleanup after Select operations
			 */
			private void cleanUpDatabase(Statement stmt) {				
						try {
							connection.close();
							stmt.close();
						} catch (Exception e) {
							log.error("Error cleaning up database after read operation: ", e);
						}				
			}
			
			/**
			 * Database cleanup after Insert operations
			 */
			private void cleanUpDatabase(PreparedStatement ps) {				
						try {
							ps.close();
							connection.commit();
							connection.setAutoCommit(true);
							connection.close();
						} catch (Exception e) {
							log.error("Error cleaning up database after write operation: ", e);
						}				
			}
			
			
			
}
