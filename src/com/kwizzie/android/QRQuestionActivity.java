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

import com.kwizzie.android.qr.IntentIntegrator;
import com.kwizzie.android.qr.IntentResult;
import com.kwizzie.model.Player;
import com.kwizzie.model.QRAnswerType;
import com.kwizzie.model.QRQuestion;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionType;

public class QRQuestionActivity extends Activity  {

	TextView quesTitle;
	Player player;
	ArrayList<Question> questions;
	int questionNumber;
	QRQuestion ques;
	String quizRoomName;
	String quizRoomID;
	int playerScore;
	TextView scoreTv;
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
		//ques.getAnswerType().setEvaluateAnswerController(this);		
		quesTitle.setText(ques.getQuestionTitle());
	}

	public void onClickQr(View v){
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}
	
	@Override
	protected void onActivityResult(int requestCode , int resultCode , Intent intent){
		IntentResult scanResult =IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if(scanResult != null){
			 String output = scanResult.getContents();
			 String correctAns = ((QRAnswerType)ques.getAnswerType()).getAnswer();
		    	if(output.equalsIgnoreCase(correctAns)){
		    		onCorrectAnswer();
		    	} else {
		    		onWrongAnswer();
		    	}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qrquestion, menu);
		return true;
	}
	
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
