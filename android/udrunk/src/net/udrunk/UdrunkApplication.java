package net.udrunk;

import java.util.List;

import net.udrunk.domain.Place;
import net.udrunk.domain.User;
import android.app.Application;

import com.google.android.imageloader.ImageLoader;

public class UdrunkApplication extends Application {

	public static User currentUser;

	public static ImageLoader imageLoader = new ImageLoader();
	
	public static final String API_DOMAIN = "http://udrunk.valentinbourgoin.net";
	
	private List<Place> places;

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}
	
	public boolean checkinsLoading;
}
