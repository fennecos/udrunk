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
import com.googlecode.androidannotations.annotations.Trace;
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

	@Trace
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Trace
	@AfterViews
	public void afterViews() {
		if (fragmentList == null) {
			Fragment fragment;
			fragmentList = new ArrayList<Fragment>();
			fragment = new UserFragment_();
			fragmentList.add(fragment);
			fragment = new TimelineFragment_();
			fragmentList.add(fragment);
			fragment = new PlacesFragment_();
			fragmentList.add(fragment);
		}

		mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mMyFragmentPagerAdapter);

		viewPager.setCurrentItem(1);

		// Bind the title indicator to the adapter
		titleIndicator.setViewPager(viewPager);
		titleIndicator.setTextColor(Color.BLACK);
		titleIndicator.setSelectedColor(Color.BLACK);
		titleIndicator.setOnPageChangeListener(onPageChangeListener);
	}

	@Trace
	@Override
	protected void onResume() {
		super.onResume();
		model.retrieveCheckins();
		
		model.deletePlacesIfNecessary();
		if(viewPager.getCurrentItem() == 2)
		{
			model.retrievePlaces();
		}
		updateProgress();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		updateProgress();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout_menu_item:
			model.logout();
			Intent intent = new Intent(this, LoginActivity_.class);
			startActivity(intent);
			finish();
			return true;
		}
		return (super.onOptionsItemSelected(item));
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
	void updateProgress() {
		if (menu != null) {
			MenuItem refreshItem = menu.findItem(R.id.refresh_item_menu);

			if (refreshItem != null) {
				if (model.checkinsLoading)
					refreshItem.setActionView(R.layout.refresh_menuitem);
				else
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

	protected ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int pageSelected) {
			if (pageSelected == 2) {
				model.retrievePlaces();
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	};

	@Override
	public void update(Observable observable, Object data) {
		if (data.equals(Model.CHECKINS_UPDATING)) {
			updateProgress();
		}
		if (data.equals(Model.CHECKINS_UPDATED)) {
			updateProgress();
		}
	}

}