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
		details = source.readParcelable(PlayerPersonalDetails.class.getClassLoader());		
		source.readMap(privateQuizRoomScores, HashMap.class.getClassLoader());
		source.readMap(publicCategoryScores, HashMap.class.getClassLoader());
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

	public Player(Player source){
		
		
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

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeParcelable(details, flags);
		dest.writeMap(privateQuizRoomScores);
		dest.writeMap(publicCategoryScores);
	}
	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {

		@Override
		public Player createFromParcel(Parcel source) {
			return new Player(source);
		}

		@Override
		public Player[] newArray(int size) {
			return new Player[size];
		}
	
	
	};
	
}
