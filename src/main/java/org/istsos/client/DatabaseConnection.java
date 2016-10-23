package org.istsos.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


/**
 * Used for creating a connection to a database. 
 * Can hold data related to dbname, host, user, password, port.
 * Supports conversion to/from JSON.
 *
 */
@SuppressWarnings("rawtypes")
public class DatabaseConnection implements IstSOSObject{
	
	@SerializedName ("dbname")
	private String dbname;
	
	@SerializedName ("host")
	private String host;
	
	@SerializedName ("user")
	private String user;
	
	@SerializedName ("password")
	private String password;
	
	@SerializedName ("port")
	private String port;
	/**
	 * 
	 * @return dbname as String
	 */
	public String getDbname() {
		return dbname;
	}
	/**
	 * 
	 * @param dbname as String
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	/**
	 * 
	 * @return host as String
	 */
	public String getHost() {
		return host;
	}
	/**
	 * 
	 * @param host as String
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * 
	 * @return username as String
	 */
	public String getUser() {
		return user;
	}
	/**
	 * 
	 * @param user as String
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * 
	 * @return password as String
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 
	 * @param password as String
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 
	 * @return port as String
	 */
	public String getPort() {
		return port;
	}
	/**
	 * 
	 * @param port as String
	 */
	public void setPort(String port) {
		this.port = port;
	}
	

	public JsonObject toJson() {

		Gson gson = new GsonBuilder().create();
		return gson.toJsonTree(this,DatabaseConnection.class).getAsJsonObject();
	}


	static DatabaseConnection fromJson(JsonObject json) {

		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, DatabaseConnection.class);
	}
}