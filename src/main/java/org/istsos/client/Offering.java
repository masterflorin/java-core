package org.istsos.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * The Offering class represents information such as name, description, visibility status,
 * expiration date, id, and number of procedures
 *
 */
@SuppressWarnings("rawtypes")
public class Offering implements IstSOSObject{
	
	@SerializedName ("name")
	private String name;
	
	@SerializedName ("description")
	private String description;
	
	@SerializedName ("active")
	private boolean status;
	
	@SerializedName ("expiration")
	private String expiration;
	
	@SerializedName ("id")
	private String id;
	
	@SerializedName ("procedures")
	private int procedures;
	
	/**
	 * 
	 * @return String with name
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
	 * @return String with description
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
	 * @return boolean value
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * 
	 * @param status - boolean
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * 
	 * @return String with expiration date
	 */
	public String getExpiration() {
		return expiration;
	}
	/**
	 * 
	 * @param expiration - String
	 */
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	/**
	 * 
	 * @return String with id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id - String
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return int 
	 */
	public int getProcedures() {
		return procedures;
	}
	/**
	 * 
	 * @param procedures - int
	 */
	public void setProcedures(int procedures) {
		this.procedures = procedures;
	}

	
	public JsonObject toJson() {
		// TODO Auto-generated method stub
		JsonObject json = new JsonObject();
		json.addProperty("name", this.getName());
		json.addProperty("description", this.getDescription());
		json.addProperty("active", this.isStatus());
		json.addProperty("expiration", this.getExpiration());
		json.addProperty("id", this.getId());
		json.addProperty("procedures", this.getProcedures());
		return json;
	}


	static Offering fromJson(JsonObject json) {
		// TODO Auto-generated method stub
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Offering.class);
	}
	
}