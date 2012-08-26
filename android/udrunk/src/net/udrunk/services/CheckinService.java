package net.udrunk.services;

import java.sql.SQLException;
import java.util.ArrayList;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.dto.AllCheckinsDto;
import net.udrunk.infra.DataBaseHelper;

import org.springframework.web.client.RestClientException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class CheckinService extends Service {

	UdrunkClient restClient;

	private DataBaseHelper databaseHelper;

	@Override
	public void onCreate() {
		super.onCreate();

		restClient = new UdrunkClient_();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Return our messenger to the Activity to get commands
		return inMessenger.getBinder();
	}

	public void getCheckins() {

		try {
			AllCheckinsDto checkins = restClient.getFeed();

			for (Checkin checkin : checkins.objects) {
				try {
					getDBHelper().getPlaceDao().createOrUpdate(
							checkin.getPlace());
					getDBHelper().getUserDao()
							.createOrUpdate(checkin.getUser());
					getDBHelper().getCheckinDao().createOrUpdate(checkin);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (RestClientException e) {
			sendOutMessageUIThread(MSG_GET_CHECKINS_FAILED);
			return;
		}

		sendOutMessage(MSG_GET_CHECKINS);
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
	public static final int MSG_GET_CHECKINS_FAILED = 4;

	// Used to receive messages from the Activity
	final Messenger inMessenger = new Messenger(new IncomingHandler());
	/** Keeps track of all current registered clients. */
	ArrayList<Messenger> outMessengers = new ArrayList<Messenger>();

	@SuppressLint("HandlerLeak")
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
				getCheckinsBackground();
				break;
			default:
				super.handleMessage(msg);
			}
		}

	}

	public void sendOutMessage(int messageId) {
		for (int i = outMessengers.size() - 1; i >= 0; i--) {
			try {
				outMessengers.get(i).send(Message.obtain(null, messageId));
			} catch (RemoteException e) {
				// The client is dead. Remove it from the list;
				// we are going through the list from back to front
				// so this is safe to do inside the loop.
				outMessengers.remove(i);
			}
		}
	}
	
	
	private Handler handler_ = new Handler();
	
    public void sendOutMessageUIThread(final int messageId) {
        handler_.post(new Runnable() {
            @Override
            public void run() {
                try {
                    sendOutMessage(messageId);
                } catch (RuntimeException e) {
                    Log.e("CheckinService_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }
        }
        );
    }

    public void getCheckinsBackground() {
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getCheckins();
                } catch (RuntimeException e) {
                    Log.e("CheckinService_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }
        }
        );
    }

}
