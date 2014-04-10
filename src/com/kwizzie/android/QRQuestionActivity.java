package com.kwizzie.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kwizzie.android.qr.IntentIntegrator;
import com.kwizzie.android.qr.IntentResult;
import com.kwizzie.model.EvaluateAnswer;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrquestion);
		quesTitle = (TextView) findViewById(R.id.questionTitle);
		
		//TODO setQuizRoomName
		questions =  getIntent().getParcelableArrayListExtra("questions");		
		questionNumber = getIntent().getExtras().getInt("questionNumber");
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		
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
	
	
	/*@Override 
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) 
    {
    	//Toast.makeText(this.getApplicationContext(), "Scanned code " + rawResult.getText(), Toast.LENGTH_LONG).show();
    	String output = rawResult.getText();
    	String correctAns = ((QRAnswerType)ques.getAnswerType()).getAnswer();
    	if(output.equalsIgnoreCase(correctAns)){
    		onCorrectAnswer();
    	} else {
    		onWrongAnswer();
    	}
    }*/

	public void onCorrectAnswer() {
		questionNumber++;
		playerScore=playerScore + 5;
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
