package android.santosh.com.doordashlite;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Santosh on 8/4/17.
 */

public class SharedPreferencesWrapper {
    private Context context;
    private SharedPreferences preferences;

    private static String HOTEL_FAVORITE_LIST = "hotel_favorite_list";

    public SharedPreferencesWrapper(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(context.getPackageName() + "app_prefs", Context.MODE_PRIVATE);
    }

    public String getHotelFavoriteSetAsString() {
        return getStringValue(HOTEL_FAVORITE_LIST);
    }

    public void saveHotelFavoriteSetAsString(String hotelFavoriteListString) {
        saveStringValue(HOTEL_FAVORITE_LIST, hotelFavoriteListString);
    }

    private void saveStringValue(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private String getStringValue(String key) {
        return preferences.getString(key, null);
    }

}
