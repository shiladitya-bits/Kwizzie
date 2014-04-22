package com.kwizzie.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwizzie.android.R;
import com.kwizzie.android.UserProfileActivity;
import com.kwizzie.model.Leader;

public class LeaderBoardAdapter extends ArrayAdapter<Leader>{

	Context context;
	List<Leader> leaders; 
	String roomID;
	
	public LeaderBoardAdapter(Context context,
			List<Leader> objects, String roomID) {
		super(context, R.layout.leaderboard_row_layout, objects);
		if(objects == null){
			objects = new ArrayList<Leader>();
		}
		this.context=context;
		this.leaders = objects;
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
			leaderHolder.setProfileImage((ImageView)rowView.findViewById(R.id.profile_pic_imageview));
			
			rowView.setTag(leaderHolder);
			rowView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Leader leader = leaders.get(position);
					//String username = leader.getUsername();
					//TODO Server call to get player using username
					//Player player = new Player("dravid123","Dravids","Rahul Dravid","dravid@gmail.com");
					Intent intent = new Intent(context,UserProfileActivity.class);
					intent.putExtra("playerID", leaders.get(position).getUsername());
					context.startActivity(intent);
				}
			});
		}
		LeaderboardItemHolder holder =(LeaderboardItemHolder) rowView.getTag();
		holder.getPlayerName().setText(leaders.get(position).getUsername());
		holder.getPlayerScore().setText(String.valueOf(leaders.get(position).getScore()));
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
