package org.knoesis.config;

public class Config {

			/** 
			 * Weka FilteredClassifier Options
			 **/
			public static final String[] filteredClassifierOptions = {"-R","first-last","-W","2500","-prune-rate","-1.0","-N","0","-stemmer",
						       "weka.core.stemmers.NullStemmer","-M","1","-tokenizer",
						       "weka.core.tokenizers.WordTokenizer","-delimiters"," \r\n\t.,;:\'\''()?!"};
			
			/**
			 * Classifier file options
			 */
			public static final String UNLABELED_FILE_ARFF = "data/unlabeled_dataset.arff";
			public static final String LABELED_FILE_ARFF = "data/labeled_dataset.arff";
			public static final String LABELED_FILE_CSV = "data/labeled_new.csv";
			public static final String TRAININGDATAFILE = "data/trainingdata.arff";
			public static final String TESTINGDATAFILE = "data/unlabeled_dataset.arff";			
			public static final String RF_CLASSIFIERPATH = "model/RandomForest.model";
			
			/**
			 * SQLite database options
			 */
			public static final String SQLite_DBString = "org.sqlite.JDBC";
			public static final String SQLite_ConnectionString = "jdbc:sqlite:res/ClassifierFeatures";
			
			/**
			 * Phrases to extract from chunker output
			 */
			public static final String[] PHRASES_FROM_CHUNKEROUTPUT = {"NP","QP","ADJP","ADVP"};
			/**
			 * Classes of temporal expressions
			 */
			public static final String[] TEMPORAL_CLASSES = {"NONE","DURATION","DATE","TIME","RDT","RT"};
			
			/**
			 * Temporal Modifiers
			 */
			public static final String[] TEMPORAL_MODIFIERS = {"ago", "after", "later", "prior", "before","earlier"};
			
			/**
			 * RT (Recurring Time) Modifiers
			 */
			public static final String[] RT_MODIFIERS = {"per","times","once","twice","thrice","q"};
			
			/**
			 * Punctuation Marks
			 */
			public static final String[] PUNCTUATION = {".", "?", "!", ",", ";", ":", "&"};
			
			/**
			 * ARFF File information 
			 * CLASS_INDEX_ARFF - location of "class" in the ARFF file. 
			 * Update this whenever you add a feature to the ARFF file.
			 * Example: 'six weeks', 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, DURATION
			 * "DURATION" is the temporal class at index 11
			 * "six weeks" is the phrase at index 0
			 */
			public static final int CLASS_INDEX_ARFF = 11;
			public static final int PHRASE_INDEX_ARFF = 0;
			
}
