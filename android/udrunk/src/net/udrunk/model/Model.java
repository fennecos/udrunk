package net.udrunk.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.Place;
import net.udrunk.domain.User;
import net.udrunk.domain.dto.AllPlacesDto;
import net.udrunk.infra.DataBaseHelper;
import net.udrunk.services.CheckinService;
import net.udrunk.services.CheckinService_;
import net.udrunk.services.UdrunkClient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.googlecode.androidannotations.api.Scope;
import com.j256.ormlite.android.apptools.OpenHelperManager;

@EBean(scope = Scope.Singleton)
public class Model extends Observable {

	public static final int CHECKINS_UPDATING = 0;
	public static final int CHECKINS_UPDATED = 1;
	public static final int PLACES_UPDATED = 1;

	@RootContext
	public Context context;

	@RestService
	public UdrunkClient restClient;

	private DataBaseHelper databaseHelper;

	private List<Place> places;

	public User getCurrentUser() {
		User result = new User();
		result.setId(1);
		result.setUsername("valentin");
		return result;
	}

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	public List<Checkin> getCheckins() {
		try {
			return getDBHelper().getCheckinDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected DataBaseHelper getDBHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,
					DataBaseHelper.class);
		}
		return databaseHelper;
	}

	public boolean checkinsLoading;
	public boolean placesLoading;

	@AfterInject
	public void doSomethingAfterInjection() {
		initAuth();
		doBindService();
	}

	/**
	 * 
	 * INIT AUTHENTIFICATION
	 * 
	 */

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

	/**
	 * 
	 * PLACE SERVICE
	 * 
	 */

	@Background
	public void retrievePlaces() {

		if (!placesLoading) {
			placesLoading = true;
			AllPlacesDto placesDto = restClient.getPlaces();

			setPlaces(placesDto.objects);
			onPlacesRetieved();
		}
	}

	@UiThread
	public void onPlacesRetieved() {
		placesLoading = false;
		setChanged();
		notifyObservers(PLACES_UPDATED);
	}

	/**
	 * 
	 * CHECKIN SERVICE
	 * 
	 */
	private Messenger checkinServiceMessenger = null;

	private Messenger inMessenger = new Messenger(new IncomingHandler());

	protected boolean mIsBound = false;

	void doBindService() {
		mIsBound = true;
		context.bindService(new Intent(context, CheckinService_.class),
				mConnection, Context.BIND_AUTO_CREATE);
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
			context.unbindService(mConnection);
			mIsBound = false;
			Toast.makeText(context, "Unbinding", Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("HandlerLeak")
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CheckinService.MSG_GET_CHECKINS:
				checkinsLoading = false;
				Toast.makeText(Model.this.context, "Checkin retieved",
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

	@UiThread
	public void retrieveCheckins() {
		setChanged();
		notifyObservers(CHECKINS_UPDATING);

		if (!checkinsLoading) {
			checkinsLoading = true;
			Toast.makeText(context, "Retieving Checkins", Toast.LENGTH_SHORT)
					.show();
			Message msg = Message.obtain(null, CheckinService.MSG_GET_CHECKINS);
			try {
				checkinServiceMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void onCheckinsRetieved() {
		setChanged();
		notifyObservers(CHECKINS_UPDATED);
	}

	public void onCheckinsServiceConnected() {

	}

	@Background
	public void insertCheckin(Checkin checkin) {
		try {
			restClient.insertCheckin(checkin);
		} catch (RestClientException e) {
			e.printStackTrace();
			showErrorToast(e.getMessage());
		}
		finally
		{
			retrieveCheckins();
		}
	}
	
	@UiThread
	public void showErrorToast(String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
