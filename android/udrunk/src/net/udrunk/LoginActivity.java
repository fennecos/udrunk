package net.udrunk;

import java.util.Observable;

import net.udrunk.domain.Login;
import net.udrunk.model.Model;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
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
		model.login(loginEdit.getText().toString(), passEdit.getText().toString());
	}
	
	@AfterViews
	protected void afterViews()
	{
		Login login = model.getCurrentLogin();
		if(login != null)
		{
			loginEdit.setText(login.getUsername());
		}
	}
	
	@AfterInject
	protected void afterInjection()
	{
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
		if (data.equals(Model.LOGIN_SUCCESS)) {
			Intent intent = new Intent(this, UdrunkActivity_.class);
			startActivity(intent);
			finish();
		}
		if (data.equals(Model.LOGIN_FAILED)) {
			Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
		}
	}
}
