package com.kwizzie.android;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kwizzie.model.PrivateQuizRoom;
import com.kwizzie.model.Question;

import flexjson.JSONDeserializer;

public class PrivateJoinQuizActivity extends Activity {

	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_join_quiz);
		pd = new ProgressDialog(this);
		pd.setMessage("Connecting");
		pd.setCancelable(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.private_join_quiz, menu);
		return true;
	}

	public void onBtnClick(View v){
		if(isNetworkAvailable()){
			EditText etRoomId = (EditText)findViewById(R.id.etRoomId);
			EditText etRoomKey = (EditText)findViewById(R.id.etSecurityKey);
			new DownloadData(this, pd).execute(etRoomId.getText().toString(), etRoomKey.getText().toString());
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
	
	private class DownloadData extends AsyncTask<String, Void, String> {
		String response;
		Activity activity;
		ProgressDialog pd;
		public DownloadData(Activity activity, ProgressDialog pd){
			this.pd = pd;
			pd.show();
			this.activity = activity;
		}
		
		@Override
		protected void onPostExecute(String result) {
			pd.cancel();
			if(result != null){
				try{
					if(response.equals("")){
						Toast.makeText(activity, "Room does not exist or incorrect security key!", Toast.LENGTH_SHORT).show();
						return;
					}
					JSONDeserializer<PrivateQuizRoom> des = new JSONDeserializer<PrivateQuizRoom>();
					PrivateQuizRoom room = des.deserialize(response);
					Intent i = new Intent(activity, PrivateStartQuizActivity.class);
					i.putParcelableArrayListExtra("questions", (ArrayList<Question>)room.getQuestions());
					i.putExtra("roomName", room.getRoomName());
					i.putExtra("roomId", room.getRoomID());
					i.putExtra("description", room.getDescription());
					
					activity.startActivity(i);
					activity.finish();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {		}

		@Override
		protected String doInBackground(String... arg0) {
			String roomId = arg0[0];
			String roomKey = arg0[1];
			try{
				
				String getURL = KwizzieConsts.SERVER_URL+"quizRoom/private?"+"roomId="+roomId+"&key="+roomKey;
				Log.i("server url",getURL);
				
				//GET REQUEST
				HttpClient client = new DefaultHttpClient();  
				HttpGet get = new HttpGet(getURL);
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntityGet = responseGet.getEntity();
		        
				if (resEntityGet != null) 
		        {  
					response = EntityUtils.toString(resEntityGet);
		            Log.d("response", response);	             
					return response;
		        }
				else return null;
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
	}
}
