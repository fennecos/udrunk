package net.udrunk;

import java.util.Observable;
import java.util.Observer;

import net.udrunk.model.Model;
import net.udrunk.model.Model_;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public abstract class CommonActivity extends SherlockFragmentActivity implements Observer {

	public Model model;
	public Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		model = Model_.getInstance_(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}
	
	public UdrunkApplication getUdrunkApplication()
	{
		return (UdrunkApplication) getApplication();
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
