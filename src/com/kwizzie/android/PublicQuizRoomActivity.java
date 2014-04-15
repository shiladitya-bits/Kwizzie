package com.kwizzie.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.kwizzie.android.adapter.CategoryListAdapter;
import com.kwizzie.model.QuestionCategory;

import flexjson.JSONDeserializer;

public class PublicQuizRoomActivity extends Activity {

	ListView categoriesListView;
	List<QuestionCategory> categories;
	CategoryListAdapter categoryAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_quiz_room);
		categoriesListView = (ListView) findViewById(R.id.categoryList);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.public_quiz_room, menu);
		return true;
	}

	private class DownloadData extends AsyncTask<String, Void, String> {
		String response;
		Activity activity;
		public DownloadData(Activity activity){
			this.activity = activity;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
				Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
			}
			JSONDeserializer<List<QuestionCategory>> des = new JSONDeserializer<List<QuestionCategory>>();
			categories = des.deserialize(result);
			categoryAdapter = new CategoryListAdapter(activity,categories);
			categoriesListView.setAdapter(categoryAdapter);
		}

		@Override
		protected void onPreExecute() {	}

		@Override
		protected String doInBackground(String... arg0) {
			try{
				
				String getURL = KwizzieConsts.SERVER_URL+"quizRoom/public/categories";
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
				return null;
			}
		}
	}
}
