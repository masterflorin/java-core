package org.istsos.client;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * The Sampling Time class represents time interval for {@link Observation} using
 * {@link Date} type with the following format: 
 * <blockquote><pre>
 * 		yyyy-MM-dd'T'HH:mm:ssXX.
 * </pre></blockquote>
 * 
 */
@SuppressWarnings("rawtypes")
public class SamplingTime implements IstSOSObject{

	transient private String duration;
	@SerializedName("beginposition")
	private Date beginPosition;
	@SerializedName("endposition")
	private Date endPosition;
	/**
	 * 
	 * @param duration - String
	 * @param beginPosition - {@link Date}
	 * @param endPosition - {@link Date}
	 */
    public SamplingTime(String duration, Date beginPosition, Date endPosition) {
        this.duration = duration;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
    }

    public SamplingTime() {
    }
    /**
     * 
     * @return string with the duration
     */
    public String getDuration() {
		return duration;
	}
    /**
     * 
     * @param duration - String
     */
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * 
	 * @return beginning date of the interval 
	 */
	public Date getBeginPosition() {
		return beginPosition;
	}
	/**
	 * 
	 * @param beginPosition - {@link Date}
	 */
	public void setBeginPosition(Date beginPosition) {
		this.beginPosition = beginPosition;
	}
	/**
	 * 
	 * @return ending date of the interval
	 */
	public Date getEndPosition() {
		return endPosition;
	}
	/**
	 * 
	 * @param endPosition
	 */
	public void setEndPosition(Date endPosition) {
		this.endPosition = endPosition;
	}

	@Override
	public String toString() {

		return "begin_pos: " + this.beginPosition + " end_pos: " + this.endPosition;
		//return super.toString();
	}


	public JsonObject toJson() {
		// TODO Auto-generated method stub
		JsonObject json = new JsonObject();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXX").create();
		
		json.addProperty("duration", this.getDuration());
		json.addProperty("beginPosition", gson.toJson(this.getBeginPosition()));
		json.addProperty("endPosition", gson.toJson(this.getEndPosition()));
		
		return json;
	}


	static SamplingTime fromJson(JsonObject json) {
		// TODO Auto-generated method stub
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXX").create();
		return gson.fromJson(json, SamplingTime.class);
	}
	
	

}