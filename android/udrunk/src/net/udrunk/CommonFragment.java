package net.udrunk;

import java.util.Observable;
import java.util.Observer;

import net.udrunk.model.Model;
import net.udrunk.model.Model_;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public class CommonFragment extends SherlockFragment implements Observer {

	public Model model;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		model = Model_.getInstance_(getActivity());
		model.addObserver(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		model.deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void update(Observable observable, Object data) {

	}
}
