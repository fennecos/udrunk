package net.udrunk.infra;

import java.sql.SQLException;

import net.udrunk.domain.Checkin;
import net.udrunk.domain.Place;
import net.udrunk.domain.User;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "udrunk.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access stored Objects
	private Dao<Checkin, Integer> checkinDao = null;
	private Dao<User, Integer> userDao = null;
	private Dao<Place, Integer> placeDao = null;
	
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			Log.i(DataBaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Checkin.class);
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Place.class);

			long millis = System.currentTimeMillis();
			Log.i(DataBaseHelper.class.getName(), "created new entries in onCreate: " + millis);
		} catch (SQLException e) {
			Log.e(DataBaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {

	}

	public Dao<User, Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(User.class);
		}
		return userDao;
	}
	public Dao<Checkin, Integer> getCheckinDao() throws SQLException {
		if (checkinDao == null) {
			checkinDao = getDao(Checkin.class);
		}
		return checkinDao;
	}
	public Dao<Place, Integer> getPlaceDao() throws SQLException {
		if (placeDao == null) {
			placeDao = getDao(Place.class);
		}
		return placeDao;
	}
	

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		checkinDao = null;
		userDao = null;
	}
	
}
