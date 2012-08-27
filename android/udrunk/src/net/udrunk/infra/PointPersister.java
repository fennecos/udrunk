package net.udrunk.infra;

import net.udrunk.domain.Point;
import android.database.SQLException;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import com.j256.ormlite.support.DatabaseResults;

public class PointPersister extends StringType {

	private final String DIVIDER = ";";

	private static final PointPersister self = new PointPersister();

	/**
	 * Static method required by the ormlite framework. By convention those
	 * methods are named "getInstance" - unfortunately in this case we're bound
	 * to the convention of the ormlite framework, hence the name
	 * "getSingleton".
	 * 
	 * @return an instance of {@link XmlSerializedType}.
	 */
	public static PointPersister getSingleton() {
		return self;
	}

	private PointPersister() {
		this(SqlType.LONG_STRING, new Class<?>[] { Object.class });
	}

	protected PointPersister(SqlType sqlType, Class<?>[] classes) {
		super(sqlType, classes);
	}

	@Override
	public Object resultToJava(FieldType fieldType, DatabaseResults results,
			int columnPos) throws SQLException {
		try {
			// We expect a string from the database
			String xml = results.getString(columnPos);
			if (xml != null) {
				String[] coordinates = xml.split(DIVIDER);
				// First one is latitude, second one is longitude
				return new Point(coordinates[0], coordinates[1]);
			}
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Object javaToSqlArg(FieldType fieldType, Object javaObject)
			throws SQLException {
		if (javaObject != null) {
			Point point = (Point) javaObject;
			// Convert latitude/longitude to string
			return String.format("%s%s%s", point.x, DIVIDER, point.y);
		}
		return null;
	}

	@Override
	public int getDefaultWidth() {
		/*
		 * Provide more space than the StringType. We don't use LongStringType
		 * since this way we wouldn't be able to call the required super
		 * constructor.
		 */
		return 0;
	}
}
