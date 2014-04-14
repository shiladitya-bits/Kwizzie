package com.kwizzie.model;

public class Leader {
	private String name;
	private	String username;
	private int score;
	private String photoURL;
	
	public Leader(){}
	
	public Leader(String name, String username, int score, String photoURL) {
		super();
		this.name = name;
		this.username = username;
		this.score = score;
		this.photoURL = photoURL;
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	
}
