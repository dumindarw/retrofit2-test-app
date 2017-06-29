package com.loits.insurance.cm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by duminda on 12/18/16.
 */

public class Utilities {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double[] toPrimitiveDouble(Double [] array){
        if(array == null){
            return null;
        }else if(array.length == 0){
            return new double[0];
        }
        final double[] result = new double[array.length];
        for (int i=0;i<array.length;i++){
            result[i] = array[i].doubleValue();
        }

        return result;
    }

    public static int[] toPrimitiveInteger(Integer [] array){
        if(array == null){
            return null;
        }else if(array.length == 0){
            return new int[0];
        }
        final int[] result = new int[array.length];
        for (int i=0;i<array.length;i++){
            result[i] = array[i].intValue();
        }

        return result;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getMonth(String date){

        String[] parts = date.split("-");

        String[] parts2 = parts[2].split(" ");

        Calendar calendar = new GregorianCalendar(Integer.parseInt(parts2[0]),
                Integer.parseInt(parts[1]) - 1,
                Integer.parseInt(parts[0]));

        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);

        return monthName;

    }

    public static String getTime(String date){

        String[] parts = date.split("-");

        String[] parts2 = parts[2].split(" ");

        return parts2[1];

    }

    public static String getDay(String date){

        String[] parts = date.split("-");

        String[] parts2 = parts[2].split(" ");

        Calendar calendar = new GregorianCalendar(Integer.parseInt(parts2[0]),
                Integer.parseInt(parts[1]) - 1,
                Integer.parseInt(parts[0]));

        String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);

        return dayName;

    }

    public static String getDate(String date){

        String[] parts = date.split("-");

        return parts[0];

    }
}
