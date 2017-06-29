package com.loits.insurance.cm.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeHelper {
	public static final String DATE_TIME_FORMAT_24 = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_FORMAT_MIN = "yyyy/MM/dd hh:mm aaa";
	public static final String TIME_FORMAT_MIN = "hh:mm aaa";
	public static final String DATE_FORMAT = "yyyy/MM/dd";


	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_24);
		return sdf.format(cal.getTime());
	}	

	public static String now(String dateFormat){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
}
