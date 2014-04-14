package com.kwizzie.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.kwizzie.android.adapter.LeaderBoardAdapter;
import com.kwizzie.model.Leader;

public class LeaderBoardActivity extends Activity {

	List<Leader> leaders;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);
		ListView leaderboardList = (ListView) findViewById(R.id.leaderboardList);
		String roomID = getIntent().getExtras().get("roomID").toString();
		//TODO Query DB for leaderboard using roomID  and then get the leaders
		leaders = new ArrayList<Leader>();
		Leader lead1 = new Leader("name","username1",10,"http://media1.santabanta.com/full1/Cricket/Rahul%20Dravid/rah16a.jpg");
		Leader lead2 = new Leader("name123","username2",50,"http://media1.santabanta.com/full1/Cricket/Rahul%20Dravid/rah16a.jpg");
		Leader lead3 = new Leader("name234454","username3",110,"http://media1.santabanta.com/full1/Cricket/Rahul%20Dravid/rah16a.jpg");
		leaders.add(lead1);
		leaders.add(lead2);
		leaders.add(lead3);
		LeaderBoardAdapter adapter = new LeaderBoardAdapter(this, leaders,roomID);
		leaderboardList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leader_board, menu);
		return true;
	}

}
