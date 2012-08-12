package net.udrunk;

import java.util.List;

import net.udrunk.adapters.PlaceAdapater;
import net.udrunk.domain.Place;
import net.udrunk.domain.dto.AllPlacesDto;
import net.udrunk.services.UdrunkClient;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.RestService;

@EFragment(R.layout.places)
public class PlacesFragment extends SherlockFragment {

	@ViewById
	ListView listView;
	
	@RestService
	public UdrunkClient restClient;

	@AfterViews
	public void afterViews() {
		setHasOptionsMenu(true);
		getPlaces();
	}

	@Background
	public void getPlaces() {
		AllPlacesDto places = restClient.getPlaces();
		
		updatePlaces(places.objects);
	}
	
	@UiThread
	void updatePlaces(List<Place> places) {
		
		PlaceAdapater adapter = new PlaceAdapater(getActivity(),
				R.layout.list_place_item, places);
		listView.setAdapter(adapter);
	}
}
