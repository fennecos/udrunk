package net.udrunk;

import java.util.ArrayList;

import net.udrunk.adapters.CheckinAdapter;
import net.udrunk.domain.Checkin;
import net.udrunk.domain.dto.AllCheckinsDto;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.timeline)
public class TimelineFragment extends Fragment {

	@ViewById
	ListView listView;

	@AfterViews
	public void afterViews() {
		getCheckins();
	}

	@Background
	public void getCheckins() {
		AllCheckinsDto checkins = ((UdrunkActivity) getActivity()).restClient
				.getFeed();
		updateCheckins(checkins.objects);
	}

	@UiThread
	void updateCheckins(ArrayList<Checkin> checkins) {
		net.udrunk.adapters.CheckinAdapter adapter = new CheckinAdapter(
				getActivity(), R.layout.list_feed_item, checkins);
		listView.setAdapter(adapter);
	}
}
