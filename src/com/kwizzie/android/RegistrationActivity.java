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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RegistrationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateUI(){
		EditText userNameText = (EditText)findViewById(R.id.usernameText);
		userNameText.setError("username already exists");
		
	}
	public void onBtnClick(View v){
		String username = ((EditText)findViewById(R.id.usernameText)).getText().toString();
		String name = ((EditText)findViewById(R.id.nameText)).getText().toString();
		String emailID = ((EditText)findViewById(R.id.emailText)).getText().toString();
		String password = ((EditText)findViewById(R.id.passwordText)).getText().toString();
		
		new DownloadData(this).execute(username , name , emailID ,password);
		
	}
	
	private class DownloadData extends AsyncTask<String, Void, String> {

		public String response;
		public static final String SERVER_URL = KwizzieConsts.SERVER_URL+"player/register";
		Activity activity;
		public DownloadData(Activity activity){
			this.activity = activity;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("1")){
				Intent intent = new Intent(activity , RegistrationCompleteActivity.class);
				startActivity(intent);
			} else {
				updateUI();
			}
		}

		@Override
		protected void onPreExecute() {		}

		@Override
		protected String doInBackground(String... args) {
			try{
				String username = args[0];
				String name = args[1];
				String emailID = args[2];
				String password  = args[3];
				String getURL = SERVER_URL;
				Log.i("server url",getURL);
				
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("emailID", emailID));
				nameValuePairs.add(new BasicNameValuePair("password", password));
			
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

}
