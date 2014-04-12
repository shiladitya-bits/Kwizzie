package com.kwizzie.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.kwizzie.android.adapter.CategoryListAdapter;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionCategory;

public class PublicQuizRoomActivity extends Activity {

	ListView categoriesListView;
	List<QuestionCategory> categories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_quiz_room);
		//TODO query server for categories
		QuestionCategory categ1 = new QuestionCategory("GK123","general Knowledge");
		QuestionCategory categ2 = new QuestionCategory("GOT123","Game Of thrones");
		QuestionCategory categ3 = new QuestionCategory("math c324","Maths");
		categories = new ArrayList<QuestionCategory>();
		categories.add(categ1);
		categories.add(categ2);
		categories.add(categ3);
		
		CategoryListAdapter categoryAdapter = new CategoryListAdapter(this,categories);
		categoriesListView = (ListView) findViewById(R.id.categoryList);
		categoriesListView.setAdapter(categoryAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.public_quiz_room, menu);
		return true;
	}

}
