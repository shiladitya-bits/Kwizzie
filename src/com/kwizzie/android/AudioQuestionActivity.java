package com.kwizzie.android;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwizzie.android.timer.QuestionTimer;
import com.kwizzie.model.AudioQuestion;
import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;
import com.kwzzie.location.QuestionLocationListener;

public class AudioQuestionActivity extends Activity implements EvaluateAnswer{

	TextView quesTitle;
	ArrayList<Question> questions;
	int questionNumber;
	Button playB;
	String quizRoomName;
	String quizRoomID;
	int playerScore;
	TextView scoreTv;
	TextView timeRemainingTv;
	QuestionTimer timer;
	LocationManager locationManager;
	QuestionLocationListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_question);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());

		View embedLayout = findViewById(R.id.embedLayout);
		scoreTv = (TextView) embedLayout.findViewById(R.id.scoreTv);
		playB = (Button) findViewById(R.id.playB);
		quesTitle = (TextView) findViewById(R.id.questionTitle);
		questions =  getIntent().getParcelableArrayListExtra("questions");		
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		scoreTv.setText(String.valueOf(playerScore));
		AudioQuestion ques = (AudioQuestion)questions.get(questionNumber);

		View quesLockLayout = findViewById(R.id.quesLockEmbedLayout);

		Button skipButton = (Button)quesLockLayout.findViewById(R.id.skipQues);
		skipButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onWrongAnswer();
				AudioQuestionActivity.this.finish();
				
			}
		});
		ques.getAnswerType().setEvaluateAnswerController(this);		
		quesTitle.setText(ques.getQuestionTitle());
		ques.getAnswerType().createAnswerLayout((LinearLayout)findViewById(R.id.answerLayout), this);
		final String url = ques.getAudioURL();

		timeRemainingTv = (TextView)embedLayout.findViewById(R.id.tvTimeRemaining);
		timer = new QuestionTimer(this, timeRemainingTv, 10000);
		ques.getAnswerType().setTimer(timer);
		
		if(ques.getLocation() ==null){
			quesLockLayout.setVisibility(View.GONE);
		} else {
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			listener = new QuestionLocationListener(this, ques.getLocation() , quesLockLayout, timer);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , KwizzieConsts.MINIMUM_TIME_BETWEEN_UPDATE, KwizzieConsts.MINIMUM_DISTANCECHANGE_FOR_UPDATE ,listener);
			timer.start();
			Location temp = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.i("currentLocation" , String.valueOf(temp.getLatitude()));
			Log.i("currentLocation" , String.valueOf(temp.getLongitude()));
			
			
		}
	
		
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
	public void onCorrectAnswer(int time) {
		if(locationManager != null){
			locationManager.removeUpdates(listener);
		}
		questionNumber++;
		playerScore=playerScore + 20 - time;
		scoreTv.setText(String.valueOf(playerScore));
		Intent intent;
		if(questionNumber==questions.size()){
			intent = new Intent(this,PrivateQuizFinishActivity.class);
		} else {
			intent = new Intent(this,QuestionType.valueOf(questions.get(questionNumber).getTypeOfQuestion()).getQuestionType());
			intent.putExtra("questionNumber",questionNumber);
			intent.putParcelableArrayListExtra("questions", questions);
		}
		intent.putExtra("quizRoomName",quizRoomName);
		intent.putExtra("quizRoomID",quizRoomID);
		intent.putExtra("playerScore",playerScore);
		if(quizRoomID.equals("public")){
			intent.putExtra("category", getIntent().getExtras().getParcelable("category"));
		}
		startActivity(intent);
		finish();
	}

	@Override
	public void onWrongAnswer() {
		if(locationManager != null){
			locationManager.removeUpdates(listener);
		}
		questionNumber++;
		Intent intent;
		if(questionNumber==questions.size()){
			intent = new Intent(this,PrivateQuizFinishActivity.class);
		} else {
			intent = new Intent(this,QuestionType.valueOf(questions.get(questionNumber).getTypeOfQuestion()).getQuestionType());
			intent.putExtra("questionNumber",questionNumber);
			intent.putParcelableArrayListExtra("questions", questions);
		}
		intent.putExtra("quizRoomName",quizRoomName);
		intent.putExtra("quizRoomID",quizRoomID);
		intent.putExtra("playerScore",playerScore);
		if(quizRoomID.equals("public")){
			intent.putExtra("category", getIntent().getExtras().getParcelable("category"));
		}
		startActivity(intent);
		finish();
	}

}
