package com.kwizzie.model;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{

	private String userName;
	private String password;
	private PlayerPersonalDetails details;
	private Map<String,Integer> privateQuizRoomScores;
	private Map<String,Integer> publicCategoryScores;
	
	public Player(){}
	
	public Player(String userName, String password,String name,String emailId) {
		//TODO  default photoURL
		details= new PlayerPersonalDetails("",name,emailId);
		this.userName = userName;
		this.password = password;
	}
	
	public Player(Parcel source){
		userName = source.readString();
		password = source.readString();
		details = source.readParcelable(PlayerPersonalDetails.class.getClassLoader());
		privateQuizRoomScores = source.readHashMap(HashMap.class.getClassLoader());
		publicCategoryScores = source.readHashMap(HashMap.class.getClassLoader());
		
		//http://stackoverflow.com/questions/10757598/what-classloader-to-use-with-parcel-readhashmap
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
	/*
	public int increaseScore(String quizRoomName , int increment){
		int initialScore=0;
		if(quizRoomScores.containsKey(quizRoomName)){
			initialScore=quizRoomScores.get(quizRoomName);
		}
		int newscore = initialScore+increment;
		quizRoomScores.put(quizRoomName, newscore);
		return newscore;
	}*/

	public Map<String,Integer> getPrivateQuizRoomScores() {
		return privateQuizRoomScores;
	}

	public void setPrivateQuizRoomScores(Map<String,Integer> privateQuizRoomScores) {
		this.privateQuizRoomScores = privateQuizRoomScores;
	}

	public Map<String,Integer> getPublicCategoryScores() {
		return publicCategoryScores;
	}

	public void setPublicCategoryScores(Map<String,Integer> publicCategoryScores) {
		this.publicCategoryScores = publicCategoryScores;
	}

	public class PlayerPersonalDetails implements Parcelable{
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
		
		public PlayerPersonalDetails(Parcel source){
			name = source.readString();
			emailId = source.readString();
			photoUrl = source.readString();
				
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
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(name);
			dest.writeString(emailId);
			dest.writeString(photoUrl);			
		}
	
		
	}

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeString(password); 
		dest.writeParcelable(details , flags);
		dest.writeMap(privateQuizRoomScores);
		dest.writeMap(publicCategoryScores);
	}
	
	
}
