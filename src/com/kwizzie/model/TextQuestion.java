package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TextQuestion extends Question{

	private String subTitle;

	public TextQuestion(String subTitle, QuestionLocation location,
			QuestionCategory category, String questionTitle,
			AnswerType answerType, boolean isLocked) {
		super();
		this.subTitle = subTitle;
		this.location = location;
		this.category = category;
		this.questionTitle = questionTitle;
		this.answerType = answerType;
		this.isLocked = isLocked;
		typeOfQuestion="TEXT_QUESTION";
	}
	
	public TextQuestion(Parcel source) {
		questionTitle = source.readString();
		subTitle = source.readString();
		//location = source.readParcelable(QuestionLocation.class.getClassLoader());
		//category = source.readParcelable(QuestionCategory.class.getClassLoader());
		answerType = source.readParcelable(AnswerType.class.getClassLoader());
		typeOfQuestion = source.readString();
	}
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	@Override
	public int describeContents() {
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(questionTitle);
		dest.writeString(subTitle);
		//dest.writeParcelable(location, flags);
		//dest.writeParcelable(category, flags);
		dest.writeParcelable(answerType, flags);
		dest.writeString(typeOfQuestion);
		//dest.writeParcelable(isLocked, flags);
	}
	
	
	
	public static final Parcelable.Creator<TextQuestion> CREATOR = new Parcelable.Creator<TextQuestion>() {

		@Override
		public TextQuestion createFromParcel(Parcel source) {
			return new TextQuestion(source);
		}

		@Override
		public TextQuestion[] newArray(int size) {
			return new TextQuestion[size];
		}
	};
}
