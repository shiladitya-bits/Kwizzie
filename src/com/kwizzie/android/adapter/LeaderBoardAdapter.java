package com.kwizzie.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.oned.rss.FinderPattern;
import com.kwizzie.android.R;
import com.kwizzie.android.adapter.CategoryListAdapter.CategoryItemHolder;
import com.kwizzie.model.Player;

public class LeaderBoardAdapter extends ArrayAdapter<Player>{

	Context context;
	List<Player> players; 
	String roomID;
	
	public LeaderBoardAdapter(Context context,
			List<Player> objects, String roomID) {
		super(context, R.layout.leaderboard_row_layout, objects);
		this.context=context;
		this.players = objects;
	}
	
	@Override
	public View getView(final int position , View convertView , ViewGroup parent){
		View rowView = convertView;
		if(rowView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.leaderboard_row_layout, null);
			LeaderboardItemHolder leaderHolder = new LeaderboardItemHolder();
			leaderHolder.setPlayerName((TextView)rowView.findViewById(R.id.playerNameTv));
			leaderHolder.setPlayerScore((TextView)rowView.findViewById(R.id.playerScoreTv));
			Button startBtn = (Button)rowView.findViewById(R.id.startQuizB);
			startBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
				}
			});
			rowView.setTag(leaderHolder);
		}
		LeaderboardItemHolder holder =(LeaderboardItemHolder) rowView.getTag();
		holder.getPlayerName().setText(players.get(position).getUserName());
		//holder.getPlayerScore().setText(players.get(position));
		return rowView;
		
	}

	static class LeaderboardItemHolder{
		private TextView playerName;
		private TextView playerScore;
		private ImageView profileImage;
		public TextView getPlayerName() {
			return playerName;
		}
		public void setPlayerName(TextView playerName) {
			this.playerName = playerName;
		}
		public TextView getPlayerScore() {
			return playerScore;
		}
		public void setPlayerScore(TextView playerScore) {
			this.playerScore = playerScore;
		}
		public ImageView getProfileImage() {
			return profileImage;
		}
		public void setProfileImage(ImageView profileImage) {
			this.profileImage = profileImage;
		}
		
	}
	

}
