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
	
	@Override
	public void onCheckinsRetieved() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckinsServiceConnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlacesRetieved() {
		placesFragment.updatePlaces();
	}

}
