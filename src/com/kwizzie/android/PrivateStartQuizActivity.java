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
import android.widget.Toast;

import com.kwizzie.model.AudioQuestion;
import com.kwizzie.model.MCQAnswerType;
import com.kwizzie.model.PictureQuestion;
import com.kwizzie.model.PrivateQuizRoom;
import com.kwizzie.model.QRAnswerType;
import com.kwizzie.model.QRQuestion;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionCategory;
import com.kwizzie.model.QuestionType;
import com.kwizzie.model.QuizRoom;
import com.kwizzie.model.TextAnswerType;
import com.kwizzie.model.TextQuestion;
import com.kwizzie.model.VideoQuestion;

import flexjson.JSONDeserializer;

public class PrivateStartQuizActivity extends Activity {
	
	ArrayList<Question> questions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_start_quiz);
		ArrayList<String> optionList = new ArrayList<String>();
		optionList.add("disney");
		optionList.add("dreamworks");
		Question ques = new PictureQuestion("http://img4.wikia.nocookie.net/__cb20130222060253/disney/images/7/7d/2013disneyprincess.jpg",null, new QuestionCategory("GK","General Knowledge"),
				"Identify The picture",new MCQAnswerType(optionList, 0, null),false);
	
		Question ques2 = new TextQuestion("contin___s",null,null,"Fill in the missing characters",new TextAnswerType("uou",null),false);
		ArrayList<String> options3 = new ArrayList<String>();
		options3.add("sonu nigam");
		options3.add("arijit singh");
		options3.add("mika singh");
		options3.add("honney singh");
		
		Question ques3 = new AudioQuestion("http://www.largesound.com/ashborytour/sound/brobob.mp3", null, null, "Identify the singer", new MCQAnswerType(options3, 2, null), false);
		Question ques4 = new VideoQuestion("http://info.sonicretro.org/images/5/54/Tiger_Sonic_3D_Blast_Ad.flv", null, null, "Identify the actor in video", new TextAnswerType("Ranbeer Kapoor",null), false);
		Question ques5 = new QRQuestion(null, null, "scan the qr code", new QRAnswerType("testqr", null), false);
		questions=new ArrayList<Question>();
		questions.add(ques5);
		questions.add(ques);
		questions.add(ques2);
		questions.add(ques3);
		questions.add(ques4);
		
		QuizRoom quizRoom = new PrivateQuizRoom(questions, null, null, "Bits Quiz Room");
		Intent intent = new Intent(this,QuestionType.valueOf(questions.get(0).getTypeOfQuestion()).getQuestionType());
		intent.putExtra("questionNumber",0);
		intent.putParcelableArrayListExtra("questions", questions);
		intent.putExtra("quizRoomName",quizRoom.getRoomName());
		intent.putExtra("quizRoomID",quizRoom.getRoomID());
		intent.putExtra("playerScore",0);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.private_start_quiz, menu);
		return true;
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
					JSONDeserializer<PrivateQuizRoom> des = new JSONDeserializer<PrivateQuizRoom>();
					PrivateQuizRoom room = des.deserialize(response);
					Log.i("parse", "parse successful");
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
