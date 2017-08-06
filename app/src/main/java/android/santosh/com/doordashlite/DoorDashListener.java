package android.santosh.com.doordashlite;

import java.util.List;

/**
 * Created by Santosh on 8/5/17.
 */

public interface DoorDashListener {
    void onRestaurantListFetchSuccess(List<Restaurant> restaurantList);
    void onRestaurantListFetchFailure();

    void onRestaurantDetailFetchSuccess(Restaurant restaurant);
    void onRestaurantDetailFetchFailure();
}
