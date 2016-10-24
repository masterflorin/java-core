package org.istsos.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.istsos.client.observation.Result;

/**
 * The Observation class represents data such as result, sampling time, procedure.
 *
 */
public class Observation implements IstSOSObject<Observation>{

	@SerializedName ("samplingTime")
	private SamplingTime samplingTime;
	@SerializedName ("name")
	private String name;
	@SerializedName ("procedure")
	private String procedure;
	@SerializedName ("result")
	private Result result;
	
	/**
	 * 
	 * @return sampling time - {@link SamplingTime}
	 */
	public SamplingTime getSamplingTime() {
		return samplingTime;
	}
	/**
	 * 
	 * @param samplingTime - {@link SamplingTime}
	 */
	public void setSamplingTime(SamplingTime samplingTime) {
		this.samplingTime = samplingTime;
	}
	/**
	 * 
	 * @return a string
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
	 * @return name of procedure as String
	 */
	public String getProcedure() {
		return procedure;
	}
	/**
	 * 
	 * @param procedure - String
	 */
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	/**
	 * 
	 * @return observation {@link Result}
	 */
	public Result getResult() {
		return result;
	}
	/**
	 * 
	 * @param result - {@link Result}
	 */
	public void setResult(Result result) {
		this.result = result;
	}


	@Override
	public JsonObject toJson() {
		// TODO Auto-generated method stub
		JsonObject json = new JsonObject();
		json.addProperty("name", this.getName());
		json.addProperty("procedure", this.getProcedure());
		//convert sampling time
		Gson gson = new GsonBuilder().create();
		json.addProperty("samplingTime", gson.toJson(this.getSamplingTime(), SamplingTime.class));
		json.addProperty("result", gson.toJson(this.getResult(), Result.class));
		
		return json;
	}
	
	
	static Observation fromJson(JsonObject json){
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Observation.class);
		
		
	}

}
