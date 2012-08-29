package net.udrunk;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.login)
public class LoginActivity extends CommonActivity {
	
	@ViewById(R.id.edit_login)
	protected EditText loginEdit;
	
	@ViewById(R.id.edit_pass)
	protected EditText passEdit;
	
	@ViewById(R.id.btn_login)
	protected Button loginButton;
	
	@Click(R.id.btn_login)
	protected void onLoginClicked()
	{
		Intent intent = new Intent(this, UdrunkActivity_.class);
		startActivity(intent);
		finish();
	}
	
	@AfterInject
	protected void afterInjection()
	{
	}
}
