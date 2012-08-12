package net.udrunk.services;

import java.sql.SQLException;
import java.util.ArrayList;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.dto.AllCheckinsDto;
import net.udrunk.infra.DataBaseHelper;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.EService;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.j256.ormlite.android.apptools.OpenHelperManager;

@EService
public class CheckinService extends Service {

	@RestService
	UdrunkClient restClient;

	private DataBaseHelper databaseHelper;

	@Override
	public IBinder onBind(Intent intent) {
		// Return our messenger to the Activity to get commands
		return inMessenger.getBinder();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onCreate();

		getCheckins();

		return Service.START_NOT_STICKY;
	}

	public void getCheckins() {

		AllCheckinsDto checkins = restClient.getFeed();

		for (Checkin checkin : checkins.objects) {
			try {
				getDBHelper().getPlaceDao().createOrUpdate(checkin.getPlace());
				getDBHelper().getUserDao().createOrUpdate(checkin.getUser());
				getDBHelper().getCheckinDao().createOrUpdate(checkin);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		showNotification();
	}

	@UiThread
	public void showNotification() {
		Toast.makeText(this, "service checkin done", Toast.LENGTH_LONG);
	}

	protected DataBaseHelper getDBHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DataBaseHelper.class);
		}
		return databaseHelper;
	}

	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_GET_CHECKINS = 3;

	// Used to receive messages from the Activity
	final Messenger inMessenger = new Messenger(new IncomingHandler());
	/** Keeps track of all current registered clients. */
	ArrayList<Messenger> outMessengers = new ArrayList<Messenger>();

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.d("MESSAGE", "Checkin Service got message " + msg.what);

			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				outMessengers.add(msg.replyTo);

				break;
			case MSG_UNREGISTER_CLIENT:
				outMessengers.remove(msg.replyTo);

				break;
			case MSG_GET_CHECKINS:

				getCheckins();
				
				for (int i = outMessengers.size() - 1; i >= 0; i--) {
					try {
						outMessengers.get(i).send(
								Message.obtain(null, MSG_GET_CHECKINS));
					} catch (RemoteException e) {
						// The client is dead. Remove it from the list;
						// we are going through the list from back to front
						// so this is safe to do inside the loop.
						outMessengers.remove(i);
					}
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

}