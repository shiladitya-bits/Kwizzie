package com.kwizzie.android.timer;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.kwizzie.model.EvaluateAnswer;

public class QuestionTimer extends CountDownTimer{

	private TextView timerDisplay;
	private EvaluateAnswer evaluator;
	private int elapsedSeconds;
	
	public QuestionTimer(EvaluateAnswer evaluator, TextView tv, long millisInFuture) {
		super(millisInFuture, 1000);
		this.evaluator = evaluator;
		this.timerDisplay = tv;
		this.timerDisplay.setText(String.valueOf(millisInFuture/1000));
		this.elapsedSeconds = 0;
	}

	@Override
	public void onFinish() {
		evaluator.onWrongAnswer();
	}

	@Override
	public void onTick(long millisUntilFinished) {
		elapsedSeconds++;
		timerDisplay.setText(String.valueOf(millisUntilFinished/1000));
	}

	public int getElapsedSeconds() {
		return elapsedSeconds;
	}

	public void setElapsedSeconds(int elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}

}
