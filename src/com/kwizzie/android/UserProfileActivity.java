package com.kwizzie.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kwizzie.model.Player;

import flexjson.JSONDeserializer;

public class UserProfileActivity extends Activity {

	Player player;
	Map<String , Integer> privateQuizScores;
	Map<String , Integer> publicQuizScores;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		String username = getIntent().getExtras().getString("playerID");
		pd = new ProgressDialog(this);
		pd.setMessage("Loading user data");
		pd.setCancelable(false);
		final DownloadData task = new DownloadData();
		task.execute(username);

	}

	public void prepareViews(){

	    privateQuizScores = player.getQuizRoomScores();
	    publicQuizScores= player.getPublicRoomScores();
	    ListView privateQuizListView = (ListView) findViewById(R.id.lvPrivateScores);
	    ListView publicQuizListView = (ListView) findViewById(R.id.lvPublicScores); 
	    
	    TextView noDataMsg = new TextView(this);
	    noDataMsg.setText("No quizzes played");
	    noDataMsg.setTextColor(getResources().getColor(R.color.dark_grey));

	    privateQuizListView.setEmptyView(noDataMsg);
	    publicQuizListView.setEmptyView(noDataMsg);
	    
	    if(privateQuizScores!= null && !privateQuizScores.isEmpty()){
		    List<Map<String, String>> privateListData = new ArrayList<Map<String, String>>();
		    for(Map.Entry<String, Integer> entry : privateQuizScores.entrySet()){
		    	Map<String, String> listRowData = new HashMap<String, String>();
		    	listRowData.put("name", entry.getKey());
		    	listRowData.put("score", String.valueOf(entry.getValue()));
		    	privateListData.add(listRowData);
		    }

		    
		    ListAdapter privateAdapter = new SimpleAdapter(this, privateListData, R.layout.quiz_score_row_layout , 
		    		new String[]{"name", "score"}, new int[]{R.id.text1, R.id.text2});
		    privateQuizListView.setAdapter(privateAdapter);	    	
	    }
	    
	    if(publicQuizScores!= null && !publicQuizScores.isEmpty()){
		    List<Map<String, String>> publicListData = new ArrayList<Map<String, String>>();
		    for(Map.Entry<String, Integer> entry : publicQuizScores.entrySet()){
		    	Map<String, String> listRowData = new HashMap<String, String>();
		    	listRowData.put("name", entry.getKey());
		    	listRowData.put("score", String.valueOf(entry.getValue()));
		    	publicListData.add(listRowData);
		    }
		    ListAdapter publicAdapter = new SimpleAdapter(this, publicListData, R.layout.quiz_score_row_layout, 
		    		new String[]{"name", "score"}, new int[]{R.id.text1, R.id.text2});
		    publicQuizListView.setAdapter(publicAdapter);
	    	
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

	private class DownloadData extends AsyncTask<String, Void, String> {
		String response;
		@Override
		protected void onPostExecute(String result) {
			pd.cancel();
			try{
				JSONDeserializer<Player> des = new JSONDeserializer<Player>();
				player = des.deserialize(result);
				prepareViews();
			} catch(Exception e){
				Toast.makeText(UserProfileActivity.this, "Error in retrieving user information", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				UserProfileActivity.this.finish();
				
			}
		}

		@Override
		protected void onPreExecute() {		
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) {
			try{
				String  username = args[0];
				String getURL = KwizzieConsts.SERVER_URL+"player/"+username;
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
