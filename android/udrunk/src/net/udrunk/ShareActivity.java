package net.udrunk;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;

@EActivity(R.layout.share)
public class ShareActivity extends CommonActivity {

	@FragmentById(R.id.placesFragment)
	PlacesFragment placesFragment;

	@AfterViews
	public void afterViews() {
		placesFragment.updatePlaces();
	}
	
}
