package net.udrunk;

import java.util.ArrayList;

import net.udrunk.adapters.CheckinAdapter;
import net.udrunk.domain.Checkin;
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

@EFragment(R.layout.timeline)
public class TimelineFragment extends SherlockFragment {

	@ViewById
	ListView listView;

	@AfterViews
	public void afterViews() {
		getCheckins();
		setHasOptionsMenu(true);
	}

	@Background
	public void getCheckins() {
		AllCheckinsDto checkins = ((UdrunkActivity) getActivity()).restClient
				.getFeed();
		updateCheckins(checkins.objects);
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
			getCheckins();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}

	@UiThread
	void updateCheckins(ArrayList<Checkin> checkins) {
		net.udrunk.adapters.CheckinAdapter adapter = new CheckinAdapter(
				getActivity(), R.layout.list_feed_item, checkins);
		listView.setAdapter(adapter);
	}
}
