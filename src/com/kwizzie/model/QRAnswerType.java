package com.kwizzie.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

public class QRAnswerType implements AnswerType {

	private String answer;
	
	
	public QRAnswerType(String answer, EvaluateAnswer evaluateAns) {
		super();
		this.answer = answer;
		this.evaluateAns = evaluateAns;
	}
	
	public QRAnswerType(Parcel source){
		answer = source.readString();
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	EvaluateAnswer evaluateAns;
	
	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(answer);
	}

	@Override
	public boolean checkAnswer(Object answer) {
		String ans = (String) answer;
		if(ans.equals(answer))
			return true;
		return false;
	}

	@Override
	public void createAnswerLayout(LinearLayout layout, Activity context) {
		
		
	}

	@Override
	public EvaluateAnswer getEvaluateAnswerController() {
		return evaluateAns;
	}

	@Override
	public void setEvaluateAnswerController(EvaluateAnswer controller) {
		
	}

	public static final Parcelable.Creator<QRAnswerType> CREATOR = new Parcelable.Creator<QRAnswerType>() {

		@Override
		public QRAnswerType createFromParcel(Parcel source) {
			return new QRAnswerType(source);
		}

		@Override
		public QRAnswerType[] newArray(int size) {
			return new QRAnswerType[size];
		}
	};
}
