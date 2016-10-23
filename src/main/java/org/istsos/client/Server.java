package org.istsos.client;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Allows storage and retrieval of services.
 * Can hold data related to name, url, user, password.
 */
public class Server{
	
	private String name;
	private String url;

	private String user;
	private String password;
	private boolean autheticationRequired = false;
	

	private List<Service> services = new ArrayList<>();
	/**
	 * Initialize a Server instance with name and url.
	 * @param name as String
	 * @param url as String
	 */
	public Server(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	/**
	 * Initialize a Server instance with all parameters.
	 * Authentication will be set to true.
	 * @param serverName as String
	 * @param url as String
	 * @param user as String
	 * @param password as String
	 */
	public Server(String serverName, String url, String user, String password) {
		this.name = serverName;
		this.url = url;
		this.setUser(user);
		this.setPassword(password);
		autheticationRequired = true;
	}

	/**
	 * Retrieve a service from the list based on its name
	 * @param serviceName as String
	 * @return Service object where found, else returns null
	 */
	public Service getService(String serviceName){

		for(Service service : this.services){

			if (service.getName().equals(serviceName))
				return service;
		}

		return null;
	}
	/**
	 * Retrieve server name
	 * @return name as String
	 */
	public String getServerName(){
		return this.name;
	}
	/**
	 * Retrieve server url
	 * @return url as String
	 */
	public String getServerUrl(){
		return this.url;
	}
	/**
	 * Retrieve username
	 * @return username as String
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Retrieve password
	 * @return password as String
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Set new username
	 * @param user as String
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * Set new password
	 * @param password as String
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 	Loads all available services from the server
	 */
	public void loadServices(){
		this.loadServices(null);
	}
	
	/**
	 * Loads all available services from the server.
	 * Note: The IstSOSListener callback modifier is set to final.
	 * Every usage of this method will result in a new request to retrieve
	 * services from istSOS.
	 * 
	 * @param callback
	 */
	public void loadServices(final IstSOSListener callback){
		services.clear();
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.url);
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.SERVICE, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        
		        JsonArray data = json.getAsJsonArray("data");
		      
                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();
		        		
		        		System.out.println(object.toString());
		        		
		        		Service service = Service.fromJson(object);
		        		service.setServer(Server.this);
		        		
		        		Server.this.services.add(service);
		        		
		        	}
		        }
		        
		        EventObject eventObject = new EventObject(Event.SERVICE_LOADED, services);
	    		
	    		if(callback != null){
	    			callback.onSuccess(eventObject);
	    		}
			}
			
			@Override
			public void onError(EventObject event) {
				// TODO Auto-generated method stub
				
			}
		}, this.getRealm());
	}
	
	/**
	 * Retrieve list of services
	 * @return List of Service objects
	 */
	public List<Service> getServices() {
		return this.services;
	}
	
	protected ArrayList<String> getRealm(){
		if(autheticationRequired){

			ArrayList<String> realm = new ArrayList<>();

			realm.add(this.getUser());
			realm.add(this.getPassword());

			return realm;

//    		return new Realm.Builder(this.getUser(), this.getPassword())
//				.setUsePreemptiveAuth(true)
//				.setScheme(AuthScheme.BASIC)
//				.build();
    	}
		return null;
	}
	
}