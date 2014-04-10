package com.kwizzie.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.google.gson.Gson;
import com.kwizzie.model.AudioQuestion;
import com.kwizzie.model.MCQAnswerType;
import com.kwizzie.model.PictureQuestion;
import com.kwizzie.model.PrivateQuizRoom;
import com.kwizzie.model.QRAnswerType;
import com.kwizzie.model.QRQuestion;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionCategory;
import com.kwizzie.model.QuestionType;
import com.kwizzie.model.QuizRoom;
import com.kwizzie.model.TextAnswerType;
import com.kwizzie.model.TextQuestion;
import com.kwizzie.model.VideoQuestion;

public class PrivateStartQuizActivity extends Activity {
	
	ArrayList<Question> questions;
	Gson gson = new Gson();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_start_quiz);
		ArrayList<String> optionList = new ArrayList<String>();
		optionList.add("disney");
		optionList.add("dreamworks");
		Question ques = new PictureQuestion("http://img4.wikia.nocookie.net/__cb20130222060253/disney/images/7/7d/2013disneyprincess.jpg",null, new QuestionCategory("GK","General Knowledge"),
				"Identify The picture",new MCQAnswerType(optionList, 0, null),false);
	
		Question ques2 = new TextQuestion("contin___s",null,null,"Fill in the missing characters",new TextAnswerType("uou",null),false);
		ArrayList<String> options3 = new ArrayList<String>();
		options3.add("sonu nigam");
		options3.add("arijit singh");
		options3.add("mika singh");
		options3.add("honney singh");
		
		Question ques3 = new AudioQuestion("http://www.largesound.com/ashborytour/sound/brobob.mp3", null, null, "Identify the singer", new MCQAnswerType(options3, 2, null), false);
		Question ques4 = new VideoQuestion("http://info.sonicretro.org/images/5/54/Tiger_Sonic_3D_Blast_Ad.flv", null, null, "Identify the actor in video", new TextAnswerType("Ranbeer Kapoor",null), false);
		Question ques5 = new QRQuestion(null, null, "scan the qr code", new QRAnswerType("testqr", null), false);
		questions=new ArrayList<Question>();
		questions.add(ques5);
		questions.add(ques);
		questions.add(ques2);
		questions.add(ques3);
		questions.add(ques4);
		
		QuizRoom quizRoom = new PrivateQuizRoom(questions, null, null, "Bits Quiz Room", "bits123");
		Intent intent = new Intent(this,QuestionType.valueOf(questions.get(0).getTypeOfQuestion()).getQuestionType());
		intent.putExtra("questionNumber",0);
		intent.putParcelableArrayListExtra("questions", questions);
		intent.putExtra("quizRoomName",quizRoom.getRoomName());
		intent.putExtra("quizRoomID",quizRoom.getRoomID());
		intent.putExtra("playerScore",0);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.private_start_quiz, menu);
		return true;
	}
}