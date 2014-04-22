package com.kwizzie.android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwizzie.android.timer.QuestionTimer;
import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.PictureQuestion;
import com.kwizzie.model.Player;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;
import com.kwzzie.location.QuestionLocationListener;

public class PictureQuestionActivity extends Activity implements EvaluateAnswer{

	PictureQuestion ques;
	ImageView view;
	TextView quesTitle;
	Player player;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_question);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());
		quesTitle = (TextView) findViewById(R.id.picQuestionTitle);

		View embedLayout = findViewById(R.id.embedLayout);
		scoreTv = (TextView) embedLayout.findViewById(R.id.scoreTv);
		view = (ImageView)findViewById(R.id.pictureQuestionImageView);
		
		//TODO setQuizRoomName
		questions =  getIntent().getParcelableArrayListExtra("questions");		
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		scoreTv.setText(String.valueOf(playerScore));
		PictureQuestion ques = (PictureQuestion)questions.get(questionNumber);		
		View quesLockLayout = findViewById(R.id.quesLockEmbedLayout);
		
		Button skipButton = (Button)quesLockLayout.findViewById(R.id.skipQues);
		skipButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onWrongAnswer();
				PictureQuestionActivity.this.finish();
				
			}
		});
		ques.getAnswerType().setEvaluateAnswerController(this);		
		quesTitle.setText(ques.getQuestionTitle());
		ques.getAnswerType().createAnswerLayout((LinearLayout)findViewById(R.id.answerLayout), this);		
		//ImageLoader imageLoader = new ImageLoader(getApplicationContext());
		//imageLoader.DisplayImage(ques.getPictureURL(),R.drawable.ic_launcher,view);
		new DownloadImageTask(view).execute(ques.getPictureURL()); 
		
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picture_question, menu);
		return true;
	}

	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView iv;
		
		public DownloadImageTask(ImageView iv){
			this.iv = iv;
		}
		@Override
		protected Bitmap doInBackground(String... arg0) {
			String url = arg0[0];
			Bitmap output = null;
			
			try{
				if(url.startsWith("localhost")){
					url = "10.0.2.2"+url.substring(9);
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
				
				InputStream in = new java.net.URL(url).openStream();
				output = BitmapFactory.decodeStream(in);
			} catch(Exception e){
				e.printStackTrace();
			}
			return output;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			iv.setImageBitmap(result);
			timer.start();			
		}

		@Override
		protected void onPreExecute() {
			
		}
		
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
