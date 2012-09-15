package net.udrunk;

import net.udrunk.domain.User;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.user)
public class UserFragment extends CommonFragment {

	private User user;
	
	@ViewById(R.id.txt_name)
	protected TextView nameText;
	
	@ViewById(R.id.txt_email)
	protected TextView emailText;
	
	@ViewById(R.id.txt_date)
	protected TextView dateText;

	@ViewById(R.id.img_avatar)
	protected ImageView avatarImg;
	
	public UserFragment() {
		super();
	}

	@AfterViews
	protected void afterViews()
	{
		setRetainInstance(true);
		user = model.getCurrentUser();
		nameText.setText(user.getUsername());
		emailText.setText(user.getEmail());
		model.imageLoader.bind(avatarImg, user.getAvatar(), null);
	}

}
