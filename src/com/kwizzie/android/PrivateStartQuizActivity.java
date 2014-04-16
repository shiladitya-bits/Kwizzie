package com.kwizzie.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;

public class PrivateStartQuizActivity extends Activity {
	
	ArrayList<Question> questions;
	String roomId, roomName, description;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_start_quiz);
		
		questions = getIntent().getExtras().getParcelableArrayList("questions");
		roomName = getIntent().getExtras().getString("roomName");
		roomId = getIntent().getExtras().getString("roomId");
		description = getIntent().getExtras().getString("description");
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
		}
	}
}
