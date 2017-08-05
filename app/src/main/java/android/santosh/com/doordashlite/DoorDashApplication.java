package android.santosh.com.doordashlite;

import android.app.Application;
import android.os.Handler;

/**
 * Created by Santosh on 8/4/17.
 */

public class DoorDashApplication extends Application {
    private DoorDashAPI doorDashAPI;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferencesWrapper sharedPreferencesWrapper = new SharedPreferencesWrapper(this);
        DoorDashController doorDashController = new DoorDashController(new Handler(), sharedPreferencesWrapper);
        doorDashAPI = new DoorDashAPI(doorDashController, sharedPreferencesWrapper);

    }

    public DoorDashAPI getDoorDashAPI() {
        return doorDashAPI;
    }
}
