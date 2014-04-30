package com.kwizzie.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kwizzie.android.qr.IntentIntegrator;
import com.kwizzie.android.qr.IntentResult;
import com.kwizzie.android.timer.QuestionTimer;
import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.Player;
import com.kwizzie.model.QRAnswerType;
import com.kwizzie.model.QRQuestion;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;
import com.kwzzie.location.QuestionLocationListener;

public class QRQuestionActivity extends Activity  implements EvaluateAnswer{

	TextView quesTitle;
	Player player;
	ArrayList<Question> questions;
	int questionNumber;
	QRQuestion ques;
	String quizRoomName;
	String quizRoomID;
	int playerScore;
	TextView scoreTv;
	TextView timeRemainingTv;
	QuestionTimer timer;
	LocationManager locationManager;
	QuestionLocationListener listener;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrquestion);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());
		
		View embedLayout = findViewById(R.id.embedLayout);
		scoreTv = (TextView) embedLayout.findViewById(R.id.scoreTv);
		quesTitle = (TextView) findViewById(R.id.questionTitle);
		
		//TODO setQuizRoomName
		questions =  getIntent().getParcelableArrayListExtra("questions");		
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		scoreTv.setText(String.valueOf(playerScore));
		ques = (QRQuestion)questions.get(questionNumber);	
		View quesLockLayout = findViewById(R.id.quesLockEmbedLayout);
		
		Button skipButton = (Button)quesLockLayout.findViewById(R.id.skipQues);
		skipButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onWrongAnswer();
				QRQuestionActivity.this.finish();
				
			}
		});
		//ques.getAnswerType().setEvaluateAnswerController(this);		
		quesTitle.setText(ques.getQuestionTitle());
		
		timeRemainingTv = (TextView)embedLayout.findViewById(R.id.tvTimeRemaining);
		timer = new QuestionTimer(this, timeRemainingTv, KwizzieConsts.QR_QUESTION_TIME_LIMIT);
		ques.getAnswerType().setTimer(timer);

		if(ques.getLocation() ==null){
			quesLockLayout.setVisibility(View.GONE);
			timer.start();
		} else {
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			listener = new QuestionLocationListener(this, ques.getLocation() , quesLockLayout, timer);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , KwizzieConsts.MINIMUM_TIME_BETWEEN_UPDATE, KwizzieConsts.MINIMUM_DISTANCECHANGE_FOR_UPDATE ,listener);
			TextView tvDest = (TextView)findViewById(R.id.tvDestiName);
			tvDest.setText(ques.getLocation().getLocationName());
			Location temp = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(temp!=null){
				Log.i("currentLocation" , String.valueOf(temp.getLatitude()));
				Log.i("currentLocation" , String.valueOf(temp.getLongitude()));	
			}
		
		}
				
	}

	public void onClickQr(View v){
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}
	
	@Override
	protected void onActivityResult(int requestCode , int resultCode , Intent intent){
		if(resultCode != RESULT_OK){
			Toast.makeText(this, "Oops! Your camera device is not working!", Toast.LENGTH_SHORT).show();
			return;
		}
		IntentResult scanResult =IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		timer.cancel();
		if(scanResult != null){
			 String output = scanResult.getContents();
			 String correctAns = ((QRAnswerType)ques.getAnswerType()).getAnswer();
	    		
    		PostAnswerTimer finishTimer = new PostAnswerTimer(KwizzieConsts.POST_ANSWER_DELAY, KwizzieConsts.POST_ANSWER_DELAY, output, correctAns);
			 if(output.equalsIgnoreCase(correctAns)){				 	
				 View v = findViewById(R.id.qrPostAnswerLayout);
		    	 v.setVisibility(View.VISIBLE);
		    	 finishTimer.start();
			 } else {
		    		TextView tvIncorrect = (TextView)findViewById(R.id.tvCorrectAns);
		    		tvIncorrect.setText("Invalid QR code scanned!");
		    		
		    		ImageView ivIncorrect = (ImageView)findViewById(R.id.ivCorrectDrawable);
		    		ivIncorrect.setImageDrawable(getResources().getDrawable(R.drawable.incorrect));
		    		View v = findViewById(R.id.qrPostAnswerLayout);
		    		v.setVisibility(View.VISIBLE);
		    		finishTimer.start();
		    	}
		} else {
			onWrongAnswer();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qrquestion, menu);
		return true;
	}

	@Override
	public void onCorrectAnswer(int time) {
		if(locationManager != null){
			locationManager.removeUpdates(listener);
		}
		questionNumber++;
		playerScore=playerScore + 20 - time/10;
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
		if(locationManager != null){
			locationManager.removeUpdates(listener);
		}
		questionNumber++;
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
	

	private class PostAnswerTimer extends CountDownTimer{
		String userAns, correctAns;
		public PostAnswerTimer(long millisInFuture, long countDownInterval, String userAns, String correctAns) {
			super(millisInFuture, countDownInterval);
			this.userAns = userAns;
			this.correctAns = correctAns;
		}

		@Override
		public void onFinish() {
			if(userAns.equalsIgnoreCase(correctAns)){
	    		onCorrectAnswer(timer.getElapsedSeconds());
	    	} else {
	    		onWrongAnswer();
	    	}
		}

		@Override
		public void onTick(long millisUntilFinished) {}
		
	}
}
