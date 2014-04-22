package com.kwizzie.model;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kwizzie.android.KwizzieConsts;
import com.kwizzie.android.R;
import com.kwizzie.android.timer.QuestionTimer;

public class MCQAnswerType implements AnswerType {

	private List<String> options;
	private int correctOptionIndex;
	EvaluateAnswer evaluateAns;
	QuestionTimer timer;
	
	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public int getCorrectOptionIndex() {
		return correctOptionIndex;
	}

	public void setCorrectOptionIndex(int correctOptionIndex) {
		this.correctOptionIndex = correctOptionIndex;
	}

	public MCQAnswerType(){}
	
	public MCQAnswerType(List<String> options, int correctOption, EvaluateAnswer obj){
		this.options = options;
		this.correctOptionIndex=correctOption;
		this.evaluateAns = obj;
	}

	
	public MCQAnswerType(Parcel source) {
		options = new ArrayList<String>();
		source.readList(options, String.class.getClassLoader());
		this.correctOptionIndex = source.readInt();
	}

	@Override
	public boolean checkAnswer(Object answer) {
		int selectedOption = (Integer) answer;
		if(correctOptionIndex==selectedOption){
			return true;
		}
		return false;
		
	}

	@Override
	public void createAnswerLayout(LinearLayout layout , Activity context) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//lp.setMargins(60,10,60,10); // left, top, right, bottom
		lp.topMargin=10;
		lp.bottomMargin=10;
		for(String option : options){
			if(option.trim().length() == 0){
				continue;
			}
			Button button = new Button(context);
			button.setText(option);
			button.setTextColor(context.getResources().getColor(android.R.color.black));
			button.setGravity(Gravity.CENTER);
			setViewBackground(button, context.getResources().getDrawable(R.drawable.button_answer_options));
			layout.addView(button,lp);
			
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					timer.cancel();
					String selectedText = ((Button)v).getText().toString();
					PostAnswerTimer postTimer = new PostAnswerTimer(KwizzieConsts.POST_ANSWER_DELAY, KwizzieConsts.POST_ANSWER_DELAY, selectedText, options.get(correctOptionIndex));
					if(selectedText.equals(options.get(correctOptionIndex))){
						updateCorrectAnswerUI((Button)v);
					} else {
						updateWrongAnswerUI((Button)v);
					}
					postTimer.start();
				}
			});
		}
		
	}
	
	private void updateWrongAnswerUI(Button btn) {
		Activity activity = (Activity)evaluateAns;
		btn.setTextColor(activity.getResources().getColor(R.color.incorrect_answer));
	}

	private void updateCorrectAnswerUI(Button btn) {
		Activity activity = (Activity)evaluateAns;
		btn.setTextColor(activity.getResources().getColor(R.color.correct_answer));
	}
	
	  /**
     * Sets background of the view.
     * This method varies in implementation depending on Android SDK version.
     *
     * @param view       View to which set background
     * @param background Background to set to view
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(options);
		dest.writeInt(correctOptionIndex);
	}

	public static final Parcelable.Creator<MCQAnswerType> CREATOR = new Parcelable.Creator<MCQAnswerType>() {

		@Override
		public MCQAnswerType createFromParcel(Parcel source) {
			return new MCQAnswerType(source);
		}

		@Override
		public MCQAnswerType[] newArray(int size) {
			return new MCQAnswerType[size];
		}
	};

	@Override
	public EvaluateAnswer getEvaluateAnswerController() {
		return evaluateAns;
	}

	@Override
	public void setEvaluateAnswerController(EvaluateAnswer controller) {
		this.evaluateAns = controller;
	}
	
	public QuestionTimer getTimer() {
		return timer;
	}

	public void setTimer(QuestionTimer timer) {
		this.timer = timer;
	}
	
	private class PostAnswerTimer extends CountDownTimer{

		String userAns, correctAns;
		public PostAnswerTimer(long millisInFuture, long countDownInterval, String userAnswer, String correctAns) {
			super(millisInFuture, countDownInterval);
			this.userAns = userAnswer;
			this.correctAns = correctAns;
		}

		@Override
		public void onFinish() {
			if(userAns.equalsIgnoreCase(correctAns)){
				evaluateAns.onCorrectAnswer(timer.getElapsedSeconds());
			} else{
				evaluateAns.onWrongAnswer();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {}
		
	}
}
