package net.udrunk;

import java.sql.SQLException;
import java.util.List;

import net.udrunk.adapters.CheckinAdapter;
import net.udrunk.domain.Checkin;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
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
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					true);
			
			getUdrunkActivity().retrieveCheckins();
			
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}

	@UiThread
	public void updateCheckins() {
		List<Checkin> checkins = null;
		try {
			checkins = getCheckinDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CheckinAdapter adapter = new CheckinAdapter(getActivity(),
				R.layout.list_feed_item, checkins);
		listView.setAdapter(adapter);
	}

	protected UdrunkActivity getUdrunkActivity() {
		return (UdrunkActivity) getActivity();
	}

	protected Dao<Checkin, Integer> getCheckinDao() throws SQLException {
		return getUdrunkActivity().getDBHelper().getCheckinDao();
	}
}
