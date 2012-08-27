package net.udrunk.domain;


import com.j256.ormlite.field.DatabaseField;


public class User {

	@DatabaseField(id = true)
	private Integer id;
	
	@DatabaseField
	private String username;
	
	@DatabaseField
	private String firstName;
	
	@DatabaseField
	private String mastName;
	
	@DatabaseField
	private String email;
	
	@DatabaseField
	private String avatar;
	
	@DatabaseField
	private long date_joined;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMastName() {
		return mastName;
	}
	public void setMastName(String mastName) {
		this.mastName = mastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public long getDate_joined() {
		return date_joined;
	}
	public void setDate_joined(long date_joined) {
		this.date_joined = date_joined;
	}
}
