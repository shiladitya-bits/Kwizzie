package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoQuestion extends Question implements Parcelable{

	private String videoURL;

	public VideoQuestion(){}
	
	public VideoQuestion(String videoURL, QuestionLocation location,
			QuestionCategory category, String questionTitle,
			AnswerType answerType, boolean isLocked) {
		super();
		this.videoURL = videoURL;
		this.location = location;
		this.category = category;
		this.questionTitle = questionTitle;
		this.answerType = answerType;
		this.isLocked = isLocked;
		this.typeOfQuestion="VIDEO_QUESTION";
	}
	
	public VideoQuestion(Parcel source){
		videoURL=source.readString();
		questionTitle=source.readString();
		answerType=source.readParcelable(AnswerType.class.getClassLoader());
		typeOfQuestion=source.readString();
	}
	public String getVideoURL() {
		return videoURL;
	}
	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}
	@Override
	public int describeContents() {
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(videoURL);
		dest.writeString(questionTitle);
		//dest.writeParcelable(location, flags);
		//dest.writeParcelable(category, flags);
		dest.writeParcelable(answerType, flags);
		dest.writeString(typeOfQuestion);
		//dest.writeParcelable(isLocked, flags);
	}
	
	public static final Parcelable.Creator<VideoQuestion>  CREATOR = new Parcelable.Creator<VideoQuestion>() {

		@Override
		public VideoQuestion createFromParcel(Parcel source) {
			return new VideoQuestion(source);
		}

		@Override
		public VideoQuestion[] newArray(int size) {
			return new VideoQuestion[size];
		}
	
	
	};
}
