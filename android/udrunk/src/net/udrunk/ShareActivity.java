package net.udrunk;

import java.util.Observable;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.Place;
import net.udrunk.model.Model;



import android.support.v4.app.FragmentTransaction;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;

@EActivity(R.layout.share)
public class ShareActivity extends CommonActivity {

	@FragmentById(R.id.placesFragment)
	PlacesFragment placesFragment;
	@FragmentById(R.id.shareFragment)
	ShareFragment shareFragment;
	
	private Place currentPlace;

	@AfterViews
	public void afterViews() {
		placesFragment.updatePlaces();
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();  
		ft.hide(shareFragment);
		ft.commit(); 
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if(data.equals( Model.PLACES_UPDATED ))
		{
			placesFragment.updatePlaces();
		}
	}
	
	public void showShareFragment(Place place)
	{
		currentPlace = place;
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();  
		ft.hide(placesFragment);
		ft.show(shareFragment);
		ft.commit(); 
		
	}

	public void sendCheckin(String message, int level)
	{
		Checkin checkin = new Checkin();
		
		checkin.setPlace(currentPlace);
		checkin.setLevel(level);
		checkin.setStatus(message);
		checkin.setUser(model.getCurrentUser());
		
		model.insertCheckin(checkin);
		
		onBackPressed();
	}
}
