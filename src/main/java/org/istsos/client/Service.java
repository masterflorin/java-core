package org.istsos.client;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Handles all requests for IstSOS data instances.
 * <p>
 * Enables capabilities for working with offerings, procedures, virtual procedures,
 * observed properties, unit of measure, data qualities, and observations. Holds
 * data classes in lists.
 */
public class Service implements IstSOSObject<Service>{

	@SerializedName("service")
	private String name;
	@SerializedName("path")
	private String configPath;
	
	private Server server;

    private ArrayList<Offering> offerings = new ArrayList<>();
    private ArrayList<Procedure> procedures = new ArrayList<>();
    private ArrayList<VirtualProcedure> virtualProcedures = new ArrayList<>();
	private ArrayList<ObservedProperty> observedProperties = new ArrayList<>();
	private ArrayList<UnitOfMeasure> uoms = new ArrayList<>();
	private ArrayList<DataQuality> dataQualities = new ArrayList<>();
	private ArrayList<Observation> observations = new ArrayList<>();
	
	public Service() {};
	/**
	 * Initialize Service on the given Server.
	 * @param server {@link Server}
	 */
	public Service(Server server) {
		this.server = server;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getConfigPath() {
		return configPath;
	}
	/**
	 * Set new configuration path
	 * @param configPath as String
	 */
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}
	/**
	 * 
	 * @param {@link Server} server 
	 */
	public void setServer(Server server){
		this.server = server;
	}
	/**
	 * Retrieve a procedure based on its name.
	 * @param procedureName as String
	 * @return {@link Procedure} instance if found, else returns null.
	 */
	public Procedure getProcedure(String procedureName){

        for(Procedure proc : this.procedures){

            if(proc.getSystem().equals(procedureName))
                return proc;
        }

        return null;

	}
	/**
	 * Retrieve an offering based on its name.
	 * @param offeringName as String
	 * @return {@link Offering} instance if found, else returns null.
	 */
	public Offering getOffering(String offeringName){
		
		for(Offering offer : this.offerings){
			
			if(offer.getName().equals(offeringName))
				return offer;
		}
		
		return null;
		
	}
	/**
	 * Retrieve an observedProperty based on its definition URN.
	 * @param defUrn as String
	 * @return {@link ObservedProperty} instance if found, else returns null.
	 */
	public ObservedProperty getObservedProperty(String defUrn){
		
		for(ObservedProperty obsProp : this.observedProperties){
			
			if(obsProp.getDefinition().equals(defUrn))
				return obsProp;
			
		}
		
		return null;
		
	}
	/**
	 * Retrieve a data quality based on its code.
	 * @param code as int
	 * @return {@link DataQuality} instance if found, else returns null.
	 */
	public DataQuality getDataQuality(int code){
		
		for(DataQuality dataQuality : this.dataQualities){
			
			if(dataQuality.getDataQualityCode() == code)
				return dataQuality;
		}
		
		return null;
	}
	/**
	 * Retrieve a virtual procedure based on its name.
	 * @param procedureName as String
	 * @return {@link VirtualProcedure} instance if found, else returns null.
	 */
	public VirtualProcedure getVirtualProcedure(String procedureName){
		
		for(VirtualProcedure proc : this.virtualProcedures){
			
			if(proc.getSystem().equals(procedureName))
				return proc;
		}
		
		return null;
	}
	/**
	 * Retrieve unit of measure based on its name.
	 * @param uomName as String
	 * @return {@link UnitOfMeasure} instance if found, else returns null.
	 */
	public UnitOfMeasure getUom(String uomName){
		
		for(UnitOfMeasure uom : this.getUnitsOfMeasure()){
			
			if(uom.getUnitName().equals(uomName))
				return uom;
		}
		
		return null;
	}
	
	
	/**
	 * Default method for loading connection to Database
	 */
	public void loadDatabase(){
		this.loadDatabase(null);
	}

