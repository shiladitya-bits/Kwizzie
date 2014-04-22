package com.kwizzie.android.adapter;

import java.util.ArrayList;
import java.util.List;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kwizzie.android.KwizzieConsts;
import com.kwizzie.android.R;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionCategory;
import com.kwizzie.model.QuestionType;

import flexjson.JSONDeserializer;

public class CategoryListAdapter extends ArrayAdapter<QuestionCategory> {

	ProgressDialog pd;
	Activity context;
	List<QuestionCategory> categories; 
	public CategoryListAdapter(Activity context,
			List<QuestionCategory> objects) {
		super(context, R.layout.category_list_row_layout, objects);
		this.context=context;
		this.categories = objects;
		pd = new ProgressDialog(context);
		
	}
	
	@Override
	public View getView(final int position , View convertView , ViewGroup parent){
		View rowView = convertView;
		if(rowView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.category_list_row_layout, null);
			CategoryItemHolder categoryHolder = new CategoryItemHolder();
			categoryHolder.setCategorynameView((TextView)rowView.findViewById(R.id.categoryname));
			
			Button startBtn = (Button)rowView.findViewById(R.id.startQuizB);
			startBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					prepareQuestions(position);
				}
			});
			rowView.setTag(categoryHolder);
		}
		CategoryItemHolder holder =(CategoryItemHolder) rowView.getTag();
		holder.getCategorynameView().setText(categories.get(position).getCategoryName());
		return rowView;
		
	}

	static class CategoryItemHolder{
		private TextView categorynameView;
		
		public TextView getCategorynameView() {
			return categorynameView;
		}
		public void setCategorynameView(TextView categorynameView) {
			this.categorynameView = categorynameView;
		}
		
	}
	
	public void prepareQuestions(int position){
		new DownloadData(context, pd, categories.get(position)).execute();
	}
		
	private class DownloadData extends AsyncTask<String, Void, String> {
		String response;
		Activity activity;
		ProgressDialog pd;
		QuestionCategory category;
		public DownloadData(Activity activity, ProgressDialog pd, QuestionCategory category){
			this.pd = pd;
			pd.show();
			this.activity = activity;
			this.category = category;
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
					JSONDeserializer<ArrayList<Question>> des = new JSONDeserializer<ArrayList<Question>>();
					ArrayList<Question> questions = des.deserialize(response);
					
					Intent intent = new Intent(activity,QuestionType.valueOf(questions.get(0).getTypeOfQuestion()).getQuestionType());
					intent.putExtra("questionNumber",0);
					intent.putParcelableArrayListExtra("questions", questions);
					intent.putExtra("quizRoomName", "public");
					intent.putExtra("quizRoomID", "public");
					intent.putExtra("category", category);
					intent.putExtra("playerScore",0);
					activity.startActivity(intent);
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
			String categoryCode = category.getCategoryCode();
			try{
				
				String getURL = KwizzieConsts.SERVER_URL+"quizRoom/public?"+"category="+categoryCode;
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

	public boolean isNetworkAvailable() 
	{
        ConnectivityManager cm = (ConnectivityManager) 
          context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
