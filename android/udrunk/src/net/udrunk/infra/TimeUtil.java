package net.udrunk.infra;

import java.util.Date;

import net.udrunk.R;
import android.content.Context;
import android.content.res.Resources;

public class TimeUtil {
	
	public static long SECOND = 1000;
	public static long MINUTE = 60 * SECOND;
	public static long HOUR = 60 * MINUTE;
	public static long DAY = 24 * HOUR;
	public static long MONTH = 30 * DAY;
	
	public static String formatAgoDate(Date date, Context context)
	{
		String result = null;
		Date now = new Date();
		
		long diff = now.getTime() - date.getTime();
		int value;
		
		Resources res = context.getResources();
		
		if(diff < MINUTE)
		{
			value = (int)(diff / SECOND);
			result = res.getQuantityString(R.plurals.seconds_ago, value, value);
		}
		else if(diff < HOUR)
		{
			value = (int)(diff / MINUTE);
			result = res.getQuantityString(R.plurals.minutes_ago, value, value);
		}
		else if(diff < DAY)
		{
			value = (int)(diff / HOUR);
			result = res.getQuantityString(R.plurals.hours_ago, value, value);
		}
		else if(diff < MONTH)
		{
			value = (int)(diff / DAY);
			result = res.getQuantityString(R.plurals.days_ago, value, value);
		}
		else
		{
			value = (int)(diff / MONTH);
			result = res.getQuantityString(R.plurals.months_ago, value, value);
		}
		
		return result;
	}
}
