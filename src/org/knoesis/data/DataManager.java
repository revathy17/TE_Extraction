package org.knoesis.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.knoesis.classification.RandomForestClassifier;
import org.knoesis.config.Config;
import org.knoesis.model.Feature;

/**
 * Retrieve classifier features from the SQLite database
 * @author revathy
 */

public class DataManager {
		
			Connection connection;
			static Logger log = Logger.getLogger(RandomForestClassifier.class.getName());
			
			public DataManager() throws ClassNotFoundException, SQLException {
					Class.forName(Config.SQLite_DBString);
					connection = DriverManager.getConnection(Config.SQLite_ConnectionString);
					connection.setAutoCommit(false);
			}
			
			/**
			 * Get the list of features from the database
			 * @return List<Feature> List of features from the database
			 * @throws SQLException
			 */
			public List<Feature> getFeatures() throws SQLException {
					String sql = "SELECT type_id, feature_id, type, feature FROM features;"; 
					ResultSet rs = null;
					Statement stmt = null;
					List<Feature> features = new ArrayList<Feature>();
					
					try {
							stmt = connection.createStatement();
							rs = stmt.executeQuery(sql);
							while ( rs.next() ) {
									int typeId = rs.getInt("type_id");
									int featureId = rs.getInt("feature_id");
									String typeName = rs.getString("type");
									String featureName = rs.getString("feature");
									Feature feature = new Feature(typeId, featureId, typeName, featureName);
									features.add(feature);
							}
					} catch (SQLException sqle) {
							throw(sqle);
					} finally {
							cleanUpDatabase(stmt, rs);
					}
					return features;
			}
			
			/**
			 * Clean up the connections
			 * @param stmt
			 * @param rs
			 */
			public void cleanUpDatabase(Statement stmt, ResultSet rs) {
					try {
							rs.close();
							stmt.close();
							connection.close();
					} catch (Exception e) {
							log.error("Error in clean up of SQLite Database Connection", e);
					}
			}
			
			
}
