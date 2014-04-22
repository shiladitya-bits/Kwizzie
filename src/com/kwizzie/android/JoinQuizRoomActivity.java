package com.kwizzie.android;

import com.kwizzie.model.Player;

import flexjson.JSONDeserializer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class JoinQuizRoomActivity extends Activity {

	Player player;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_quiz_room);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());
		
		View header = findViewById(R.id.headerLayout);
		SharedPreferences  pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    String json = pref.getString("player", "");
	    JSONDeserializer<Player> des = new JSONDeserializer<Player>();
	    player = des.deserialize(json);

		TextView tvHeader = (TextView)header.findViewById(R.id.tvHeaderName);
	    tvHeader.setText(player.getDetails().getName());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_quiz_room, menu);
		return true;
	}

	public void onBtnClick(View v){
		switch(v.getId()){
		case R.id.privateQuizRoomB :
			Intent intent = new Intent(this,PrivateJoinQuizActivity.class);
			startActivity(intent);
			break;
		case R.id.publicQuizRoomB :
			Intent intent1 = new Intent(this,PublicQuizRoomActivity.class);
			startActivity(intent1);
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_profile:
			Intent i = new Intent(this, UserProfileActivity.class);
			i.putExtra("playerID", player.getUserName());
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
