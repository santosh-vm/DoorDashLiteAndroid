package android.santosh.com.doordashlite;

/**
 * Created by Santosh on 8/4/17.
 */

public class DoorDashAPI {
    private DoorDashController doorDashController;
    private SharedPreferencesWrapper sharedPreferencesWrapper;

    public DoorDashAPI(DoorDashController doorDashController,
                       SharedPreferencesWrapper sharedPreferencesWrapper){
        this.doorDashController = doorDashController;
        this.sharedPreferencesWrapper = sharedPreferencesWrapper;
    }

    public DoorDashController getDoorDashController(){
        return doorDashController;
    }

    public SharedPreferencesWrapper getSharedPreferencesWrapper(){
        return sharedPreferencesWrapper;
    }

}
