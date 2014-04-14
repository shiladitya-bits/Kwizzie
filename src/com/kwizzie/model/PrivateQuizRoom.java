package com.kwizzie.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class PrivateQuizRoom extends QuizRoom implements Parcelable{

	
	private List<Question> questions;

	public PrivateQuizRoom(){}
	
	public PrivateQuizRoom(List<Question> questions,
			String description, String roomName, String roomID) {
		this.questions = questions;
		this.description = description;
		this.roomName = roomName;
		this.roomID = roomID;
	}

	public PrivateQuizRoom(Parcel source) {
	//	this.questions = Arrays.asList((Question[])source.readParcelableArray(Question.class.getClassLoader()));
		source.readList(questions, Question.class.getClassLoader());
		this.description = source.readString();
		this.roomName = source.readString();
		this.roomID = source.readString();
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//dest.writeParcelableArray(questions.toArray(new Question[questions.size()]), flags);
		dest.writeList(questions);
		dest.writeString(description);
		dest.writeString(roomName);
		dest.writeString(roomID);
	}
	
	public static final Parcelable.Creator<PrivateQuizRoom> CREATOR = new Parcelable.Creator<PrivateQuizRoom>() {

		@Override
		public PrivateQuizRoom createFromParcel(Parcel source) {
			return new PrivateQuizRoom(source);
		}

		@Override
		public PrivateQuizRoom[] newArray(int size) {
			return new PrivateQuizRoom[size];
		}
	};
}
