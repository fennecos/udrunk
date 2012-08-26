package net.udrunk;

import java.util.Observable;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.Place;
import net.udrunk.model.Model;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

@EActivity(R.layout.share)
public class ShareActivity extends CommonActivity {

	private PlacesFragment placesFragment;

	@Extra("place_extra")
	protected Place currentPlace;

	@AfterViews
	public void afterViews() {
		if (currentPlace == null) {
			placesFragment = new PlacesFragment_();

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.fragment_container, placesFragment);
			ft.commit();

			placesFragment.updatePlaces();
		} else {
			showShareFragment(currentPlace);
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(R.string.share_title);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data.equals(Model.PLACES_UPDATED)) {
			placesFragment.updatePlaces();
		}
	}

	public void showShareFragment(Place place) {
		currentPlace = place;

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// ft.setCustomAnimations(android.R.animator.fade_in,
		// android.R.animator.fade_out);
		ft.replace(R.id.fragment_container, new ShareFragment_());
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		if (placesFragment != null)
			ft.addToBackStack(null);
		ft.commit();

	}

	public void sendCheckin(String message, int level) {
		Checkin checkin = new Checkin();

		checkin.setPlace(currentPlace);
		checkin.setLevel(level);
		checkin.setStatus(message);
		checkin.setUser(model.getCurrentUser());

		model.insertCheckin(checkin);

		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;

		}
		return (super.onOptionsItemSelected(item));
	}
}
