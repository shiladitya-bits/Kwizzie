package com.kwizzie.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class JoinQuizRoomActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_quiz_room);
		ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic_imageview);
		profilePictureView.setImageBitmap(Utils.getProfilePicture());
		
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
}
