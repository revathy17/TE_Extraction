package org.knoesis.model;

public class Feature {
			private int type_id;
			private int feature_id;
			private String typeName;
			private String featureName;
			 
			public Feature(int type_id, int feature_id, String typeName, String featureName) {
					this.type_id = type_id;
					this.feature_id = feature_id;
					this.typeName = typeName;
					this.featureName = featureName;
			}
			
			public int getTypeId() {
					return type_id;
			}
			
			public int getFeatureId() {
					return feature_id;
			}
			
			public String getTypeName() {
					return typeName;
			}
			
			public String getFeatureName() {
					return featureName;
			}
}
