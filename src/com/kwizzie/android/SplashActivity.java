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
import android.view.View;
import android.widget.Toast;

import com.kwizzie.model.PrivateQuizRoom;

import flexjson.JSONDeserializer;

public class SplashActivity extends Activity {

	PrivateQuizRoom quizRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
    
    public void onClick(View v){
    	switch(v.getId()){
    	case R.id.loginBtn :
    		Intent intentLogin = new Intent(this,LoginActivity.class);
    		startActivity(intentLogin);
    	break;
    	case R.id.signUpBtn :
    		Intent intentSignUp = new Intent(this,RegistrationActivity.class);
    		startActivity(intentSignUp);
    	break;	
    	}
    }
}
