package com.kwizzie.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.kwizzie.model.Player;

import flexjson.JSONDeserializer;

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
		SharedPreferences  pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    String json = pref.getString("player", "");
	    JSONDeserializer<Player> des = new JSONDeserializer<Player>();
	    player = des.deserialize(json);
	    //TODO backend call player  , quiz room nid , score
	    TextView scoreTv = (TextView) findViewById(R.id.finalScoreTv);
	    scoreTv.setText("You scored "+playerScore);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.private_quiz_finish, menu);
		return true;
	}

	public void onClickBtn(View v){
		switch(v.getId()){
		case R.id.leaderboardBtn:
			Intent i = new Intent(this, LeaderBoardActivity.class);
			i.putExtra("roomID", quizRoomID);
			startActivity(i);
			finish();
			break;
		case R.id.leaveRoomBtn:
			Intent i2 = new Intent(this, JoinQuizRoomActivity.class);
			startActivity(i2);
			finish();
			break;
		}
		
	}
}
