package org.istsos.client.observation;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * The DataArray represents the values of the observation.
 *
 */
public class DataArray {
	
	@SerializedName ("values")
	private ArrayList<String[]> values = new ArrayList<String[]>();
	
	/**
	 * 
	 * @return list of String array
	 */
	public ArrayList<String[]> getValues() {
		return values;
	}
	/**
	 * 
	 * @param values - {@link ArrayList}
	 */
	public void setValues(ArrayList<String[]> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "values: " + values;
	}

	static DataArray fromJson(JsonObject json) {
		// TODO Auto-generated method stub
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, DataArray.class);
		
	}
	
	
}
