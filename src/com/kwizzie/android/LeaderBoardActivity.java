package com.kwizzie.android;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.kwizzie.android.adapter.LeaderBoardAdapter;
import com.kwizzie.model.Player;

public class LeaderBoardActivity extends Activity {

	List<Player> players;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);
		ListView leaderboardList = (ListView) findViewById(R.id.leaderboardList);
		String roomID = getIntent().getExtras().get("roomID").toString();
		LeaderBoardAdapter adapter = new LeaderBoardAdapter(this, players,roomID );
		leaderboardList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leader_board, menu);
		return true;
	}

}
