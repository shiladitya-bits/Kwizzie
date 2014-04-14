package com.kwizzie.model;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TextAnswerType implements AnswerType {
	
	private String correctAnswer;
	EvaluateAnswer evalAns;

	public TextAnswerType(){}
	
	public TextAnswerType(Parcel source){
		correctAnswer = source.readString();
	}
	
	public TextAnswerType(String correctAnswer, EvaluateAnswer evalAns) {
		super();
		this.correctAnswer = correctAnswer;
		this.evalAns = evalAns;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	@Override
	public boolean checkAnswer(Object answer) {
		String enteredAns = (String) answer;
		if(enteredAns.equalsIgnoreCase(correctAnswer)){
			return true;
		}
		return false;
	}

	@Override
	public void createAnswerLayout(LinearLayout layout, Activity context) {
		LinearLayout horizontal = new LinearLayout(context);
		horizontal.setOrientation(LinearLayout.HORIZONTAL);
		horizontal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		final EditText et = new EditText(context);
		et.setEms(20);
		et.setHint("Enter answer here");
		et.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.25f));
		horizontal.addView(et);
		
		Button btn = new Button(context);
		btn.setText("Go!");
		btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.75f));
		horizontal.addView(btn);
		
		layout.addView(horizontal);
		btn.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				String ans = et.getText().toString().trim();
				if(ans.equalsIgnoreCase(correctAnswer)){
					evalAns.onCorrectAnswer();
				} else{
					evalAns.onWrongAnswer();
				}
			}
		});
	}

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getCorrectAnswer());
	}

	public static final Parcelable.Creator<TextAnswerType> CREATOR = new Parcelable.Creator<TextAnswerType>() {

		@Override
		public TextAnswerType createFromParcel(Parcel source) {
			return new TextAnswerType(source);
		}

		@Override
		public TextAnswerType[] newArray(int size) {
			return new TextAnswerType[size];
		}
	};


	@Override
	public EvaluateAnswer getEvaluateAnswerController() {
		return evalAns;
	}

	@Override
	public void setEvaluateAnswerController(EvaluateAnswer controller) {
		this.evalAns = controller;
	}
	
	
}
