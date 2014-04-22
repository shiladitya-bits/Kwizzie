package com.kwizzie.android;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwizzie.model.Player;
import com.kwizzie.model.QuestionCategory;

import flexjson.JSONDeserializer;

public class PrivateQuizFinishActivity extends Activity {

	String quizRoomName;
	String quizRoomID;
	int playerScore;
	Player player;
	String response;
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
	    TextView scoreTv = (TextView) findViewById(R.id.finalScoreTv);
	    scoreTv.setText(String.valueOf(playerScore));
	    if(isNetworkAvailable()){
	    	new	DownloadData().execute(player.getUserName() , quizRoomID , String.valueOf(playerScore));
	    } else {
		new AlertDialog.Builder(this).setMessage("Can not connect to network. Please check your data connection.")
		.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setCancelable(false).show();
	}
	   
	}
	public boolean isNetworkAvailable() 
	{
        ConnectivityManager cm = (ConnectivityManager) 
          getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
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
			/*if(quizRoomID.equals("public")){
				QuestionCategory category = getIntent().getExtras().getParcelable("category");
				i.putExtra("roomID", category.getCategoryCode());
			} else {*/
				i.putExtra("roomID", quizRoomID);
			//}
			startActivity(i);
			finish();
			break;
		case R.id.leaveRoomBtn:
			finish();
			break;
		}
		
	}
	
	private class DownloadData extends AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(String result) {
			LinearLayout buttonLayout = (LinearLayout)findViewById(R.id.postUpdateScoreLayout);
			LinearLayout updateLayout = (LinearLayout)findViewById(R.id.updateScoreLayout);
			
			buttonLayout.setVisibility(View.VISIBLE);
			updateLayout.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {		}

		@Override
		protected String doInBackground(String... args) {
			
			String username = args[0];
			String roomID = args[1];
			String score = args[2];

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("roomID", roomID));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("score", score));
			String getURL = KwizzieConsts.SERVER_URL+"player/updatePrivateScore";
				
			if(roomID.equals("public")){
				QuestionCategory category = getIntent().getExtras().getParcelable("category");
				getURL = KwizzieConsts.SERVER_URL+"player/updatePublicScore";
				nameValuePairs.add(new BasicNameValuePair("category", category.getCategoryCode()));
			}
			Log.i("server url",getURL);

			try{
				// POST REQUEST
				HttpClient httpclient = new DefaultHttpClient();				
				HttpPost httppost = new HttpPost(getURL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse = httpclient.execute(httppost);
				HttpEntity resEntityGet = httpResponse.getEntity();  
	
		        			
			if (resEntityGet != null) 
	        {  
				response = EntityUtils.toString(resEntityGet);
	            Log.d("response", response);	             
				return response;
	        }
			else return null;
			}
			catch(Exception e){
				return null;
			}
		}
		
	}
}
