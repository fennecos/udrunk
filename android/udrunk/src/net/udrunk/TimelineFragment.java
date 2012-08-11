package net.udrunk;

import java.sql.SQLException;
import java.util.List;

import net.udrunk.adapters.CheckinAdapter;
import net.udrunk.domain.Checkin;
import net.udrunk.domain.Place;
import net.udrunk.domain.User;
import net.udrunk.domain.dto.AllCheckinsDto;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.j256.ormlite.dao.Dao;

@EFragment(R.layout.timeline)
public class TimelineFragment extends SherlockFragment {

	@ViewById
	ListView listView;

	@AfterViews
	public void afterViews() {
		setHasOptionsMenu(true);
		updateCheckins();
		getCheckins();
	}

	@Background
	public void getCheckins() {
		getUdrunkActivity().showProgress();
		
		AllCheckinsDto checkins = ((UdrunkActivity) getActivity()).restClient
				.getFeed();
		
		for(Checkin checkin : checkins.objects)
		{
			try {
				getPlaceDao().createOrUpdate(checkin.getPlace());
				getUserDao().createOrUpdate(checkin.getUser());
				getCheckinDao().createOrUpdate(checkin);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		updateCheckins();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.feed_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
			getCheckins();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@UiThread
	void updateCheckins() {
		List<Checkin> checkins = null;
		try {
			checkins = getCheckinDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CheckinAdapter adapter = new CheckinAdapter(getActivity(), R.layout.list_feed_item, checkins);
		listView.setAdapter(adapter);
		
		getUdrunkActivity().hideProgress();
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
