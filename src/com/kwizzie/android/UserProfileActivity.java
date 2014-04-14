package com.kwizzie.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.kwizzie.model.Player;

public class UserProfileActivity extends Activity {

	Player player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		Player player = (Player)getIntent().getExtras().get("player");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

}
