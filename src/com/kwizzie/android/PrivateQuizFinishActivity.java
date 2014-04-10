package com.kwizzie.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kwizzie.model.Player;

public class PrivateQuizFinishActivity extends Activity {

	String quizRoomName;
	String quizRoomID;
	int playerScore;
	Player player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_quiz_finish);
		quizRoomName = getIntent().getExtras().getString("quizRoomName");
		quizRoomID = getIntent().getExtras().getString("quizRoomID");
		playerScore = getIntent().getExtras().getInt("playerScore");
		SharedPreferences  pref = getPreferences(MODE_PRIVATE);
		Gson gson = new Gson();
	    String json = pref.getString("player", "");
	    player = gson.fromJson(json, Player.class);
	    //TODO backend call player  , quiz room nid , score
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.private_quiz_finish, menu);
		return true;
	}

}
