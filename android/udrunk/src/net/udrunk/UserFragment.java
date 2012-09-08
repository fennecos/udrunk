package net.udrunk;

import net.udrunk.domain.User;
import net.udrunk.model.Model;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.user)
public class UserFragment extends SherlockFragment {

	private User user;
	
	@ViewById(R.id.txt_name)
	protected TextView nameText;
	
	@ViewById(R.id.txt_email)
	protected TextView emailText;
	
	@ViewById(R.id.txt_date)
	protected TextView dateText;

	@ViewById(R.id.img_avatar)
	protected ImageView avatarImg;
	
	@Bean
	protected Model model;
	
	public UserFragment() {
		super();
	}

	@AfterViews
	protected void afterViews()
	{
		nameText.setText(user.getUsername());
		emailText.setText(user.getEmail());
		model.imageLoader.bind(avatarImg, user.getAvatar(), null);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
