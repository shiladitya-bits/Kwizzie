package com.kwizzie.model;

import android.app.Activity;

import com.kwizzie.android.AudioQuestionActivity;
import com.kwizzie.android.PictureQuestionActivity;
import com.kwizzie.android.QRQuestionActivity;
import com.kwizzie.android.TextQuestionActivity;
import com.kwizzie.android.VideoQuestionActivity;

public enum QuestionType {

	PICTURE_QUESTION(PictureQuestionActivity.class, PictureQuestion.class),
	VIDEO_QUESTION(VideoQuestionActivity.class, VideoQuestion.class),
	AUDIO_QUESTION(AudioQuestionActivity.class, AudioQuestion.class),
	TEXT_QUESTION(TextQuestionActivity.class, TextQuestion.class),
	QR_QUESTION(QRQuestionActivity.class, QRQuestion.class);

	private Class<? extends Activity> questionType;
	private Class<? extends Question> modelClass;
	
	private QuestionType(Class<? extends Activity> arg, Class<? extends Question> model){
		this.questionType = arg;
		this.modelClass = model;
	}

	public Class<? extends Activity> getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Class<? extends Activity> questionType) {
		this.questionType = questionType;
	}

	public Class<? extends Question> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<? extends Question> modelClass) {
		this.modelClass = modelClass;
	}
	
}
