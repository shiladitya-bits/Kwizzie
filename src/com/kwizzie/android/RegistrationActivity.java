package com.kwizzie.android;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RegistrationActivity extends Activity {

	EditText etUsername;
	EditText etName;
	EditText etEmailId;
	EditText etPassword;
	EditText etRetypePassword;
	Uri profileImageURI;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		// Show the Up button in the action bar.
		setupActionBar();
		pd = new ProgressDialog(this);
		pd.setMessage("Registering");
		pd.setCancelable(false);
		etUsername = (EditText)findViewById(R.id.usernameText);
		etName = (EditText)findViewById(R.id.nameText);
		etEmailId = (EditText)findViewById(R.id.emailText);
		etPassword = (EditText)findViewById(R.id.passwordText);
		etRetypePassword = (EditText)findViewById(R.id.retypePasswordText);
		
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
		switch(v.getId()){
		case R.id.btnBrowse:
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(i, "Select Profile Picture"), 1);
			break;
		case R.id.signUpB:
			String username = etUsername.getText().toString();
			String name = etName.getText().toString();
			String emailID = etEmailId.getText().toString();
			String password = etPassword.getText().toString();
			String retypePassword = etRetypePassword.getText().toString();
			if(username.trim().length() ==0){
				etUsername.setError("Username cannot be empty");
				etUsername.requestFocus();
				return;
			}
			if(name.trim().length() ==0){
				etName.setError("Username cannot be empty");
				etName.requestFocus();
				return;			
			}
			
			if(emailID.trim().length() ==0){
				etEmailId.setError("Username cannot be empty");
				etEmailId.requestFocus();
				return;
			}
			if(password.trim().length() == 0){
				etPassword.setError("Password cannot be empty");
				etPassword.requestFocus();
				return;
			}
			if(!password.trim().equals(retypePassword)){
				etRetypePassword.setError("Passwords do not match!");
				etRetypePassword.requestFocus();
				return;
			}
			new DownloadData(this).execute(username , name , emailID ,password);
			break;
		}

		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == 1){
				profileImageURI = data.getData();
			}
		}
	}


	public String getRealPathFromUri(Uri contentUri){
		String proj[] = {MediaStore.Images.Media.DATA};
		android.database.Cursor cursor = managedQuery(contentUri, proj, null,null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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
			pd.cancel();
			if(result.equals("1")){
				Intent intent = new Intent(activity , RegistrationCompleteActivity.class);
				startActivity(intent);
				finish();
			} else {
				updateUI();
			}
		}

		@Override
		protected void onPreExecute() {	
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) {
			try{
				String username = args[0];
				String name = args[1];
				String emailID = args[2];
				String password  = args[3];
				File image = null;
				HttpClient httpClient;
				HttpPost httppost;
				
				if(profileImageURI != null){
					String getURL = KwizzieConsts.SERVER_BASE_URL+"RegistrationServlet";
					Log.i("server url",getURL);
					httpClient = new DefaultHttpClient();				
					httppost = new HttpPost(getURL);				
					image = new File(getRealPathFromUri(profileImageURI));
					MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					mpEntity.addPart("image", new FileBody(image, "application/octet"));
					mpEntity.addPart("username", new StringBody(username));
					mpEntity.addPart("name", new StringBody(name));
					mpEntity.addPart("emailID", new StringBody(emailID));
					mpEntity.addPart("password", new StringBody(password));

					httppost.setEntity(mpEntity);

				} else {
					String getURL = SERVER_URL;
					Log.i("server url",getURL);
					httpClient = new DefaultHttpClient();				
					httppost = new HttpPost(getURL);				
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("username", username));
					nameValuePairs.add(new BasicNameValuePair("name", name));
					nameValuePairs.add(new BasicNameValuePair("emailID", emailID));
					nameValuePairs.add(new BasicNameValuePair("password", password));

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
				}
					
				HttpResponse httpResponse = httpClient.execute(httppost);
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
