package com.kwizzie.model;

import android.app.Activity;

import com.kwizzie.android.AudioQuestionActivity;
import com.kwizzie.android.PictureQuestionActivity;
import com.kwizzie.android.QRQuestionActivity;
import com.kwizzie.android.TextQuestionActivity;
import com.kwizzie.android.VideoQuestionActivity;

public enum QuestionType {

	PICTURE_QUESTION(PictureQuestionActivity.class),
	VIDEO_QUESTION(VideoQuestionActivity.class),
	AUDIO_QUESTION(AudioQuestionActivity.class),
	TEXT_QUESTION(TextQuestionActivity.class),
	QR_QUESTION(QRQuestionActivity.class);

	private Class<? extends Activity> questionType;
	
	private QuestionType(Class<? extends Activity> arg){
		this.questionType = arg;
	}

	public Class<? extends Activity> getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Class<? extends Activity> questionType) {
		this.questionType = questionType;
	}
	
	
}
