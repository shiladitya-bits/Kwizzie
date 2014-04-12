package com.kwizzie.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kwizzie.android.R.id;
import com.kwizzie.model.EvaluateAnswer;
import com.kwizzie.model.PublicQuizRoom;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;
import com.kwizzie.model.TextQuestion;

public class TextQuestionActivity extends Activity implements EvaluateAnswer{

	int questionNumber;
	ArrayList<Question> questions;
	TextView quesTitle;
	TextView quesSubTitle;
	String quizRoomName;
	String quizRoomID;
	int playerScore;
	TextView scoreTv;
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
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_question, menu);
		return true;
	}
	
	@Override
	public void onCorrectAnswer() {
		questionNumber++;
		playerScore=playerScore + 5;
		scoreTv.setText(String.valueOf(playerScore));
		if(questionNumber==questions.size()){
			Intent intent = new Intent(this,PrivateQuizFinishActivity.class);
			intent.putExtra("quizRoomName",quizRoomName);
			intent.putExtra("quizRoomID",quizRoomID);
			intent.putExtra("playerScore",playerScore);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this,TextQuestionActivity.class);
			startActivity(intent);
			intent.putExtra("questionNumber",questionNumber);
			intent.putParcelableArrayListExtra("questions", questions);
			intent.putExtra("quizRoomName",quizRoomName);
			intent.putExtra("quizRoomID",quizRoomID);
			intent.putExtra("playerScore",playerScore);
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
