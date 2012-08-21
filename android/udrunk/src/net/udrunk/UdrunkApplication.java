package net.udrunk;

import android.app.Application;

import com.google.android.imageloader.ImageLoader;

public class UdrunkApplication extends Application {

	public static ImageLoader imageLoader = new ImageLoader();
	
	public static final String API_DOMAIN = "http://udrunk.valentinbourgoin.net";
	
}
