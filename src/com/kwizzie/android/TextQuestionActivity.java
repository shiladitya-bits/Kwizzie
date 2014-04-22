package com.kwizzie.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwizzie.android.timer.QuestionTimer;
import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;
import com.kwizzie.model.TextQuestion;
import com.kwzzie.location.QuestionLocationListener;

public class TextQuestionActivity extends Activity implements EvaluateAnswer{

	int questionNumber;
	ArrayList<Question> questions;
	TextView quesTitle;
	TextView quesSubTitle;
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
		setContentView(R.layout.activity_text_question);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());
		
		quesTitle = (TextView) findViewById(R.id.questionTitle);
		quesSubTitle = (TextView) findViewById(R.id.questionSubTitle);

		View embedLayout = findViewById(R.id.embedLayout);
		scoreTv = (TextView) embedLayout.findViewById(R.id.scoreTv);
		
		questions =  getIntent().getParcelableArrayListExtra("questions");
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		scoreTv.setText(String.valueOf(playerScore));
		TextQuestion ques = (TextQuestion)questions.get(questionNumber);
		
		ques.getAnswerType().setEvaluateAnswerController(this);
		
		quesTitle.setText(ques.getQuestionTitle());
		quesSubTitle.setText(ques.getSubTitle());
		
		ques.getAnswerType().createAnswerLayout((LinearLayout)findViewById(R.id.answerLayout), this);
	
		timeRemainingTv = (TextView)embedLayout.findViewById(R.id.tvTimeRemaining);
		timer = new QuestionTimer(this, timeRemainingTv, KwizzieConsts.QUESTION_TIME_LIMIT);
		ques.getAnswerType().setTimer(timer);

		View quesLockLayout = findViewById(R.id.quesLockEmbedLayout);
		if(ques.getLocation() ==null){
			quesLockLayout.setVisibility(View.GONE);
			timer.start();
		} else {
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			listener = new QuestionLocationListener(this, ques.getLocation() , quesLockLayout, timer);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , KwizzieConsts.MINIMUM_TIME_BETWEEN_UPDATE, KwizzieConsts.MINIMUM_DISTANCECHANGE_FOR_UPDATE ,listener);
			TextView tvDest = (TextView)findViewById(R.id.tvDestiName);
			tvDest.setText(ques.getLocation().getLocationName());
			
			/*	Location temp = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.i("currentLocation" , String.valueOf(temp.getLatitude()));
			Log.i("currentLocation" , String.valueOf(temp.getLongitude()));*/
		}
		/*//just to check
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//listener = new QuestionLocationListener(this, ques.getLocation() , quesLockLayout, timer);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , KwizzieConsts.MINIMUM_TIME_BETWEEN_UPDATE, KwizzieConsts.MINIMUM_DISTANCECHANGE_FOR_UPDATE ,listener);
		Location temp = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Log.i("currentLocation" , String.valueOf(temp.getLatitude()));
		Log.i("currentLocation" , String.valueOf(temp.getLongitude()));*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_question, menu);
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
