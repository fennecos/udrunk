package net.udrunk;

import java.io.IOException;

import net.udrunk.infra.DataBaseHelper;
import net.udrunk.services.CheckinService;
import net.udrunk.services.CheckinService_;
import net.udrunk.services.UdrunkClient;
import net.udrunk.services.UdrunkClient_;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public abstract class CommonActivity extends SherlockFragmentActivity {

	protected UdrunkClient restClient;

	private DataBaseHelper databaseHelper;

	private Messenger checkinServiceMessenger = null;

	private Messenger inMessenger = new Messenger(new IncomingHandler());

	protected boolean mIsBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		restClient = new UdrunkClient_();
		initAuth();
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

	public void initAuth() {

		// This should of course be extracted into a separate class
		ClientHttpRequestInterceptor authInterceptor = new ClientHttpRequestInterceptor() {

			@Override
			public ClientHttpResponse intercept(HttpRequest request,
					byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				HttpHeaders headers = request.getHeaders();

				String username = "valentin";
				String key = "valentin";
				headers.set("Authorization", "ApiKey " + username + ":" + key);

				return execution.execute(request, body);
			}
		};

		RestTemplate rt = restClient.getRestTemplate();

		ClientHttpRequestInterceptor[] interceptors = { authInterceptor };
		rt.setInterceptors(interceptors);

	}

	@Override
	protected void onResume() {
		super.onResume();
		doBindService();
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		doUnbindService();
	}

	void doBindService() {
		mIsBound = true;
		bindService(new Intent(this, CheckinService_.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	void doUnbindService() {
		if (mIsBound) {
			// If we have received the service, and hence registered with
			// it, then now is the time to unregister.
			if (checkinServiceMessenger != null) {
				try {
					Message msg = Message.obtain(null,
							CheckinService.MSG_UNREGISTER_CLIENT);
					msg.replyTo = inMessenger;
					checkinServiceMessenger.send(msg);
				} catch (RemoteException e) {
					// There is nothing special we need to do if the service
					// has crashed.
				}
			}

			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
			Toast.makeText(this, "Unbinding", Toast.LENGTH_SHORT);
		}
	}

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CheckinService.MSG_GET_CHECKINS:
				getUdrunkApplication().checkinsLoading = false;
				Toast.makeText(CommonActivity.this, "Checkin retieved",
						Toast.LENGTH_SHORT).show();
				onCheckinsRetieved();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			checkinServiceMessenger = new Messenger(binder);

			try {
				Message msg = Message.obtain(null,
						CheckinService.MSG_REGISTER_CLIENT);
				msg.replyTo = inMessenger;
				checkinServiceMessenger.send(msg);
				onCheckinsServiceConnected();

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			checkinServiceMessenger = null;
		}
	};

	public void retrieveCheckins() {
		if (!getUdrunkApplication().checkinsLoading) {
			getUdrunkApplication().checkinsLoading = true;
			Toast.makeText(this, "Retieving Checkins", Toast.LENGTH_SHORT)
					.show();
			Message msg = Message.obtain(null, CheckinService.MSG_GET_CHECKINS);
			try {
				checkinServiceMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public UdrunkClient getRestClient()
	{
		return restClient;
	}

	abstract public void onCheckinsRetieved();

	abstract public void onCheckinsServiceConnected();
	
	abstract public void onPlacesRetieved();
	
}