	/**
	 * Opens new connection to the Database.
	 * @param callback
	 */
	public void loadDatabase(final IstSOSListener callback){

		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.DATABASE, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				
				// Convert response in DataBase objects
				DatabaseConnection databaseConnection = DatabaseConnection.fromJson(object);
				
				System.out.println(databaseConnection.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.DATABASE_LOADED, databaseConnection);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for testing database connection
	 * @param database as {@link DatabaseConnection}
	 */
	public void validateDatabase(DatabaseConnection databaseConnection) {
		this.validateDatabase(databaseConnection, null);
	}
	/**
	 * Validate the database connection.
	 * @param database as {@link DatabaseConnection}
	 * @param callback as IstSOSListener
	 */
	public void validateDatabase(DatabaseConnection database, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		
		String data = gson.toJson(database);
		
		IstSOS.executePost(Requests.getUrl(Requests.Request.VALIDATE_DATABASE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				
				// Convert response in DataBase objects
				DatabaseConnection databaseConnection = DatabaseConnection.fromJson(object);
				
				System.out.println(databaseConnection.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.DATABASE_VALIDATED, databaseConnection);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for loading service provider
	 */
	public void loadProvider(){
		this.loadProvider(null);
	}
	/**
	 * Retrieve service provider from the Service.
	 * 
	 * @param callback as IstSOSListener
	 */
	public void loadProvider(final IstSOSListener callback){

		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.PROVIDER, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				
				// Convert response in Provider objects
				Provider provider = Provider.fromJson(object);
				
				System.out.println(provider.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.PROVIDER_LOADED, provider);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for updating service provider
	 * @param provider
	 */
	public void updateProvider(Provider provider){
		this.updateProvider(provider, null);
	}
	/**
	 * Update service provider based on a Provider instance.
	 * @param provider as {@link Provider}
	 * @param callback as IstSOSListener
	 */
	public void updateProvider(Provider provider, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		
		String data = gson.toJson(provider);
		
		IstSOS.executePut(Requests.getUrl(Requests.Request.PROVIDER, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				
				// Convert response in Provider objects
				Provider provider = Provider.fromJson(object);
				
				System.out.println(provider.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.PROVIDER_UPDATED, provider);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for retrieving an observation object by specifying an offering, a single procedure, a observed property, as well
	 * as the time interval.
	 * @param offering as {@link Offering}
	 * @param procedure as {@link Procedure}
	 * @param defUrn as {@link ObservedProperty}
	 * @param beginPosition as {@link Date}
	 * @param endPosition as {@link Date}
	 */
	public void getObservation(Offering offering, Procedure procedure, ObservedProperty defUrn, 
			Date beginPosition, Date endPosition){
		this.getObervation(offering, procedure, defUrn, beginPosition, endPosition, null);
	}
	/**
	 * Retrieve an observation object by specifying an offering, a single procedure, a observed property, as well
	 * as the time interval.
	 * <p>
	 * Note: Date objects will be formatted according to the specs used by istSOS, "yyyy-MM-dd'T'HH:mm:ssXX".
	 * @param offering as {@link Offering}
	 * @param procedure as {@link Procedure}
	 * @param defUrn as {@link ObservedProperty}
	 * @param beginPosition as {@link Date}
	 * @param endPosition as {@link Date}
	 * @param callback as {@link IstSOSListener}
	 */
	public void getObervation(Offering offering, Procedure procedure, ObservedProperty defUrn, 
			Date beginPosition, Date endPosition, final IstSOSListener callback){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX");

		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("offering", offering.getName());
		urlKeyMap.put("procedure", procedure.getSystem());
		urlKeyMap.put("definition", defUrn.getDefinition());
		urlKeyMap.put("begin_position", formatter.format(beginPosition)); //beginPosition.toString());
		urlKeyMap.put("end_position", formatter.format(endPosition)); //endPosition.toString());

		IstSOS.executeGet(Requests.getUrl(Requests.Request.GETOBSERVATION, urlKeyMap), new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject data = json.getAsJsonObject("data");
				//convert to observation object
				Observation observation = Observation.fromJson(data);
				System.out.println(observation.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.OBSERVATION_LOADED, observation);

				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			@Override
			public void onError(EventObject event) {
				
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for loading Observation based by specifying multiple procedures, 
	 * multiple observed properties, an offering, as well as the interval of time.
	 * Note: Date objects will be formatted according to the specs used by istSOS, "yyyy-MM-dd'T'HH:mm:ssXX".
	 * @param offering as {@link Offering}
	 * @param procedure as {@link List}<{@link Procedure}>
	 * @param defUrn as {@link List}<{@link ObservedProperty}>
	 * @param beginPosition as {@link Date}
	 * @param endPosition as {@link Date}
	 */
	public void getObservation(Offering offering, List<Procedure> procedure, 
					List<ObservedProperty> defUrn, Date beginPosition, Date endPosition){
		this.getObervation(offering, procedure, defUrn, beginPosition, endPosition, null);
	}
	/**
	 * Loads Observations by doing a request based on multiple procedures and observed properties.
	 * <p>
	 * Note: Date objects will be formatted according to the specs used by istSOS, "yyyy-MM-dd'T'HH:mm:ssXX".
	 * @param offering as {@link Offering}
	 * @param procedure as {@link List}<{@link Procedure}>
	 * @param defUrn as {@link List}<{@link ObservedProperty}>
	 * @param beginPosition as {@link Date}
	 * @param endPosition as {@link Date}
	 * @param callback as {@link IstSOSListener}
	 */
	public void getObervation(Offering offering, List<Procedure> procedure, 
					List<ObservedProperty> defUrn, Date beginPosition, Date endPosition, final IstSOSListener callback){
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX");
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		
		String procedureNames = new String();
		//get all procedure names
		for(Procedure proc : procedure){
			procedureNames += proc.getSystem() + "%2C";	//encode comma
		}
		
		String urns = new String();
		//get all def urns
		for(ObservedProperty urn : defUrn){
			urns += urn.getDefinition() + "%2C";	//encode comma
		}
		
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("offering", offering.getName());
		urlKeyMap.put("procedure", procedureNames);
		urlKeyMap.put("definition", urns);
		urlKeyMap.put("begin_position", formatter.format(beginPosition));
		urlKeyMap.put("end_position", formatter.format(endPosition));
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.GETOBSERVATION, urlKeyMap), new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        JsonArray data = json.getAsJsonArray("data");

                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();
		        		
		        		//converto observation object
						Observation observation = Observation.fromJson(object);

                        Service.this.observations.add(observation);
		        	}
		        }
				
		        EventObject eventObject = new EventObject(
		        		Event.OBSERVATION_LOADED, observations);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			@Override
			public void onError(EventObject event) {
				
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Retrieve a list of Observations
	 * @return List of Observations
	 */
	public ArrayList<Observation> getObservations() {
		return observations;
	}
	/**
	 * Default method for describing sensor properties based on procedure name
	 * @param procedureName as String
	 */
	public void describeSensor(String procedureName){
		this.describeSensor(procedureName, null);
	}
	/**
	 * Describe sensor properties by specifying procedure name.
	 * 
	 * @param procedureName as String
	 * @param callback as IstSOSListener
	 */
	public void describeSensor(String procedureName, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", procedureName);

		IstSOS.executeGet(Requests.getUrl(Requests.Request.PROCEDURE, urlKeyMap), new IstSOSListener() {

			@Override
			public void onSuccess(EventObject event) {

				JsonObject json = (JsonObject) event.getObject();
				JsonObject data = json.getAsJsonObject("data");

				System.out.println(data);

				Procedure procedure = Procedure.fromJson(data);

				EventObject eventObject = new EventObject(Event.PROCEDURES_LOADED, procedure);

				if(callback != null){
					callback.onSuccess(eventObject);
				}
			}

			@Override
			public void onError(EventObject event) {
				//

			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for registering a sensor
	 * @param procedure
	 */
	public void registerSensor(Procedure procedure) {
		this.registerSensor(procedure, null);
	}
	/**
	 * Registers new sensor on the service.
	 * @param procedure as Procedure
	 * @param callback as IstSOSListener
	 */
	public void registerSensor(Procedure procedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		//convert procedure to json
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(procedure, Procedure.class);
		
		IstSOS.executePost(Requests.getUrl(Requests.Request.PROCEDURE, urlKeyMap), data, new IstSOSListener() {

			@Override
			public void onSuccess(EventObject event) {

				JsonObject json = (JsonObject) event.getObject();
				JsonObject data = json.getAsJsonObject("data");

				System.out.println(data);

				Procedure procedure = Procedure.fromJson(data);

				EventObject eventObject = new EventObject(Event.PROCEDURE_REGISTERED, procedure);

				if(callback != null){
					callback.onSuccess(eventObject);
				}
			}

			@Override
			public void onError(EventObject event) {
				//

			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for inserting observation.
	 * @param procedure {@link Procedure}
	 * @param observation {@link Observation}
	 */
	public void insertObservation(Procedure procedure, Observation observation){
		this.insertObservation(procedure, observation, null);
	}
	/**
	 * Insert Observation to service.
	 * @param procedure {@link Procedure}
	 * @param observation {@link Observation}
	 * @param callback {@link IstSOSListener}
	 */
	public void insertObservation(Procedure procedure, Observation observation, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(observation, Observation.class);

		IstSOS.executePost(Requests.getUrl(Requests.Request.INSERT_OBSERVATION, urlKeyMap), data, new IstSOSListener() {

			@Override
			public void onSuccess(EventObject event) {

				JsonObject json = (JsonObject) event.getObject();
				JsonObject data = json.getAsJsonObject("data");

				System.out.println(data);

				Procedure procedure = Procedure.fromJson(data);

				EventObject eventObject = new EventObject(Event.OBSERVATION_INSERTED, procedure);

				if(callback != null){
					callback.onSuccess(eventObject);
				}
			}

			@Override
			public void onError(EventObject event) {
				//

			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for loading Status
	 */
	public void loadStatus() {
		this.loadStatus(null);
	}
	/**
	 * Load status of the service.
	 * @param callback IstSOSListener
	 */
	public void loadStatus(final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());

		IstSOS.executeGet(Requests.getUrl(Requests.Request.STATUS, urlKeyMap), new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				
				JsonObject json = (JsonObject) event.getObject();
		        JsonArray data = json.getAsJsonArray("data");
		        Status status = new Status();
		        
                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();
		        		
		        		//convert to observation object
						status = Status.fromJson(object);
						System.out.println(status.toJson().toString());
		        	}
		        }
				
		        EventObject eventObject = new EventObject(
		        		Event.STATUS_LOADED, status);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Load existing data qualities from service. 
	 * Default method used without parameter.
	 */
	public void loadDataQualities(){
		this.loadDataQualities(null);
	}
	/**
	 * Load existing data qualities from service. 
	 * 
	 * @param callback IstSOSListener
	 */
	public void loadDataQualities(final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.DATA_QUALITIES, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        
		        JsonArray data = json.getAsJsonArray("data");
		        
                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();

						DataQuality dataQuality = DataQuality.fromJson(object);

                        Service.this.dataQualities.add(dataQuality);
		        	}
		        }
		        
		        EventObject eventObject = new EventObject(Event.DATA_QUALITIES_LOADED, dataQualities);
	    		
	    		if(callback != null){
	    			callback.onSuccess(eventObject);
	    		}
			}
			
			@Override
			public void onError(EventObject event) {
				//   
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Retrieve data qualities
	 * @return List of data qualities
	 */
	public ArrayList<DataQuality> getDataQualities() {
		return dataQualities;
	}
	/**
	 * Default method for registering a data quality to selected service.
	 * @param dataQuality
	 */
	public void registerDataQuality(DataQuality dataQuality){
		this.registerDataQuality(dataQuality, null);
	}
	/**
	 * Register new data quality to the service.
	 * @param dataQuality {@link DataQuality}
	 * @param callback {@link IstSOSListener}
	 */
	public void registerDataQuality(DataQuality dataQuality, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(dataQuality);

		IstSOS.executePost(Requests.getUrl(Requests.Request.DATA_QUALITIES, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.DATA_QUALITY_REGISTERED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for updating data quality characteristics.
	 * @param dataQuality
	 */
	public void updateDataQuality(DataQuality dataQuality){
		this.updateDataQuality(dataQuality, null);
	}
	/**
	 * Update a data quality's characteristics (code, name, description). 
	 * @param dataQuality {@link DataQuality}
	 * @param callback {@link IstSOSListener}
	 */
	public void updateDataQuality(DataQuality dataQuality, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", String.valueOf(dataQuality.getDataQualityCode()));
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(dataQuality);

		IstSOS.executePut(Requests.getUrl(Requests.Request.DATA_QUALITIES, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				
				DataQuality dataQuality = DataQuality.fromJson(object);
				
				System.out.println(dataQuality.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.DATA_QUALITY_UPDATED, dataQuality);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for removing data quality from service.
	 * @param dataQuality
	 */
	public void removeDataQuality(DataQuality dataQuality){
		this.removeDataQuality(dataQuality, null);
	}
	/**
	 * Remove data quality from service.
	 * @param dataQuality {@link DataQuality}
	 * @param callback {@link IstSOSListener}
	 */
	public void removeDataQuality(DataQuality dataQuality, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", String.valueOf(dataQuality.getDataQualityCode()));
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(dataQuality);

		IstSOS.executeDelete(Requests.getUrl(Requests.Request.DATA_QUALITIES, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.getAsString();
				
				System.out.println(message.toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.DATA_QUALITY_REMOVED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for loading observed properties from selected service.
	 */
	public void loadObservedProperties(){
		this.loadObservedProperties();
	}
	/**
	 * Load observed properties from selected service.
	 * @param callback as {@link IstSOSListener}
	 */
	public void loadObservedProperties(final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.OBSERVED_PROPERTIES, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        
		        JsonArray data = json.getAsJsonArray("data");
		        
                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();

						ObservedProperty obsProperty = ObservedProperty.fromJson(object);

                        Service.this.observedProperties.add(obsProperty);
		        	}
		        }
		        
		        EventObject eventObject = new EventObject(Event.OBSERVED_PROPERTIES_LOADED, observedProperties);
	    		
	    		if(callback != null){
	    			callback.onSuccess(eventObject);
	    		}
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Retrieve observed properties.
	 * @return List of observed properties
	 */
	public ArrayList<ObservedProperty> getObservedProperties() {
		return observedProperties;
	}
	/**
	 * Default method for registering observed property to service.
	 * @param obs - {@link ObservedProperty}
	 */
	public void registerObservedProperty(ObservedProperty obs){
		this.registerObservedProperty(obs, null);
	}
	/**
	 * Register new observed property to service.
	 * @param observedProperty - {@link ObservedProperty}
	 * @param callback - {@link IstSOSListener}
	 */
	public void registerObservedProperty(ObservedProperty observedProperty, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(observedProperty);

		IstSOS.executePost(Requests.getUrl(Requests.Request.OBSERVED_PROPERTIES, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.OBSERVED_PROPERTY_REGISTERED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for updating selected Observed Property.
	 * @param observedProperty - {@link ObservedProperty}
	 */
	public void updateObservedProperty(ObservedProperty observedProperty){
		this.updateObservedProperty(observedProperty, null);
	}
	/**
	 * Update observed property characteristics.
	 * @param observedProperty - {@link ObservedProperty}
	 * @param callback - {@link IstSOSListener}
	 */
	public void updateObservedProperty(ObservedProperty observedProperty, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", observedProperty.getDefinition());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(observedProperty);

		IstSOS.executePut(Requests.getUrl(Requests.Request.OBSERVED_PROPERTIES, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				// Convert response in observed property type object
				JsonObject object = json.get("data").getAsJsonObject();

				ObservedProperty obsProperty = ObservedProperty.fromJson(object);
				
				System.out.println(obsProperty.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.OBSERVED_PROPERTY_UPDATED, obsProperty);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for removing observed property.
	 * @param observedProperty - {@link ObservedProperty}
	 */
	public void removeObservedProperty(ObservedProperty observedProperty){
		this.removeObservedProperty(observedProperty, null);
	}
	/**
	 * Remove observed property from service.
	 * @param observedProperty - {@link ObservedProperty}
	 * @param callback - {@link IstSOSListener}
	 */
	public void removeObservedProperty(ObservedProperty observedProperty, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", observedProperty.getDefinition());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(observedProperty);

		IstSOS.executeDelete(Requests.getUrl(Requests.Request.OBSERVED_PROPERTIES, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.OBSERVED_PROPERTY_REMOVED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for loading units of measure from service.
	 */
	public void loadUnitsOfMeasure(){
		this.loadUnitsOfMeasure(null);
	}
	/**
	 * Load units of measure from service.
	 * @param callback - {@link IstSOSListener}
	 */
	public void loadUnitsOfMeasure(final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.UOMS, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        
		        JsonArray data = json.getAsJsonArray("data");
		        
                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();

						UnitOfMeasure uom = UnitOfMeasure.fromJson(object);

                        Service.this.uoms.add(uom);
		        	}
		        }
		        
		        EventObject eventObject = new EventObject(Event.UOMS_LOADED, uoms);
	    		
	    		if(callback != null){
	    			callback.onSuccess(eventObject);
	    		}
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Retrieve units of measure of list from service.
	 * @return List with units of measure
	 */
	public ArrayList<UnitOfMeasure> getUnitsOfMeasure() {
		return uoms;
	}
	/**
	 * Default method for registering unit of measure to service
	 * @param uom - {@link UnitOfMeasure}
	 */
	public void registerUnitOfMeasure(UnitOfMeasure uom){
		this.registerUnitOfMeasure(uom, null);
	}
	/**
	 * Register unit of measure to service.
	 * @param uom - {@link UnitOfMeasure}
	 * @param callback - {@link IstSOSListener}
	 */
	public void registerUnitOfMeasure(UnitOfMeasure uom, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(uom);

		IstSOS.executePost(Requests.getUrl(Requests.Request.UOMS, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.UOM_REGISTERED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for updating selected unit of measure.
	 * @param uom - {@link IstSOSListener}
	 */
	public void updateUnitOfMeasure(UnitOfMeasure uom){
		this.updateUnitOfMeasure(uom, null);
	}
	/**
	 * Update selected unit of measure.
	 * @param uom - {@link UnitOfMeasure}
	 * @param callback - {@link IstSOSListener}
	 */
	public void updateUnitOfMeasure(UnitOfMeasure uom, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", uom.getUnitName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(uom);

		IstSOS.executePut(Requests.getUrl(Requests.Request.UOMS, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				// Convert response in unit of measure type object
				
				JsonObject object = json.get("data").getAsJsonObject();

				UnitOfMeasure response_uom = UnitOfMeasure.fromJson(object);
				
				System.out.println(response_uom.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.UOM_UPDATED, response_uom);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	/**
	 * Default method for removing unit of measure
	 * @param uom
	 */
	public void removeUnitOfMeasure(UnitOfMeasure uom){
		this.removeUnitOfMeasure(uom, null);
	}
	/**
	 * Remove unit of measure from service.
	 * @param uom - {@link UnitOfMeasure}
	 * @param callback - {@link IstSOSListener}
	 */
	public void removeUnitOfMeasure(UnitOfMeasure uom, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", uom.getUnitName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(uom);

		IstSOS.executeDelete(Requests.getUrl(Requests.Request.UOMS, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.UOM_REMOVED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	/**
	 * Default method for loading procedures from service
	 */
	public void loadProcedures(){
		this.loadProcedures(null);
	}
	/**
	 * Load procedures from service.
	 * @param callback - {@link IstSOSListener}
	 */
	public void loadProcedures(final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.PROCEDURES, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        JsonArray data = json.getAsJsonArray("data");


                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();

						Procedure procedure = Procedure.fromJson(object);

                        Service.this.procedures.add(procedure);
		        	}
		        }
		        
		        EventObject eventObject = new EventObject(Event.PROCEDURES_LOADED, procedures);
	    		
	    		if(callback != null){
	    			callback.onSuccess(eventObject);
	    		}
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Retrieve a list of procedures
	 * @return List of procedures.
	 */
	public ArrayList<Procedure> getProcedures() {
		return this.procedures;
	}
	/**
	 * Default method for registering procedure
	 * @param procedure
	 */
	public void registerProcedure(Procedure procedure){
		this.registerProcedure(procedure, null);
	}
	
	
	/**
	 * Retrieve procedure based by name from service
	 * @param procedureName
	 */
	public void getProcedure(String procedureName, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", procedureName);

		IstSOS.executeGet(Requests.getUrl(Requests.Request.PROCEDURE, urlKeyMap), new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
			
				Procedure response_procedure = Procedure.fromJson(object);
				System.out.println(response_procedure.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.PROCEDURE_LOADED, response_procedure);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Register procedure to selected service
	 * @param procedure
	 * @param callback
	 */
	public void registerProcedure(Procedure procedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(procedure);

		IstSOS.executePost(Requests.getUrl(Requests.Request.PROCEDURE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.PROCEDURE_REGISTERED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for updating procedure.
	 * @param procedure
	 */
	public void updateProcedure(Procedure procedure){
		this.updateProcedure(procedure, null);
	}
	
	/**
	 * Update selected procedure
	 * @param procedure
	 * @param callback
	 */
	public void updateProcedure(Procedure procedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", procedure.getSystem());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(procedure);

		IstSOS.executeDelete(Requests.getUrl(Requests.Request.PROCEDURE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				Procedure response_procedure = Procedure.fromJson(json);
				
				System.out.println(response_procedure.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.PROCEDURE_UPDATED, response_procedure);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for loading virtual procedures from service
	 */
	public void loadVirtualProcedures(){
		this.loadVirtualProcedures(null);
	}
	
	/**
	 * Load virtual procedures from service
	 * @param callback
	 */
	public void loadVirtualProcedures(final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        
		        JsonArray data = json.getAsJsonArray("data");
		        
                for(JsonElement element : data){
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();

						VirtualProcedure virtualProcedure = VirtualProcedure.fromJson(object);

                        Service.this.virtualProcedures.add(virtualProcedure);
		        	}
		        }
		        
		        EventObject eventObject = new EventObject(Event.VIRTUAL_PROCEDURES_LOADED, virtualProcedures);
	    		
	    		if(callback != null){
	    			callback.onSuccess(eventObject);
	    		}
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	public ArrayList<VirtualProcedure> getVirtualProcedures() {
		return virtualProcedures;
	}
	
	/**
	 * Default method for loading virtual procedure code
	 * @param virtualProcedure
	 */
	public void loadVirtualProcedureCode(VirtualProcedure virtualProcedure){
		this.loadVirtualProcedureCode(virtualProcedure, null);
	}
	
	/**
	 * Load virtual procedure code
	 * @param virtualProcedure
	 * @param callback
	 */
	public void loadVirtualProcedureCode(VirtualProcedure virtualProcedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", virtualProcedure.getName());

		IstSOS.executeGet(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES_CODE, urlKeyMap), new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				
				VirtualProcedure virtualProcedure = VirtualProcedure.fromJson(object);
				
				System.out.println(virtualProcedure.toJson().toString());
				
		        EventObject eventObject = new EventObject(Event.VIRTUAL_PROCEDURE_CODE_LOADED, virtualProcedure);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for registering virtual procedure code
	 * @param virtualProcedure
	 */
	public void registerVirtualProcedureCode(VirtualProcedure virtualProcedure){
		this.registerVirtualProcedureCode(virtualProcedure, null);
	}
	
	/**
	 * Register virtual procedure code
	 * @param virtualProcedure
	 * @param callback
	 */
	public void registerVirtualProcedureCode(VirtualProcedure virtualProcedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", virtualProcedure.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(virtualProcedure);

		IstSOS.executePost(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES_CODE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.VIRTUAL_PROCEDURE_CODE_REGISTERED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for updating virtual procedure code
	 * @param virtualProcedure
	 */
	public void updateVirtualProcedureCode(VirtualProcedure virtualProcedure){
		this.updateVirtualProcedureCode(virtualProcedure, null);
	}
	
	/**
	 * Update virtual procedure code
	 * @param virtualProcedure
	 * @param callback
	 */
	public void updateVirtualProcedureCode(VirtualProcedure virtualProcedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", virtualProcedure.getSystem());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(virtualProcedure);

		IstSOS.executePut(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES_CODE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				
				// Convert response in virtual procedure type object
				VirtualProcedure response_vProcedure = VirtualProcedure.fromJson(object);
				
				System.out.println(response_vProcedure.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.VIRTUAL_PROCEDURE_CODE_UPDATED, response_vProcedure);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for removing virtual procedure code
	 * @param virtualProcedure
	 */
	public void removeVirtualProcedureCode(VirtualProcedure virtualProcedure){
		this.removeVirtualProcedureCode(virtualProcedure, null);
	}
	
	/**
	 * Remove virtual procedure code
	 * @param virtualProcedure
	 * @param callback
	 */
	public void removeVirtualProcedureCode(VirtualProcedure virtualProcedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", virtualProcedure.getSystem());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(virtualProcedure);

		IstSOS.executeDelete(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES_CODE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.getAsString();
				
				System.out.println(json.toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.VIRTUAL_PROCEDURE_CODE_REMOVED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for loading virtual procedure rating curve
	 * @param virtualProcedure
	 */
	public void loadVirtualProcedureRatingCurve(VirtualProcedure virtualProcedure){
		this.loadVirtualProcedureRatingCurve(virtualProcedure, null);
	}
	
	public void loadVirtualProcedureRatingCurve(VirtualProcedure virtualProcedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", virtualProcedure.getName());

		IstSOS.executeGet(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES_RATINGCURVE, urlKeyMap), new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				JsonObject object = json.get("data").getAsJsonObject();
				VirtualProcedure virtualProcedure = VirtualProcedure.fromJson(object);
				
				System.out.println(virtualProcedure.toJson().toString());
				
		        EventObject eventObject = new EventObject(Event.VIRTUAL_PROCEDURE_RATINGCURVE_LOADED, virtualProcedure);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for registering rating curve
	 * @param virtualProcedure
	 */
	public void registerVirtualProcedureRatingCurve(VirtualProcedure virtualProcedure){
		this.registerVirtualProcedureRatingCurve(virtualProcedure, null);
	}
	
	/**
	 * Register rating curve for virtual procedure
	 * @param virtualProcedure
	 * @param callback
	 */
	public void registerVirtualProcedureRatingCurve(VirtualProcedure virtualProcedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", virtualProcedure.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(virtualProcedure);

		IstSOS.executePost(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES_RATINGCURVE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.VIRTUAL_PROCEDURE_CODE_REGISTERED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for removing rating curve
	 * 
	 * @param virtualProcedure
	 */
	public void removeVirtualProcedureRatingCurve(VirtualProcedure virtualProcedure){
		this.removeVirtualProcedureCode(virtualProcedure, null);
	}
	
	/**
	 * Remove rating curve from virtual procedure
	 * @param virtualProcedure
	 * @param callback
	 */
	public void removeVirtualProcedureRatingCurve(VirtualProcedure virtualProcedure, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", virtualProcedure.getSystem());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(virtualProcedure);

		IstSOS.executeDelete(Requests.getUrl(Requests.Request.VIRTUAL_PROCEDURES_RATINGCURVE, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.getAsString();
				
				System.out.println(json.toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.VIRTUAL_PROCEDURE_RATINGCURVE_REMOVED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}

	
	/**
	 * Default method for loading offerings
	 */
	public void loadOfferings(){
		this.loadOfferings(null);
	}
	
	/**
	 * Load offerings from service.
	 * @param callback
	 */
	public void loadOfferings(final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		IstSOS.executeGet(Requests.getUrl(Requests.Request.OFFERINGS, urlKeyMap), new IstSOSListener() {
			
			@Override
			public void onSuccess(EventObject event) {
				
				JsonObject json = (JsonObject) event.getObject();
		        
		        JsonArray data = json.getAsJsonArray("data");
		        
		        for (Iterator<JsonElement> iter = data.iterator(); iter.hasNext();){
		        	JsonElement element = iter.next();
		        	if(element.isJsonObject()){
		        		JsonObject object = element.getAsJsonObject();
		        		
		        		System.out.println(object.toString());
		        		
		        		Offering offering = Offering.fromJson(object);
		        		
                        Service.this.offerings.add(offering);
		        		
		        	}
		        }
		        
		        EventObject eventObject = new EventObject(Event.OFFERINGS_LOADED, offerings);
	    		
	    		if(callback != null){
	    			callback.onSuccess(eventObject);
	    		}
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Retrieve a list of offerings
	 * @return an ArrayList of offerings, with offering name as key and offering object as value
	 */
	public ArrayList<Offering> getOfferings() {
		return this.offerings;
	}
	
	/**
	 * Default method for registering offering to service.
	 * @param offering
	 */
	public void registerOffering(Offering offering){
		this.registerOffering(offering, null);
	}
	
	/**
	 * Register offering to service
	 * @param offering
	 * @param callback
	 */
	public void registerOffering(Offering offering, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(offering);

		IstSOS.executePost(Requests.getUrl(Requests.Request.OFFERINGS, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.OFFERINGS_REGISTERED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
		
	}
	
	/**
	 * Default method for updating offering
	 * @param offering
	 */
	public void updateOffering(Offering offering){
		this.updateOffering(offering, null);
	}
	
	/**
	 * Update selected offering
	 * @param offering
	 * @param callback
	 */
	public void updateOffering(Offering offering, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", offering.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(offering);

		IstSOS.executePut(Requests.getUrl(Requests.Request.OFFERINGS, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				// Convert response in offering type object
				Offering response_offering = Offering.fromJson(json);

				System.out.println(response_offering.toJson().toString());
				
		        EventObject eventObject = new EventObject(
		        		Event.OFFERINGS_UPDATED, response_offering);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}
	
	/**
	 * Default method for removing offering
	 * @param offering
	 */
	public void removeOffering(Offering offering){
		this.removeOffering(offering, null);
	}
	
	/**
	 * Remove offering from service
	 * @param offering
	 * @param callback
	 */
	public void removeOffering(Offering offering, final IstSOSListener callback){
		
		Map<String, String> urlKeyMap = new HashMap<String, String>();
		urlKeyMap.put("url", this.server.getServerUrl());
		urlKeyMap.put("name", this.getName());
		urlKeyMap.put("code", offering.getName());
		
		Gson gson = new GsonBuilder().create();
		String data = gson.toJson(offering);

		IstSOS.executeDelete(Requests.getUrl(Requests.Request.OFFERINGS, urlKeyMap), data, new IstSOSListener(){
			
			@Override
			public void onSuccess(EventObject event) {
				
				System.out.println("Result..");
				
				JsonObject json = (JsonObject) event.getObject();
				
				System.out.println(json.toString());
				
				String message = json.toString();
				
		        EventObject eventObject = new EventObject(
		        		Event.OFFERINGS_REMOVED, message);
		        
				if(callback != null){
					callback.onSuccess(eventObject);
				}
				
			}
			
			@Override
			public void onError(EventObject event) {
				// 
				
			}
		}, this.server.getRealm());
	}

	static Service fromJson(JsonObject json){
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Service.class);
	}

	@Override
	public JsonObject toJson() {
		return null;
	}
}