package net.udrunk;

import net.udrunk.adapters.PlaceAdapater;
import net.udrunk.model.Model;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.places)
public class PlacesFragment extends SherlockFragment {

	@ViewById
	ListView listView;
	
	@Bean
	public Model model;
	
	@AfterViews
	public void afterViews() {
		setHasOptionsMenu(true);
		updatePlaces();
	}

	@UiThread
	void updatePlaces() {
		if(model != null)
		{
			PlaceAdapater adapter = new PlaceAdapater(getActivity(),
					R.layout.list_place_item, model.getPlaces());
			listView.setAdapter(adapter);
		}
	}
	
	public CommonActivity getCommonActivity()
	{
		return (CommonActivity)getActivity();
	}
}
