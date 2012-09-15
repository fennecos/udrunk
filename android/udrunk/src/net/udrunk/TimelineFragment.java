package net.udrunk;

import java.util.List;
import java.util.Observable;

import net.udrunk.adapters.CheckinAdapter;
import net.udrunk.domain.Checkin;
import net.udrunk.model.Model;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.timeline)
public class TimelineFragment extends CommonFragment {

	@ViewById
	ListView listView;
	
	@Bean
	public CheckinAdapter adapter;

	@AfterViews
	public void afterViews() {
		setRetainInstance(true);
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
		case R.id.refresh_item_menu:
			model.retrieveCheckins();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}

	@UiThread
	public void updateCheckins() {
		if(model != null)
		{
			List<Checkin> checkins = model.getCheckins();
			
			adapter.updateItems(checkins);
			listView.setAdapter(adapter);
		}
	}

	protected UdrunkActivity getUdrunkActivity() {
		return (UdrunkActivity) getActivity();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data.equals(Model.CHECKINS_UPDATED)) {
			updateCheckins();
		}
	}
}
