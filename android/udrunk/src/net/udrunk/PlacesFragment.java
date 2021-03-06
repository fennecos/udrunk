package net.udrunk;

import java.util.Observable;

import net.udrunk.adapters.PlaceAdapater;
import net.udrunk.domain.Place;
import net.udrunk.model.Model;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.places)
public class PlacesFragment extends CommonFragment {

	@ViewById
	ListView listView;

	@Bean
	public PlaceAdapater adapter;

	@ViewById
	public ProgressBar empty;

	@ViewById(R.id.bnt_getplaces)
	public Button getPlacesButton;
	
	@Trace
	@AfterViews
	public void afterViews() {
		setRetainInstance(true);
		setHasOptionsMenu(true);
		updatePlaces();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.places_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.map_item_menu:
			Intent intent = new Intent(getActivity(), LocationActivity_.class);
			startActivity(intent);
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}

	@UiThread
	void updatePlaces() {
		Log.d("updateplaces", model == null ? "null":"pas null");
		if (model != null) {
			adapter.updateItems(model.getPlaces());
			listView.setAdapter(adapter);

			updateProgress();
		}
	}

	@ItemClick
	public void listViewItemClicked(Place place) {
		if (getActivity() instanceof ShareActivity) {
			((ShareActivity) getActivity()).showShareFragment(place);
		} else {
			Intent intent = new Intent(getActivity(),
					PlaceDetailsActivity_.class);
			intent.putExtra("place_extra", place);
			startActivity(intent);
		}
	}

	@Click(R.id.bnt_getplaces)
	protected void retryClicked() {
		model.retrievePlaces();
	}

	@Trace
	public void updateProgress() {
		if (model != null) {
			if (model.isPlacesLoading()) {
				Log.d("updateprogress", "1");
				getPlacesButton.setVisibility(View.GONE);
				empty.setVisibility(View.VISIBLE);
			} else if (model.getPlaces() == null
					|| model.getPlaces().size() == 0) {
				Log.d("updateprogress", "2");
				getPlacesButton.setVisibility(View.VISIBLE);
				empty.setVisibility(View.GONE);
			} else {
				Log.d("updateprogress", "3");
				getPlacesButton.setVisibility(View.GONE);
				empty.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data.equals(Model.PLACES_UPDATING)) {
			updateProgress();
		}
		if (data.equals(Model.PLACES_UPDATED)) {
			updatePlaces();
		}
	}
}
