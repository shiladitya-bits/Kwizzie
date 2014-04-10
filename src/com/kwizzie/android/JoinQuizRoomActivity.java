package com.kwizzie.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class JoinQuizRoomActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_quiz_room);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_quiz_room, menu);
		return true;
	}

}
