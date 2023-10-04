package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	final static private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static Date parseDate(String date) {
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(Date date) {
		return sdf.format(date);
	}
}
