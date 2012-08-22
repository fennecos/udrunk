package net.udrunk.services;

import net.udrunk.domain.dto.AllPlacesDto;
import net.udrunk.model.Model;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.UiThread;

@EBean
public class PlacesBackgroundTask {
	@Bean
	public Model model;

	@Background
	public void retrievePlaces() {

		AllPlacesDto placesDto = model.restClient.getPlaces();
		
		model.setPlaces(placesDto.objects);
		updateUI();
	}

	// Notice that we manipulate the activity ref only from the UI thread
	@UiThread
	public void updateUI() {
		model.onPlacesRetieved();
	}
}
