package net.udrunk;

import java.util.ArrayList;
import java.util.Observable;

import net.udrunk.model.Model;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Touch;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.AnimationRes;
import com.viewpagerindicator.TitlePageIndicator;

@EActivity(R.layout.main)
public class UdrunkActivity extends CommonActivity {

	@ViewById
	public ViewPager viewPager;

	@ViewById
	public TitlePageIndicator titleIndicator;

	@ViewById(R.id.btn_share)
	public ImageButton shareButton;

	private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

	@AnimationRes(R.anim.share_button_anim)
	public Animation shareAnim;

	private static ArrayList<Fragment> fragmentList;

	private TimelineFragment timelineFragment;
	private PlacesFragment placesFragment;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void afterViews() {

		fragmentList = new ArrayList<Fragment>();
		timelineFragment = new TimelineFragment_();
		fragmentList.add(timelineFragment);
		placesFragment = new PlacesFragment_();
		fragmentList.add(placesFragment);

		mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mMyFragmentPagerAdapter);

		// Bind the title indicator to the adapter
		titleIndicator.setViewPager(viewPager);
		titleIndicator.setTextColor(Color.BLACK);
		titleIndicator.setSelectedColor(Color.BLACK);

		model.retrieveCheckins();
		model.retrievePlaces();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (model.checkinsLoading) {
			showProgress();
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Touch(R.id.btn_share)
	void shareTouched(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			shareButton.startAnimation(shareAnim);
			break;
		case MotionEvent.ACTION_UP:
			Intent intent = new Intent(this, ShareActivity_.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@UiThread
	void showProgress() {
		if(menu != null)
		{
			MenuItem refreshItem = menu.findItem(R.id.refresh);

			if (refreshItem != null) {
				refreshItem.setActionView(R.layout.refresh_menuitem);
			}
		}
	}

	@UiThread
	void hideProgress() {
		if(menu != null)
		{
			MenuItem refreshItem = menu.findItem(R.id.refresh);

			if (refreshItem != null) {
				refreshItem.setActionView(null);
			}
		}
	}

	protected class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {

			return fragmentList.get(index);
		}

		@Override
		public int getCount() {

			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Resources res = UdrunkActivity.this.getResources();
			String[] titles = res.getStringArray(R.array.viewpager_titles);
			return titles[position];
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data.equals(Model.CHECKINS_UPDATING)) {
			showProgress();
		}
		if (data.equals(Model.CHECKINS_UPDATED)) {
			timelineFragment.updateCheckins();
			hideProgress();
		}
		if (data.equals(Model.PLACES_UPDATED)) {
			placesFragment.updatePlaces();
		}
	}

}