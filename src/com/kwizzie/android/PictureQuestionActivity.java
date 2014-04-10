package com.kwizzie.android;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.PictureQuestion;
import com.kwizzie.model.Player;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_question);
		
		quesTitle = (TextView) findViewById(R.id.picQuestionTitle);
		view = (ImageView)findViewById(R.id.pictureQuestionImageView);
		
		//TODO setQuizRoomName
		questions =  getIntent().getParcelableArrayListExtra("questions");		
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		
		PictureQuestion ques = (PictureQuestion)questions.get(questionNumber);		
		ques.getAnswerType().setEvaluateAnswerController(this);		
		quesTitle.setText(ques.getQuestionTitle());
		ques.getAnswerType().createAnswerLayout((LinearLayout)findViewById(R.id.answerLayout), this);		
		//ImageLoader imageLoader = new ImageLoader(getApplicationContext());
		//imageLoader.DisplayImage(ques.getPictureURL(),R.drawable.ic_launcher,view);
		new DownloadImageTask(view).execute(ques.getPictureURL()); 
		
		
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
		}

		@Override
		protected void onPreExecute() {
			
		}
		
	}


	@Override
	public void onCorrectAnswer() {
		//TODO increment according to timer
		playerScore=playerScore + 5;
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
