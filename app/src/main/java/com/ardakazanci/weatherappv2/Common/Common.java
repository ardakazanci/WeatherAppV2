package com.ardakazanci.weatherappv2.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tüm projede, değişmeden kullanacağımız değerler ve metotlar burada tanımlanacak.
 */

public class Common {

    public static final String APP_ID = "34f70c8b3129f10ebf2fd23f1a0d3a82";
    public static Location current_location = null;


    public static String convertUnixToDate(int dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE MM yyyy");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToHour(int dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }
}
