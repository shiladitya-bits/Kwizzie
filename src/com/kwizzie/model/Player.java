package com.kwizzie.model;

import java.util.Map;

public class Player {

	private String userName;
	private String password;
	private PlayerPersonalDetails details;
	private Map<String,Integer> quizRoomScores;
	
	
	public Player(String userName, String password,String name,String emailId) {
		//TODO  default photoURL
		details= new PlayerPersonalDetails("",name,emailId);
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public PlayerPersonalDetails getDetails() {
		return details;
	}

	public void setDetails(PlayerPersonalDetails details) {
		this.details = details;
	}

	public Map<String, Integer> getQuizRoomScores() {
		return quizRoomScores;
	}

	public void setQuizRoomScores(Map<String, Integer> quizRoomScores) {
		this.quizRoomScores = quizRoomScores;
	}

	public Player(String userName, String password,String name,String emailId,String photoURL) {
		super();
		details= new PlayerPersonalDetails(photoURL,name,emailId);
		this.userName = userName;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int increaseScore(String quizRoomName , int increment){
		int initialScore=0;
		if(quizRoomScores.containsKey(quizRoomName)){
			initialScore=quizRoomScores.get(quizRoomName);
		}
		int newscore = initialScore+increment;
		quizRoomScores.put(quizRoomName, newscore);
		return newscore;
	}

	public class PlayerPersonalDetails{
		private String photoUrl;
		private String name;
		private String emailId;
		public String getPhotoUrl() {
			return photoUrl;
		}
		public PlayerPersonalDetails(String photoUrl, String name,
				String emailId) {
			this.photoUrl = photoUrl;
			this.setName(name);
			this.emailId = emailId;
		}
		public void setPhotoUrl(String photoUrl) {
			this.photoUrl = photoUrl;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
		
	}
	
	
}
