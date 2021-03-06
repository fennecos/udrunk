package net.udrunk;

import java.util.List;

import net.udrunk.domain.Place;
import net.udrunk.infra.FixedMyLocationOverlay;
import android.content.Intent;
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
import com.googlecode.androidannotations.annotations.Click;
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

	private FixedMyLocationOverlay myLocationOverlay;

	@AfterViews
	public void afterViews() {
		txtName.setText(currentPlace.getName());
		txtCity.setText(currentPlace.getCity());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(R.string.place_title);

		GeoPoint point = getPoint(currentPlace.getGeometry().y, currentPlace.getGeometry().x);

		mapView.setBuiltInZoomControls(true);
		mapView.getController().setCenter(point);
		mapView.getController().setZoom(17);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.drink_martini);
		HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(
				drawable, this);

		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
				currentPlace.getName());
		itemizedoverlay.addOverlay(overlayitem);

		myLocationOverlay = new FixedMyLocationOverlay(
				this, mapView);
		myLocationOverlay.enableMyLocation();

		mapOverlays.add(itemizedoverlay);
		mapOverlays.add(myLocationOverlay);

		mapView.postInvalidate();
		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapView.getController().animateTo(
						myLocationOverlay.getMyLocation());
			}
		});

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
	
	@Click(R.id.btn_iamhere)
	protected void iAmHereClicked()
	{
		Intent intent = new Intent(this, ShareActivity_.class);
		intent.putExtra("place_extra", currentPlace);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}
}
