package net.udrunk.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Place implements Serializable {
	private String id;
	
	private String name;
	private String city;
	
	public Place(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
