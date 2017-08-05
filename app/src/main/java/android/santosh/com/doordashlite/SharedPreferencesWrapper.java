package android.santosh.com.doordashlite;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Santosh on 8/4/17.
 */

public class SharedPreferencesWrapper {
    private Context context;
    private SharedPreferences preferences;

    public SharedPreferencesWrapper(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(context.getPackageName() + "app_prefs", Context.MODE_PRIVATE);
    }

}
