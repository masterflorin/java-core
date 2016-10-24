package org.istsos.client;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

/**
 * ObservedProperty class represents information such as definition URN, name,
 * description, unit of measure, and procedure names.
 */
@SuppressWarnings("rawtypes")
public class ObservedProperty implements IstSOSObject {
	
	@SerializedName ("definition")
	private String definition;
	
	transient private ArrayList<String> procedures = new ArrayList<String>();
	@SerializedName ("name")
	private String name;
	@SerializedName ("description")
	private String description;
	@SerializedName("uom")
	private String uom;
	
	/**
	 * 
	 * @return definition URN as String
	 */
	public String getDefinition() {
		return definition;
	}
	/**
	 * 
	 * @param definition - String
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	/**
	 * 
	 * @return list of procedure names
	 */
	public ArrayList<String> getProcedures() {
		return procedures;
	}
	/**
	 * 
	 * @param procedures - List of string
	 */
	public void setProcedures(ArrayList<String> procedures) {
		this.procedures = procedures;
	}
	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name - String
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return description 
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 
	 * @param description - String
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 
	 * @return unit of measure as String
	 */
	public String getUom() {
		return uom;
	}
	/**
	 * 
	 * @param uom - String
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}

	@Override
	public String toString() {
		return "Name: " + this.name + " definition: " + this.definition;
	}

	public JsonObject toJson() {
		// TODO Auto-generated method stub
		JsonObject json = new JsonObject();
		json.addProperty("definition", this.getDefinition());
		json.addProperty("description", this.getDescription());
		json.addProperty("name", this.getName());
		
		//convert procedures list into json element
		Gson gson = new GsonBuilder().create();
		JsonElement procedures = gson.toJsonTree(this.getProcedures(), new TypeToken<ArrayList<String>>(){}.getType());
		json.add("procedures", procedures);
		
		return json;
		
	}

	static ObservedProperty fromJson(JsonObject json) {
		// TODO Auto-generated method stub
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, ObservedProperty.class);
	}
}