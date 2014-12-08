package rs.poc.models;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Event {

	private String id;
	private String creatorId;
	private Timestamp startTime;
	private String city;
	private String state;
	private String zip;
	private String country;
	private Double lat;
	private Double lon;
	private ArrayList<Double> words;
	
	public Event(){
		words = new ArrayList<Double>();
	}
	
	public void addWordData(double wordFreq){
		words.add(wordFreq);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj.getClass() != this.getClass())
			return false;
		Event evt = (Event) obj;
		return this.id.equals(evt.id);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.size(); i++) {
			sb.append("word"+i+": " + words.get(i));
		}
		return id + "|" + creatorId + "|" + startTime.toString() + "|" + "word data: " + sb.toString(); 
	}
}
