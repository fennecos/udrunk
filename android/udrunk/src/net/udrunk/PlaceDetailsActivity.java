package net.udrunk;

import net.udrunk.domain.Place;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.place_details)
public class PlaceDetailsActivity extends CommonActivity {

	@ViewById(R.id.txt_name)
	TextView txtName;

	@ViewById(R.id.txt_city)
	TextView txtCity;
	
	@Extra("place_extra")
	Place currentPlace;
	
	@AfterViews
	public void afterViews() {
		txtName.setText(currentPlace.getName());
		txtCity.setText(currentPlace.getCity());
	}
}
