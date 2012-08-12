package net.udrunk;

import net.udrunk.adapters.PlaceAdapater;
import net.udrunk.services.UdrunkClient;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
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
		updatePlaces();
	}

	@UiThread
	void updatePlaces() {
		
		PlaceAdapater adapter = new PlaceAdapater(getActivity(),
				R.layout.list_place_item, getCommonActivity().getUdrunkApplication().getPlaces());
		listView.setAdapter(adapter);
	}
	
	public CommonActivity getCommonActivity()
	{
		return (CommonActivity)getActivity();
	}
}
