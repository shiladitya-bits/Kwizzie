package com.kwizzie.android;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.kwizzie.android.timer.QuestionTimer;
import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;
import com.kwizzie.model.VideoQuestion;
import com.kwzzie.location.QuestionLocationListener;

public class VideoQuestionActivity extends Activity implements EvaluateAnswer {

	VideoView view;
	TextView quesTitle;
	ArrayList<Question> questions;
	int questionNumber;
	String quizRoomName;
	String quizRoomID;
	int playerScore;
	TextView scoreTv;
	TextView timeRemainingTv;
	QuestionTimer timer;
	LocationManager locationManager;
	QuestionLocationListener listener;
	MediaController controller;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_question);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());

		View embedLayout = findViewById(R.id.embedLayout);
		scoreTv = (TextView) embedLayout.findViewById(R.id.scoreTv);
		quesTitle = (TextView) findViewById(R.id.questionTitle);
		view = (VideoView)findViewById(R.id.questionvideoView);		
		questions =  getIntent().getParcelableArrayListExtra("questions");		
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		scoreTv.setText(String.valueOf(playerScore));
		VideoQuestion ques = (VideoQuestion)questions.get(questionNumber);		
		View quesLockLayout = findViewById(R.id.quesLockEmbedLayout);
		Button skipButton = (Button)quesLockLayout.findViewById(R.id.skipQues);

		skipButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onWrongAnswer();
				VideoQuestionActivity.this.finish();
			}
		});
		ques.getAnswerType().setEvaluateAnswerController(this);
		quesTitle.setText(ques.getQuestionTitle());
		ques.getAnswerType().createAnswerLayout((LinearLayout)findViewById(R.id.answerLayout), this);
		String url;
		if(ques.getVideoURL().startsWith("localhost")){
			url = "10.0.2.2"+ques.getVideoURL().substring(9);
		} else {
			url = ques.getVideoURL();
		}
		String finalUrl;
		StringTokenizer portToken = new StringTokenizer(url, "/");
		if(!quizRoomID.equals("public")){
			finalUrl = "http://"+portToken.nextToken();
		} else {
			finalUrl = "http://"+portToken.nextToken()+":8080";
		}
		while(portToken.hasMoreTokens()){
			finalUrl = finalUrl+"/"+portToken.nextToken();
		}
		url = finalUrl;
		controller = new MediaController(this);
		controller.setAnchorView(view);
		controller.setMediaPlayer(view);
		Uri uri=Uri.parse(url);
		view.setMediaController(controller);
		view.setVideoURI(uri);
		view.requestFocus();
		view.setFocusable(true);
		view.setBackground(getResources().getDrawable(R.drawable.video_play));
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				view.setBackgroundDrawable(null);
				view.start();
				return true;
			}
		});
		view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				timer.start();
			}
		});
		
		timeRemainingTv = (TextView)embedLayout.findViewById(R.id.tvTimeRemaining);
		timer = new QuestionTimer(this, timeRemainingTv, KwizzieConsts.QUESTION_TIME_LIMIT);
		ques.getAnswerType().setTimer(timer);
		
		if(ques.getLocation() ==null){
			quesLockLayout.setVisibility(View.GONE);
		} else {
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			listener = new QuestionLocationListener(this, ques.getLocation() , quesLockLayout, timer);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , KwizzieConsts.MINIMUM_TIME_BETWEEN_UPDATE, KwizzieConsts.MINIMUM_DISTANCECHANGE_FOR_UPDATE ,listener);
			TextView tvDest = (TextView)findViewById(R.id.tvDestiName);
			tvDest.setText(ques.getLocation().getLocationName());

		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.video_question, menu);
		return true;
	}

	@Override
	public void onCorrectAnswer(int time) {
		view.stopPlayback();
		if(locationManager != null){
			locationManager.removeUpdates(listener);
		}
		questionNumber++;
		playerScore=playerScore + 20 - time;
		scoreTv.setText(String.valueOf(playerScore));
		Intent intent;
		if(questionNumber>=questions.size()){
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
		view.stopPlayback();
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
