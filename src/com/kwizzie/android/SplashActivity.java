package com.kwizzie.android;


import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kwizzie.model.PrivateQuizRoom;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(isNetworkAvailable()){
        	new DownloadData(this).execute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
    
    public void onClick(View v){
    	switch(v.getId()){
    	case R.id.loginBtn :
    		Intent intentLogin = new Intent(this,LoginActivity.class);
    		startActivity(intentLogin);
    	break;
    	case R.id.signUpBtn :
    		Intent intentSignUp = new Intent(this,RegistrationActivity.class);
    		startActivity(intentSignUp);
    	break;	
    	
    	
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
		public static final String SERVER_URL = "http://10.20.1.215:8080/KwizzieServer/kwizzie/quizRoom/private?roomId=bits123&key=bitsbpgccafe";
		String response;
		Activity activity;
		public DownloadData(Activity activity){
			this.activity = activity;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
				Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
				try{
					Gson gson = new Gson();
					PrivateQuizRoom room = gson.fromJson(result, PrivateQuizRoom.class);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {		}

		@Override
		protected String doInBackground(String... arg0) {
			try{
				
				String getURL = SERVER_URL;
				Log.i("server url",getURL);
				
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				nameValuePairs.add(new BasicNameValuePair("flag", "transaction"));
				
				// POST REQUEST
//				HttpClient httpclient = new DefaultHttpClient();				
//				HttpPost httppost = new HttpPost(getURL);
//				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//				HttpResponse httpResponse = httpclient.execute(httppost);
//				HttpEntity resEntityGet = httpResponse.getEntity();  
		
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
