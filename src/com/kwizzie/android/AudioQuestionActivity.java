package com.kwizzie.android;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwizzie.model.AudioQuestion;
import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;

public class AudioQuestionActivity extends Activity implements EvaluateAnswer{

	TextView quesTitle;
	ArrayList<Question> questions;
	int questionNumber;
	Button playB;
	String quizRoomName;
	String quizRoomID;
	int playerScore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_question);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());
		playB = (Button) findViewById(R.id.playB);
		quesTitle = (TextView) findViewById(R.id.questionTitle);
		questions =  getIntent().getParcelableArrayListExtra("questions");		
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		
		AudioQuestion ques = (AudioQuestion)questions.get(questionNumber);		
		ques.getAnswerType().setEvaluateAnswerController(this);		
		quesTitle.setText(ques.getQuestionTitle());
		ques.getAnswerType().createAnswerLayout((LinearLayout)findViewById(R.id.answerLayout), this);
		final String url = ques.getAudioURL();
		
		playB.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				MediaPlayer mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				try {
					mediaPlayer.setDataSource(url);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				mediaPlayer.start();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio_question, menu);
		return true;
	}

	@Override
	public void onCorrectAnswer() {
		questionNumber++;
		playerScore=playerScore + 5;
		if(questionNumber==questions.size()){
			Intent intent = new Intent(this,PrivateQuizFinishActivity.class);
			intent.putExtra("quizRoomName",quizRoomName);
			intent.putExtra("quizRoomID",quizRoomID);
			intent.putExtra("playerScore",playerScore);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this,QuestionType.valueOf(questions.get(questionNumber).getTypeOfQuestion()).getQuestionType());
			intent.putExtra("questionNumber",questionNumber);
			intent.putParcelableArrayListExtra("questions", questions);
			intent.putExtra("quizRoomName",quizRoomName);
			intent.putExtra("quizRoomID",quizRoomID);
			intent.putExtra("playerScore",playerScore);
			startActivity(intent);
		}
		finish();
	}

	@Override
	public void onWrongAnswer() {
		questionNumber++;
		if(questionNumber==questions.size()){
			Intent intent = new Intent(this,PrivateQuizFinishActivity.class);
			intent.putExtra("quizRoomName",quizRoomName);
			intent.putExtra("quizRoomID",quizRoomID);
			intent.putExtra("playerScore",playerScore);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this,QuestionType.valueOf(questions.get(questionNumber).getTypeOfQuestion()).getQuestionType());
			intent.putExtra("questionNumber",questionNumber);
			intent.putParcelableArrayListExtra("questions", questions);
			intent.putExtra("quizRoomName",quizRoomName);
			intent.putExtra("quizRoomID",quizRoomID);
			intent.putExtra("playerScore",playerScore);
			startActivity(intent);
		}
		finish();
	}

}
