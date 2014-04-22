package com.kwizzie.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;

public class PrivateStartQuizActivity extends Activity {
	
	ArrayList<Question> questions;
	String roomId, roomName, description;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_start_quiz);
		boolean alreadyPlayed = getIntent().getExtras().getBoolean("hasAlreadyPlayed");
		roomId = getIntent().getExtras().getString("roomId");
		TextView numOfQuestext = (TextView)findViewById(R.id.numberOFQuestions);
		TextView descriptionText = (TextView)findViewById(R.id.description);
		TextView quizRoomNameText = (TextView)findViewById(R.id.quizRoomName);
		if(alreadyPlayed){
			TextView failMsg = (TextView) findViewById(R.id.tvFailMessage);
			failMsg.setText("Oops !!! You have already played in this Quiz room.");
			failMsg.setVisibility(View.VISIBLE);
			Button startQuiz = (Button) findViewById(R.id.buttonStartQuiz);
			startQuiz.setVisibility(View.GONE);
			numOfQuestext.setVisibility(View.INVISIBLE);
			descriptionText.setVisibility(View.INVISIBLE);
			quizRoomNameText.setVisibility(View.INVISIBLE);
		} else {
			questions = getIntent().getExtras().getParcelableArrayList("questions");
			roomName = getIntent().getExtras().getString("roomName");
			description = getIntent().getExtras().getString("description");
			numOfQuestext.setText("Number of questions: "+String.valueOf(questions.size()));
			descriptionText.setText(description);
			quizRoomNameText.setText(roomName);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.private_start_quiz, menu);
		return true;
	}
	
	public void onBtnClick(View v){
		switch(v.getId()){
		case R.id.buttonStartQuiz:
			Intent intent = new Intent(this,QuestionType.valueOf(questions.get(0).getTypeOfQuestion()).getQuestionType());
			intent.putExtra("questionNumber",0);
			intent.putParcelableArrayListExtra("questions", questions);
			intent.putExtra("quizRoomName", roomName);
			intent.putExtra("quizRoomID", roomId);
			intent.putExtra("playerScore",0);
			startActivity(intent);
			finish();
			break;
		case R.id.buttonViewLeaderboard:
			Intent leaderboardIntent = new Intent(this,LeaderBoardActivity.class);
			leaderboardIntent.putExtra("roomID", roomId);
			startActivity(leaderboardIntent);
		}
	}
}
