package com.kwzzie.location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.kwizzie.android.timer.QuestionTimer;
import com.kwizzie.model.QuestionLocation;


public class QuestionLocationListener implements LocationListener{

	Activity activity;
	QuestionLocation questionLocation;
	Location destination;
	View quesLockLayout;
	QuestionTimer timer;
	
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
		double radius = questionLocation.getRadius();
		float distance = location.distanceTo(destination);
		if(distance < radius){
			quesLockLayout.setVisibility(View.GONE);
			timer.start();
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
