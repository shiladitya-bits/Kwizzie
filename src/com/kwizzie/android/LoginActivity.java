package com.kwizzie.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kwizzie.model.Player;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LoginActivity extends Activity {

	EditText usernameText;
	EditText passwordText;
	Button loginB;
	Player player;
	Bitmap imageBitmap; 
	String response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Show the Up button in the action bar.
		setupActionBar();
	usernameText = (EditText) findViewById(R.id.username);
	passwordText = (EditText) findViewById(R.id.password);
	loginB = (Button) findViewById(R.id.LoginB);
	loginB.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View arg0) {
			
			String username = usernameText.getText().toString();
			String password = passwordText.getText().toString();

			if(isNetworkAvailable()){
					new DownloadData().execute(username , password);
			} else {
				new AlertDialog.Builder(LoginActivity.this).setMessage("Can not connect to network. Please check your data connection.")
				.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int pid = android.os.Process.myPid();
						android.os.Process.killProcess(pid);
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						startActivity(intent);
					}
				}).setCancelable(false).show();
			}
		
		}
	});
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
		getMenuInflater().inflate(R.menu.login, menu);
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
	//crops imageBitmap to circle shape
	 public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		  // TODO Auto-generated method stub
		  int targetWidth = 50;
		  int targetHeight = 50;
		  Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,Bitmap.Config.ARGB_8888);
          Canvas canvas = new Canvas(targetBitmap);
		  Path path = new Path();
		  path.addCircle(((float) targetWidth - 1) / 2,
		  ((float) targetHeight - 1) / 2,
		  (Math.min(((float) targetWidth), 
		                ((float) targetHeight)) / 2),
		          Path.Direction.CCW);
		  

		                canvas.clipPath(path);
		  Bitmap sourceBitmap = scaleBitmapImage;
		  canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
		    sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight), null);
		  return targetBitmap;
		 }

	 //sets profile pic in imageBitmap from URL
	 private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
			
		@Override
		protected Bitmap doInBackground(String... arg0) {
			String url = arg0[0];
			
			try{
				InputStream in = new java.net.URL(url).openStream();
				imageBitmap = BitmapFactory.decodeStream(in);
			} catch(Exception e){
				e.printStackTrace();
			}
			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if(result == null){
				imageBitmap = BitmapFactory.decodeResource(LoginActivity.this.getResources(), R.drawable.ic_launcher);
			}
			saveImage();
		}

		@Override
		protected void onPreExecute() {
			
		}
	 }

	 public void saveImage(){
		imageBitmap = getRoundedShape(imageBitmap);
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/kwizzie";
		File dir = new File(filePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(dir , "profilePic.png");
		try{
		FileOutputStream fout = new FileOutputStream(file);
		imageBitmap.compress(Bitmap.CompressFormat.PNG, 85, fout);
		fout.flush();
		fout.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Intent intent = new Intent(getApplicationContext(),JoinQuizRoomActivity.class);
		startActivity(intent);
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

		public DownloadData(){
		}
		
		@Override
		protected void onPostExecute(String result) {
			JSONDeserializer<Player> deserializer  = new JSONDeserializer<Player>();
			player = deserializer.deserialize(result);
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor prefEditor =  pref.edit();
			JSONSerializer ser = new JSONSerializer();
			String jsonPlayer = ser.deepSerialize(player);
			prefEditor.putString("player", jsonPlayer);
			prefEditor.commit();
			new DownloadImageTask().execute(player.getDetails().getPhotoUrl());
		}

		@Override
		protected void onPreExecute() {		}

		@Override
		protected String doInBackground(String... args) {
			try{
				
				String getURL = KwizzieConsts.SERVER_URL+"player";
				String  username = args[0];
				String password = args[1];
				Log.i("server url",getURL);
				
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("password",password));
				
				// POST REQUEST
				HttpClient httpclient = new DefaultHttpClient();				
				HttpPost httppost = new HttpPost(getURL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse = httpclient.execute(httppost);
				HttpEntity resEntityGet = httpResponse.getEntity();  
	
				/*//GET REQUEST
				HttpClient client = new DefaultHttpClient();  
				HttpGet get = new HttpGet(getURL);
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntityGet = responseGet.getEntity();
		        			*/
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
