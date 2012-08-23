package net.udrunk;

import android.widget.EditText;
import android.widget.SeekBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.sharefragment)
public class ShareFragment extends SherlockFragment {

	@ViewById
	public SeekBar levelSeekBar;

	@ViewById
	public EditText messageEdit;
	
	@AfterViews
	public void afterViews() {
		setHasOptionsMenu(true);
		
	}
	
	@Click(R.id.commitBtn)
	public void onCommitBtnClicked()
	{
		((ShareActivity)getActivity()).sendCheckin(messageEdit.getText().toString(), levelSeekBar.getProgress());
	}
}
