package net.udrunk;

import java.util.List;

import net.udrunk.domain.Place;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.place_details)
public class PlaceDetailsActivity extends SherlockMapActivity {

	@ViewById(R.id.txt_name)
	protected TextView txtName;

	@ViewById(R.id.txt_city)
	protected TextView txtCity;

	@ViewById(R.id.mapview)
	protected MapView mapView;
	
	@Extra("place_extra")
	Place currentPlace;

	@AfterViews
	public void afterViews() {
		txtName.setText(currentPlace.getName());
		txtCity.setText(currentPlace.getCity());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		mapView.setBuiltInZoomControls(true);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.drink_martini);
		HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
		
		GeoPoint point = getPoint(currentPlace.getLat(),currentPlace.getLong());
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", currentPlace.getName());
		
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		
		mapView.getController().setCenter(point);
		mapView.getController().setZoom(16);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
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

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}
	
	private GeoPoint getPoint(double lat, double lon) {
	    return(new GeoPoint((int)(lat*1000000.0),
	                          (int)(lon*1000000.0)));
	  }
}
