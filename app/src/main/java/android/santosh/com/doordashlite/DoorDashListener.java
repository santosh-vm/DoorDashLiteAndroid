package android.santosh.com.doordashlite;

import java.util.List;

/**
 * Created by Santosh on 8/5/17.
 */

public interface DoorDashListener {
    void onHotelListFetchSuccess(List<Restaurant> restaurantList);
    void onHotelListFetchFailure();
}
