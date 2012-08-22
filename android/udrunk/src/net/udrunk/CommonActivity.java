package net.udrunk;

import java.util.Observable;
import java.util.Observer;

import net.udrunk.infra.DataBaseHelper;
import net.udrunk.model.Model;
import net.udrunk.model.Model_;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public abstract class CommonActivity extends SherlockFragmentActivity implements Observer {

	private DataBaseHelper databaseHelper;

	public Model model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		model = Model_.getInstance_(this);
		super.onCreate(savedInstanceState);
	}
	
	public UdrunkApplication getUdrunkApplication()
	{
		return (UdrunkApplication) getApplication();
	}

	protected DataBaseHelper getDBHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DataBaseHelper.class);
		}
		return databaseHelper;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		model.addObserver(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		model.deleteObserver(this);
	}

	@Override
	public void update(Observable observable, Object data) {
		
	}
}
