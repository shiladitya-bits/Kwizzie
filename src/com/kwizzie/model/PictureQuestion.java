package com.kwizzie.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class PictureQuestion extends Question {

	private String pictureURL;

	public PictureQuestion(){}
	
	public PictureQuestion(String pictureURL, QuestionLocation location,
			QuestionCategory category, String questionTitle,
			AnswerType answerType, boolean isLocked) {
		super();
		this.pictureURL = pictureURL;
		this.location = location;
		this.category = category;
		this.questionTitle = questionTitle;
		this.answerType = answerType;
		this.isLocked = isLocked;
		typeOfQuestion="PICTURE_QUESTION";
	}

//	public PictureQuestion(JSONObject obj){
//		try {
//			this.pictureURL = obj.getString("pictureURL");
//			this.location = new QuestionLocation();
//		} catch (JSONException e) {
//			
//			e.printStackTrace();
//		}
//	}
	public PictureQuestion(Parcel source){
		questionTitle = source.readString();
		pictureURL = source.readString();
		//location = source.readParcelable(QuestionLocation.class.getClassLoader());
		//category = source.readParcelable(QuestionCategory.class.getClassLoader());
		answerType = source.readParcelable(AnswerType.class.getClassLoader());
		typeOfQuestion = source.readString();
	}
	
	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	@Override
	public int describeContents() {
		
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(questionTitle);
		dest.writeString(pictureURL);
		//dest.writeParcelable(location, flags);
		//dest.writeParcelable(category, flags);
		dest.writeParcelable(answerType, flags);
		dest.writeString(typeOfQuestion);
		//dest.writeParcelable(isLocked, flags);
	}
	
	public static final Parcelable.Creator<PictureQuestion> CREATOR = new Parcelable.Creator<PictureQuestion>() {

		@Override
		public PictureQuestion createFromParcel(Parcel source) {
			return new PictureQuestion(source);
		}

		@Override
		public PictureQuestion[] newArray(int size) {
			return new PictureQuestion[size];
		}
	};
}
