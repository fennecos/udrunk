package net.udrunk;

import java.util.Observable;

import net.udrunk.model.Model;
import android.widget.EditText;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.new_account)
public class SignUpActivity extends CommonActivity {
	
	@ViewById(R.id.edit_login)
	protected EditText loginEdit;
	
	@ViewById(R.id.edit_pass)
	protected EditText passEdit;
	
	@ViewById(R.id.edit_email)
	protected EditText emailEdit;
	
	@Click(R.id.btn_validate)
	protected void onVlidateClicked()
	{
		model.createUser(loginEdit.getText().toString(), passEdit.getText().toString(), emailEdit.getText().toString());
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
		if (data.equals(Model.LOGIN_SUCCESS)) {
			finish();
		}
		if (data.equals(Model.LOGIN_FAILED)) {
			Toast.makeText(this, "Creation Failed", Toast.LENGTH_LONG).show();
		}
	}
}
