package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionLocation implements Parcelable{

	double latitude;
	double longitute;
	double radius;
	String locationName;
	String imageUrl;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitute() {
		return longitute;
	}
	public void setLongitute(double longitute) {
		this.longitute = longitute;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public QuestionLocation(double latitude, double longitute, double radius,
			String locationName, String imageUrl) {
		super();
		this.latitude = latitude;
		this.longitute = longitute;
		this.radius = radius;
		this.locationName = locationName;
		this.imageUrl = imageUrl;
	}
	public QuestionLocation() {}

	public QuestionLocation(Parcel source) {
		this.latitude = source.readDouble();
		this.longitute = source.readDouble();
		this.radius = source.readDouble();
		this.locationName = source.readString();
		this.imageUrl = source.readString();
	}
	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(latitude);
		dest.writeDouble(longitute);
		dest.writeDouble(radius);
		dest.writeString(locationName);
		dest.writeString(imageUrl);
	}

	public static final Parcelable.Creator<QuestionLocation> CREATOR = new Parcelable.Creator<QuestionLocation>() {

		@Override
		public QuestionLocation createFromParcel(Parcel source) {
			return new QuestionLocation(source);
		}

		@Override
		public QuestionLocation[] newArray(int size) {
			return new QuestionLocation[size];
		}
	};
		
}
