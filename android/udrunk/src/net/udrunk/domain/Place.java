package net.udrunk.domain;

import java.io.Serializable;

import net.udrunk.infra.PointPersister;


import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class Place implements Serializable {
	@DatabaseField(id = true)
	private Integer id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String city;

	@DatabaseField(persisterClass = PointPersister.class)
	private Point geometry;
	
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
	
	public Point getGeometry() {
		return geometry;
	}
	public void setGeometry(Point geometry) {
		this.geometry = geometry;
	}
}
