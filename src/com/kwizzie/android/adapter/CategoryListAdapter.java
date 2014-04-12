package com.kwizzie.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kwizzie.android.R;
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

public class CategoryListAdapter extends ArrayAdapter<QuestionCategory> {

	Context context;
	List<QuestionCategory> categories; 
	public CategoryListAdapter(Context context,
			List<QuestionCategory> objects) {
		super(context, R.layout.category_list_row_layout, objects);
		this.context=context;
		this.categories = objects;
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
	
	//TODO: make backend call to server to get qs for category. 
	public void prepareQuestions(int position){
		String categoryCode = categories.get(position).getCategoryCode();
		startQuiz();
	}
	
	public void startQuiz(){
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
		ArrayList<Question> questions=new ArrayList<Question>();
		questions.add(ques5);
		questions.add(ques);
		questions.add(ques2);
		questions.add(ques3);
		questions.add(ques4);
		
		Intent intent = new Intent(context,QuestionType.valueOf(questions.get(0).getTypeOfQuestion()).getQuestionType());
		intent.putExtra("questionNumber",0);
		intent.putParcelableArrayListExtra("questions", questions);
		intent.putExtra("quizRoomName","sample Room");
		intent.putExtra("quizRoomID","");
		intent.putExtra("playerScore",0);
		context.startActivity(intent);
	}
}
