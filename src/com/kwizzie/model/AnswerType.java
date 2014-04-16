package com.kwizzie.model;

import com.kwizzie.android.timer.QuestionTimer;

import android.app.Activity;
import android.os.Parcelable;
import android.widget.LinearLayout;

public interface AnswerType extends Parcelable {
	
	boolean checkAnswer(Object answer); 
	void createAnswerLayout(LinearLayout layout, Activity context);
	EvaluateAnswer getEvaluateAnswerController();
	void setEvaluateAnswerController(EvaluateAnswer controller);
	QuestionTimer getTimer();
	void setTimer(QuestionTimer timer);
}
