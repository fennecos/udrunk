package net.udrunk.model;

import java.util.List;

import net.udrunk.domain.Place;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;


@EBean(scope = Scope.Singleton)
public class Model {
	private List<Place> places;

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}
	
	public boolean checkinsLoading;
}
