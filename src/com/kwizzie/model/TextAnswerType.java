package com.kwizzie.model;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwizzie.android.KwizzieConsts;
import com.kwizzie.android.R;
import com.kwizzie.android.timer.QuestionTimer;

public class TextAnswerType implements AnswerType {
	
	private String correctAnswer;
	EvaluateAnswer evalAns;
	QuestionTimer timer;
	
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
		
		LinearLayout mainLayout = (LinearLayout) context.getLayoutInflater().inflate(R.layout.text_answer_layout, null);
		layout.addView(mainLayout);
		Button proceed = (Button) context.findViewById(R.id.proceed);
		final EditText ansText = (EditText) context.findViewById(R.id.userAns);
		proceed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				timer.cancel();
				String ans = ansText.getText().toString().trim();
				PostAnswerTimer postAnswerTimer = new PostAnswerTimer(KwizzieConsts.POST_ANSWER_DELAY, KwizzieConsts.POST_ANSWER_DELAY, ans, correctAnswer);
				if(ans.equalsIgnoreCase(correctAnswer)){
					updateCorrectAnswerUI();
				} else{
					updateWrongAnswerUI();
				}
				postAnswerTimer.start();
			}
		});
		/*LinearLayout horizontal = new LinearLayout(context);
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
				timer.cancel();
				String ans = et.getText().toString().trim();
				PostAnswerTimer postAnswerTimer = new PostAnswerTimer(1000, 1000, ans, correctAnswer);
				if(ans.equalsIgnoreCase(correctAnswer)){
					updateCorrectAnswerUI();
				} else{
					updateWrongAnswerUI();
				}
				postAnswerTimer.start();
			}

		});*/
	}


	private void updateWrongAnswerUI() {
		Activity activity = (Activity)evalAns;
		View toHide = activity.findViewById(R.id.textAnswerLayout);
		View toShow = activity.findViewById(R.id.textPostAnswerLayout);
		toHide.setVisibility(View.GONE);
		toShow.setVisibility(View.VISIBLE);
		TextView tvCorrectAns = (TextView)toShow.findViewById(R.id.tvCorrectAns);
		tvCorrectAns.setText("Correct Answer: "+correctAnswer);
		ImageView ivCorrectStatus = (ImageView)toShow.findViewById(R.id.ivCorrectDrawable);
		ivCorrectStatus.setImageDrawable(activity.getResources().getDrawable(R.drawable.incorrect));
	}

	private void updateCorrectAnswerUI() {
		Activity activity = (Activity)evalAns;
		View toHide = activity.findViewById(R.id.textAnswerLayout);
		View toShow = activity.findViewById(R.id.textPostAnswerLayout);
		toHide.setVisibility(View.GONE);
		toShow.setVisibility(View.VISIBLE);
		TextView tvCorrectAns = (TextView)toShow.findViewById(R.id.tvCorrectAns);
		tvCorrectAns.setText("Correct Answer: "+correctAnswer);
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

	@Override
	public QuestionTimer getTimer() {
		return timer;
	}

	@Override
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
				evalAns.onCorrectAnswer(timer.getElapsedSeconds());
			} else{
				evalAns.onWrongAnswer();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {}
		
	}
}
