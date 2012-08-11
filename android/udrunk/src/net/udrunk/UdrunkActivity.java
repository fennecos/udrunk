package net.udrunk;

import java.io.IOException;
import java.util.ArrayList;

import net.udrunk.infra.DataBaseHelper;
import net.udrunk.services.UdrunkClient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Touch;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.AnimationRes;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.viewpagerindicator.TitlePageIndicator;

@EActivity(R.layout.main)
public class UdrunkActivity extends SherlockFragmentActivity {

	@ViewById
	public ViewPager viewPager;

	@ViewById
	public TitlePageIndicator titleIndicator;

	@ViewById(R.id.btn_share)
	public ImageButton shareButton;

	private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

	@RestService
	public UdrunkClient restClient;

	@AnimationRes(R.anim.share_button_anim)
	public Animation shareAnim;

	private DataBaseHelper databaseHelper;

	private static ArrayList<Fragment> fragmentList;

	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setSupportProgressBarIndeterminateVisibility(true);

		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void afterViews() {

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new TimelineFragment_());
		fragmentList.add(new PlacesFragment_());

		mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mMyFragmentPagerAdapter);

		// Bind the title indicator to the adapter
		titleIndicator.setViewPager(viewPager);
		titleIndicator.setTextColor(Color.BLACK);
		titleIndicator.setSelectedColor(Color.BLACK);
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
		findViewById(R.id.refresh).setVisibility(View.GONE);
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@UiThread
	void hideProgress() {
		findViewById(R.id.refresh).setVisibility(View.VISIBLE);
		setSupportProgressBarIndeterminateVisibility(false);
	}
	
	protected DataBaseHelper getDBHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DataBaseHelper.class);
		}
		return databaseHelper;
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

	@AfterInject
	public void initAuth() {

		// This should of course be extracted into a separate class
		ClientHttpRequestInterceptor authInterceptor = new ClientHttpRequestInterceptor() {

			@Override
			public ClientHttpResponse intercept(HttpRequest request,
					byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				HttpHeaders headers = request.getHeaders();

				String username = "valentin";
				String key = "valentin";
				headers.set("Authorization", "ApiKey " + username + ":" + key);

				return execution.execute(request, body);
			}
		};

		RestTemplate rt = restClient.getRestTemplate();

		ClientHttpRequestInterceptor[] interceptors = { authInterceptor };
		rt.setInterceptors(interceptors);

	}
}