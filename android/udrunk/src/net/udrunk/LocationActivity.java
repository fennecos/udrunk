package net.udrunk;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity(R.layout.map)
public class LocationActivity extends SherlockMapActivity {

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
