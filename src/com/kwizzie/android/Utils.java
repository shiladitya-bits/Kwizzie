package com.kwizzie.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class Utils {

	public static Bitmap getProfilePicture(){
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/kwizzie/profilePic.png";
		
		Bitmap output = null;
		try {
			FileInputStream fin = new FileInputStream(new File(filePath));
			return BitmapFactory.decodeStream(fin);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return output;
	}
}
