package net.udrunk.model;

import java.io.IOException;
import java.net.ContentHandler;
import java.net.URLStreamHandlerFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.Place;
import net.udrunk.domain.User;
import net.udrunk.domain.dto.AllPlacesDto;
import net.udrunk.infra.DataBaseHelper;
import net.udrunk.infra.JamendoCache;
import net.udrunk.services.CheckinService;
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

import com.google.android.imageloader.BitmapContentHandler;
import com.google.android.imageloader.ImageLoader;
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
	public static final int PLACES_UPDATING = 2;
	public static final int PLACES_UPDATED = 3;

	@RootContext
	protected Context context;

	@RestService
	protected UdrunkClient restClient;

	protected DataBaseHelper databaseHelper;

	protected List<Place> places;

	protected boolean connected;
	
	@AfterInject
	protected void afterInjection()
	{
		imageLoader = createImageLoader(context);
	}

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

	protected void initAuth() {

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

	public void retrievePlaces() {
		if (!placesLoading) {
			notifyObservers(PLACES_UPDATING);
			retrievePlacesBackground();
		}
	}

	@Background
	protected void retrievePlacesBackground() {
		placesLoading = true;
		AllPlacesDto placesDto = restClient.getPlaces();

		setPlaces(placesDto.objects);
		onPlacesRetieved();
	}

	@UiThread
	protected void onPlacesRetieved() {
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
	
	private boolean checkinUpdateRequested;

	protected boolean mIsBound = false;

	protected void doBindService() {
		mIsBound = true;
		context.bindService(new Intent(context, CheckinService.class),
				mConnection, Context.BIND_AUTO_CREATE);
	}

	protected void doUnbindService() {
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
			case CheckinService.MSG_GET_CHECKINS_FAILED:
				checkinsLoading = false;
				Toast.makeText(Model.this.context, "Checkin failed",
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
			connected = true;

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
			connected = false;
		}
	};

	@UiThread
	public void retrieveCheckins() {
		if (connected) {
			setChanged();
			notifyObservers(CHECKINS_UPDATING);

			if (!checkinsLoading) {
				checkinsLoading = true;
				Toast.makeText(context, "Retieving Checkins",
						Toast.LENGTH_SHORT).show();
				Message msg = Message.obtain(null,
						CheckinService.MSG_GET_CHECKINS);
				try {
					checkinServiceMessenger.send(msg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			checkinUpdateRequested = true;
		}
	}

	protected void onCheckinsRetieved() {
		setChanged();
		notifyObservers(CHECKINS_UPDATED);
	}

	protected void onCheckinsServiceConnected() {
		if(checkinUpdateRequested)
		{
			checkinUpdateRequested = false;
			retrieveCheckins();
		}
	}

	@Background
	public void insertCheckin(Checkin checkin) {
		try {
			restClient.insertCheckin(checkin);
		} catch (RestClientException e) {
			e.printStackTrace();
			showErrorToast(e.getMessage());
		} finally {
			retrieveCheckins();
		}
	}
	
	/* 
	 * IMAGE LOADER
	 * 
	 */
	
	private static final int IMAGE_TASK_LIMIT = 3;
	
	public ImageLoader imageLoader;
	
	public static final String API_DOMAIN = "http://udrunk.valentinbourgoin.net";
	
	private static ImageLoader createImageLoader(Context context) {
        // Install the file cache (if it is not already installed)
        JamendoCache.install(context);
        
        // Just use the default URLStreamHandlerFactory because
        // it supports all of the required URI schemes (http).
        URLStreamHandlerFactory streamFactory = null;

        // Load images using a BitmapContentHandler
        // and cache the image data in the file cache.
        ContentHandler bitmapHandler = JamendoCache.capture(new BitmapContentHandler(), null);

        // For pre-fetching, use a "sink" content handler so that the
        // the binary image data is captured by the cache without actually
        // parsing and loading the image data into memory. After pre-fetching,
        // the image data can be loaded quickly on-demand from the local cache.
        ContentHandler prefetchHandler = JamendoCache.capture(JamendoCache.sink(), null);

        // Perform callbacks on the main thread
        Handler handler = null;
        
        return new ImageLoader(IMAGE_TASK_LIMIT, streamFactory, bitmapHandler, prefetchHandler,
                ImageLoader.DEFAULT_CACHE_SIZE, handler);
    }

	@UiThread
	public void showErrorToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
