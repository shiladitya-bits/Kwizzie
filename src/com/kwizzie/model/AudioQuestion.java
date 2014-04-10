package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AudioQuestion extends Question implements Parcelable{

	private String audioURL;
	public AudioQuestion(String audioURL, QuestionLocation location,
			QuestionCategory category, String questionTitle,
			AnswerType answerType, boolean isLocked) {
		super();
		this.audioURL = audioURL;
		this.location = location;
		this.category = category;
		this.questionTitle = questionTitle;
		this.answerType = answerType;
		this.isLocked = isLocked;
		this.typeOfQuestion="AUDIO_QUESTION";
	}
	public AudioQuestion(Parcel source){
		audioURL = source.readString();
		questionTitle = source.readString();
		answerType = source.readParcelable(AudioQuestion.class.getClassLoader());
		typeOfQuestion = source.readString();
	}
	
	public String getAudioURL() {
		return audioURL;
	}
	public void setAudioURL(String audioURL) {
		this.audioURL = audioURL;
	}
	@Override
	public int describeContents() {
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(audioURL);
		parcel.writeString(questionTitle);
		parcel.writeParcelable(answerType, flags);
		parcel.writeString(typeOfQuestion);
		
	}
	
	public static final Parcelable.Creator<AudioQuestion> CREATOR = new Parcelable.Creator<AudioQuestion>() {

		@Override
		public AudioQuestion createFromParcel(Parcel source) {
			return new AudioQuestion(source);
		}

		@Override
		public AudioQuestion[] newArray(int size) {
			return new AudioQuestion[size];
		}
	};
}
