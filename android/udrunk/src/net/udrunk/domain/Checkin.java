package net.udrunk.domain;

import com.j256.ormlite.field.DatabaseField;


public class Checkin {
	@DatabaseField(id = true)
	private Integer id;
	
	@DatabaseField
	private String added;
	
	@DatabaseField
	private Integer level;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Place place;
	
	@DatabaseField
	private String status;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private User user;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdded() {
		return added;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
