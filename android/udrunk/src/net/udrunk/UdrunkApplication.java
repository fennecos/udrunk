package net.udrunk;

import net.udrunk.domain.User;
import android.app.Application;

import com.google.android.imageloader.ImageLoader;

public class UdrunkApplication extends Application {

	public static User currentUser;

	public static ImageLoader imageLoader = new ImageLoader();
	
	public static final String API_DOMAIN = "http://udrunk.valentinbourgoin.net"; 
}
