package net.udrunk.domain;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class Place implements Serializable {
	@DatabaseField(id = true)
	private Integer id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String city;

	@DatabaseField
	private String geometry;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	
	public String getGeometry() {
		return geometry;
	}
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	
	public double getLat()
	{
		String part = geometry.substring(7, geometry.length() - 1); // Get rid of parentheses.
	    String[] coords = part.split(" ");
	    double lat = Double.parseDouble(coords[1]);
	    return lat;
	}

	public double getLong()
	{
		String part = geometry.substring(7, geometry.length() - 1); // Get rid of parentheses.
		String[] coords = part.split(" ");
		double lg = Double.parseDouble(coords[0]);
		return lg;
	}
}
