package com.kwzzie.location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kwizzie.android.KwizzieConsts;
import com.kwizzie.android.R;
import com.kwizzie.android.timer.QuestionTimer;
import com.kwizzie.model.QuestionLocation;

public class QuestionLocationListener implements LocationListener{

	Activity activity;
	QuestionLocation questionLocation;
	Location destination;
	View quesLockLayout;
	QuestionTimer timer;
	CountDownTimer unlockedTimer;
	
	public QuestionLocationListener(Activity activity, QuestionLocation questionLocation , View quesLockLayout, QuestionTimer timer){
		this.questionLocation = questionLocation;
		destination = new Location(LocationManager.GPS_PROVIDER);
		destination.setLatitude(questionLocation.getLatitude());
		destination.setLongitude(questionLocation.getLongitute());
		this.quesLockLayout =quesLockLayout;
		this.timer = timer;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.i("currentLocation" , String.valueOf(location.getLatitude()));
		Log.i("currentLocation" , String.valueOf(location.getLongitude()));
		double radius = questionLocation.getRadius();
		float distance = location.distanceTo(destination);
		TextView tvDist = (TextView)quesLockLayout.findViewById(R.id.tvDestDistance);
		tvDist.setText(String.valueOf(distance)+" metres");
		unlockedTimer = new CountDownTimer(2000, 2000) {
			
			@Override
			public void onTick(long millisUntilFinished) {}
			
			@Override
			public void onFinish() {
				
				quesLockLayout.setVisibility(View.GONE);
				timer.start();
			}
		};

		if(distance < KwizzieConsts.DEFAULT_RADIUS){  //TODO: change later to question radius
			unlockedTimer.start();
			View btnView = quesLockLayout.findViewById(R.id.skipQues);
			btnView.setVisibility(View.INVISIBLE);
			/*quesLockLayout.setVisibility(View.GONE);
			timer.start();*/
			/*View quesUnlockLayout = activity.findViewById(R.id.quesUnlockLayout);
			quesLockLayout.setVisibility(View.VISIBLE);*/
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

}
