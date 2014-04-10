package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QRQuestion extends Question{

	public QRQuestion( QuestionLocation location,
			QuestionCategory category, String questionTitle,
			AnswerType answerType, boolean isLocked) {
		super();
		this.location = location;
		this.category = category;
		this.questionTitle = questionTitle;
		this.answerType = answerType;
		this.isLocked = isLocked;
		this.typeOfQuestion="QR_QUESTION";
	}
	
	public QRQuestion(Parcel source){
		questionTitle = source.readString();
		answerType = source.readParcelable(QRAnswerType.class.getClassLoader());
		typeOfQuestion = source.readString();
	}

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(questionTitle);
		dest.writeParcelable(answerType, flags);
		dest.writeString(typeOfQuestion);
	}
	
	public static final Parcelable.Creator<QRQuestion> CREATOR = new Parcelable.Creator<QRQuestion>() {

		@Override
		public QRQuestion createFromParcel(Parcel source) {
			return new QRQuestion(source);
		}

		@Override
		public QRQuestion[] newArray(int size) {
			return new QRQuestion[size];
		}
	};
}
