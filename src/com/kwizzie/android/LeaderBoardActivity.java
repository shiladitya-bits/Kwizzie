package com.kwizzie.android;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import com.kwizzie.android.adapter.LeaderBoardAdapter;
import com.kwizzie.model.Leader;
import flexjson.JSONDeserializer;

public class LeaderBoardActivity extends Activity {

	String response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);
		String roomID = getIntent().getExtras().get("roomID").toString();
		new DownloadData(this, roomID).execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leader_board, menu);
		return true;
	}
	private class DownloadData extends AsyncTask<String, Void, List<Leader>> {
		
		String roomID;
		Activity activity;
		public DownloadData(Activity activity , String roomID){
			this.activity = activity;
			this.roomID = roomID;
		}
		
		@Override
		protected void onPostExecute(List<Leader> leaders) {
			if(leaders != null){
				ListView leaderboardList = (ListView) activity.findViewById(R.id.leaderboardList);
				LeaderBoardAdapter adapter = new LeaderBoardAdapter(activity, leaders, getIntent().getExtras().getString("category"));
				leaderboardList.setAdapter(adapter);	
			}
		}

		@Override
		protected void onPreExecute() {		}

		@Override
		protected List<Leader> doInBackground(String... args) {
			
			try{
				String getURL = KwizzieConsts.SERVER_URL+"leaders?roomID="+roomID;
				Log.i("server url",getURL);
	
				HttpClient client = new DefaultHttpClient();  
				HttpGet get = new HttpGet(getURL);
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntityGet = responseGet.getEntity();
		        			
			if (resEntityGet != null) 
	        {  
				response = EntityUtils.toString(resEntityGet);
	            Log.d("response", response);	
	            JSONDeserializer<List<Leader>> des = new JSONDeserializer<List<Leader>>();
	            List<Leader> leaders= des.deserialize(response);
				return leaders;
	        }
			else return null;
			}
			catch(Exception e){
				return null;
			}
		}
		
	}
}
