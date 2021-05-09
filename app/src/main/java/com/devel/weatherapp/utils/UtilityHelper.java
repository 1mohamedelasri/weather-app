package com.devel.weatherapp.utils;

import android.content.Context;
import android.location.Location;
import android.text.format.Time;

import com.devel.weatherapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilityHelper {

    public static String formatTemperature(Context context, double temperature, boolean withCelsiusSymbol) {
        // Data stored in Celsius by default.  If user prefers to see in Fahrenheit, convert
        // the values here.
        String suffix = "\u00B0";
        //temperature = (temperature * 1.8) + 32;

        if(withCelsiusSymbol) return String.format(context.getString(R.string.format_temperature), temperature);

        return String.format(context.getString(R.string.format_temperature_nosymbol), temperature);
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static String formatDate(long dateInMilliseconds) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy ");
        String dateString = dateFormat.format(new Date(dateInMilliseconds * 1000));
        return dateString;
    }

    public static String formatHourly(long dateInMilliseconds) {
        DateFormat dateFormat = new SimpleDateFormat("kk:mm");
        String dateString = dateFormat.format(new Date(dateInMilliseconds * 1000));
        return dateString;
    }

    public static String format(long dateInMilliseconds) {
        DateFormat dateFormat = new SimpleDateFormat("EEEE ");
        String dateString = dateFormat.format(new Date(dateInMilliseconds * 1000));
        return dateString;
    }

    public static Date timestampToDate(Long timestamp){
        java.util.Date date =new java.util.Date((long)timestamp*1000);
        return date;
    }

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis ) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(UtilityHelper.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }
    public static String getFormattedDayHour(Context context, long dateInMillis ) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(UtilityHelper.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }

    public static String getFormattedWind(Context context, Double windSpeed) {
        int windFormat;
        windFormat = R.string.format_wind_kmh;

        return String.format(context.getString(windFormat), windSpeed);
    }

    public static int getBackgroundResourceForWeatherCondition(Long weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes


        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.w_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.w_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.w_rain;
        } else if (weatherId == 511) {
            return R.drawable.w_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.w_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.w_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.w_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.w_storm;
        } else if (weatherId == 800) {
            return R.drawable.w_sunny;
        } else if (weatherId == 801) {
            return R.drawable.w_cool;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.w_bad;
        }
        return -1;
    }

    public static int getCardViewColorResourceForWeatherCondition(Long weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes


        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.corners_bg_cardview;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.corners_bg_cardview;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.corners_bg_cardview;
        } else if (weatherId == 511) {
            return R.drawable.w_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.corners_bg_cardview;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.corners_bg_cardview;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.corners_bg_cardview_dark;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.corners_bg_cardview;
        } else if (weatherId == 800) {
            return R.drawable.corners_bg_cardview_dark;
        } else if (weatherId == 801) {
            return R.drawable.corners_bg_cardview_dark;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.corners_bg_cardview_dark;
        }
        return -1;
    }


    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getArtResourceForWeatherCondition(Long weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

    public static String[] geoLocToString(Location loc){
        return new String[]{String.valueOf(loc.getLatitude()),String.valueOf(loc.getLongitude())};
    }

    public static String getCountryName(String name){
        Locale loc = new Locale("en",name);
        return loc.getDisplayCountry();
    }
}
