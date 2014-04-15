package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

public class PlayerPersonalDetails implements Parcelable {
	private String photoUrl;
	private String name;
	private String emailId;
	public String getPhotoUrl() {
		return photoUrl;
	}
	public PlayerPersonalDetails(){}
	
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
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(emailId);
		dest.writeString(photoUrl);
	}
	
	public static final Parcelable.Creator<PlayerPersonalDetails> CREATOR = new Parcelable.Creator<PlayerPersonalDetails>() {

		@Override
		public PlayerPersonalDetails createFromParcel(Parcel source) {
			return new PlayerPersonalDetails(source);
		}

		@Override
		public PlayerPersonalDetails[] newArray(int size) {
			return new PlayerPersonalDetails[size];
		}
	
	
	};
}