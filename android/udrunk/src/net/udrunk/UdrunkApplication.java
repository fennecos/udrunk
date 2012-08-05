package net.udrunk;

import com.google.android.imageloader.ImageLoader;

import net.udrunk.domain.User;
import android.app.Application;

public class UdrunkApplication extends Application {

	public static User currentUser;

	public static ImageLoader imageLoader = new ImageLoader();
	
	public static final String API_DOMAIN = "http://udrunk.valentinbourgoin.net"; 
}
