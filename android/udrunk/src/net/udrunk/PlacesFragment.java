package net.udrunk;

import java.sql.SQLException;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.Place;
import net.udrunk.domain.User;

import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.j256.ormlite.dao.Dao;

@EFragment(R.layout.places)
public class PlacesFragment extends SherlockFragment {

	@ViewById
	ListView listView;

	@AfterViews
	public void afterViews() {
		setHasOptionsMenu(true);
		getPlaces();
	}

	@Background
	public void getPlaces() {
		
	}
	
	protected UdrunkActivity getUdrunkActivity()
	{
		return (UdrunkActivity) getActivity();
	}
	
	protected Dao<Checkin, Integer> getCheckinDao() throws SQLException {
		return getUdrunkActivity().getDBHelper().getCheckinDao();
	}
	protected Dao<Place, Integer> getPlaceDao() throws SQLException {
		return getUdrunkActivity().getDBHelper().getPlaceDao();
	}
	protected Dao<User, Integer> getUserDao() throws SQLException {
		return getUdrunkActivity().getDBHelper().getUserDao();
	}
}
