
package com.kwizzie.android;

public interface KwizzieConsts {
	String SERVER_URL = "http://192.168.43.245:8080/KwizzieServer/kwizzie/";
	//String SERVER_URL = "http://ec2-54-187-75-194.us-west-2.compute.amazonaws.com:8080/KwizzieServer/kwizzie/";
	
	String SERVER_BASE_URL = "http://192.168.43.245:8080/KwizzieServer/";
	//String SERVER_BASE_URL = "http://ec2-54-187-75-194.us-west-2.compute.amazonaws.com:8080/KwizzieServer/";
	
	long MINIMUM_TIME_BETWEEN_UPDATE = 1000;
	float MINIMUM_DISTANCECHANGE_FOR_UPDATE = 0.001f;
	int QUESTION_TIME_LIMIT = 20000;
	int QR_QUESTION_TIME_LIMIT = 100000;
	int POST_ANSWER_DELAY = 2000;
}
