package net.udrunk.services;

import net.udrunk.CommonActivity;
import net.udrunk.domain.dto.AllPlacesDto;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;

@EBean
public class PlacesBackgroundTask {
	@RootContext
	CommonActivity activity;

	@Background
	public void retrievePlaces() {

		AllPlacesDto placesDto = activity.getRestClient().getPlaces();
		
		activity.getUdrunkApplication().setPlaces(placesDto.objects);
		updateUI();
	}

	// Notice that we manipulate the activity ref only from the UI thread
	@UiThread
	public void updateUI() {
		activity.onPlacesRetieved();
	}
}
