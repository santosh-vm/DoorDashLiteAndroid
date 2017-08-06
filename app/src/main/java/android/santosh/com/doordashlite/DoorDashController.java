package android.santosh.com.doordashlite;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Santosh on 8/4/17.
 */

public class DoorDashController {
    private static String TAG = DoorDashController.class.getSimpleName();
    private Handler uiHandler;
    private ExecutorService executorService;
    private static String HOTEL_LIST_URL = "https://api.doordash.com/v2/restaurant/?lat=%1s&lng=%2s";
    private static String HOTEL_DETAIL_URL = "https://api.doordash.com/v2/restaurant/%s/";
    private OkHttpClient client;
    private Gson gson;
    private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
    private List<DoorDashListener> doorDashListenerList = Collections.synchronizedList(new ArrayList<DoorDashListener>());
    private HashSet<Integer> favoriteSet = new HashSet<>();
    private SharedPreferencesWrapper sharedPreferencesWrapper;


    DoorDashController(Handler uiHandler, SharedPreferencesWrapper sharedPreferencesWrapper) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.uiHandler = uiHandler;
        this.client = new OkHttpClient();
        this.gson = new GsonBuilder().create();
        this.sharedPreferencesWrapper = sharedPreferencesWrapper;
    }

    public void getRestaurantList(final double latitude, final double longitude) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Caching previously obtained result in memory, suggested improvement is to store in database and read from there
                        //and refresh database by making network once every hour.
                        if (restaurantList.size() <= 0) {
                            Request request = new Request.Builder()
                                    .url(String.format(HOTEL_LIST_URL, latitude, longitude))
                                    .build();
                            Response response = client.newCall(request).execute();
                            String stringyfiedJson = new String(response.body().bytes(), "UTF-8");
                            switch (response.code()) {
                                case 200:
                                    restaurantList = Arrays.asList(gson.fromJson(stringyfiedJson, Restaurant[].class));
                                    if (restaurantList.size() > 0) {
                                        Log.d(TAG, "From Network restaurantList.size(): " + restaurantList.size());
                                        refreshRestaurantList();
                                    } else {
                                        notifyRestaurantListFetchFailure();
                                    }
                                    break;
                                default:
                                    notifyRestaurantListFetchFailure();
                                    break;
                            }
                        } else {
                            Log.d(TAG, "From Memory restaurantList.size(): " + restaurantList.size());
                            refreshRestaurantList();
                        }
                    } catch (IOException iex) {
                        Log.e(TAG, "IOException Caught:\n" + iex);
                        notifyRestaurantListFetchFailure();
                    }
                }
            });
        } else {
            notifyRestaurantListFetchFailure();
        }
    }

    public void getRestaurantDetails(final int restaurantId) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Request request = new Request.Builder()
                                .url(String.format(HOTEL_DETAIL_URL, restaurantId))
                                .build();
                        Response response = client.newCall(request).execute();
                        String stringyfiedJson = new String(response.body().bytes(), "UTF-8");
                        switch (response.code()) {
                            case 200:
                                Restaurant restaurant = gson.fromJson(stringyfiedJson, Restaurant.class);
                                if (restaurant != null) {
                                    notifyRestaurantDetailFetchSuccess(restaurant);
                                } else {
                                    notifyRestaurantDetailFetchFailure();
                                }
                                break;
                            default:
                                notifyRestaurantDetailFetchFailure();
                                break;
                        }
                    } catch (IOException iex) {
                        notifyRestaurantDetailFetchFailure();
                    }
                }
            });
        } else {
            notifyRestaurantDetailFetchFailure();
        }
    }

    public void updateRestaurantFavoriteStatus(int position, boolean newIsFavorite, Restaurant restaurant) {
        if (restaurantList != null && restaurantList.size() > 0 && restaurantList.contains(restaurant)) {
            Log.d(TAG, "Before marking restaurant with id: " + restaurant.getId() + ", favoriteSet: " + favoriteSet);
            int restaurantId = restaurant.getId();
            restaurant.setFavorite(newIsFavorite);
            restaurantList.get(position).setFavorite(newIsFavorite);
            if (newIsFavorite) {
                favoriteSet.add(restaurantId);
            } else {
                if (favoriteSet.contains(restaurantId)) {
                    favoriteSet.remove(restaurantId);
                }
            }
            sharedPreferencesWrapper.saveHotelFavoriteSetAsString(gson.toJson(favoriteSet));
            notifyRestaurantListFetchSuccess(restaurantList);
            Log.d(TAG, "After marking restaurant with id: " + restaurant.getId() + ", favoriteSet: " + favoriteSet);
        }
    }

    private synchronized void refreshRestaurantList() {
        loadFavoriteSetFromSharedPref();
        if (favoriteSet.size() > 0) {
            for (Restaurant restaurant : restaurantList) {
                if (favoriteSet.contains(restaurant.getId())) {
                    restaurant.setFavorite(true);
                }
            }
        }
        Collections.sort(restaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                int b1 = restaurant1.isFavorite() ? 1 : 0;
                int b2 = restaurant2.isFavorite() ? 1 : 0;
                return b2 - b1;
            }
        });
        notifyRestaurantListFetchSuccess(restaurantList);
    }

    private void loadFavoriteSetFromSharedPref() {
        String stringifiedSet = sharedPreferencesWrapper.getHotelFavoriteSetAsString();
        if (!TextUtils.isEmpty(stringifiedSet)) {
            Type hashSetType = new TypeToken<HashSet<Integer>>() {
            }.getType();
            HashSet<Integer> obtainedFavoriteSet = new Gson().fromJson(stringifiedSet, hashSetType);
            favoriteSet = new HashSet<>(obtainedFavoriteSet);
        } else {
            favoriteSet = new HashSet<>();
        }
    }

    public void addDoorDashListener(DoorDashListener doorDashListener) {
        if (doorDashListenerList != null && !doorDashListenerList.contains(doorDashListener)) {
            doorDashListenerList.add(doorDashListener);
        }
    }

    public void removeDoorDashListener(DoorDashListener doorDashListener) {
        if (doorDashListenerList != null && doorDashListenerList.contains(doorDashListener)) {
            doorDashListenerList.remove(doorDashListener);
        }
    }

    private void notifyRestaurantListFetchSuccess(final List<Restaurant> restaurantList) {
        if (doorDashListenerList != null && doorDashListenerList.size() > 0) {
            for (final DoorDashListener doorDashListener : doorDashListenerList) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        doorDashListener.onRestaurantListFetchSuccess(restaurantList);
                    }
                });
            }
        }
    }

    private void notifyRestaurantListFetchFailure() {
        if (doorDashListenerList != null && doorDashListenerList.size() > 0) {
            for (final DoorDashListener doorDashListener : doorDashListenerList) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        doorDashListener.onRestaurantListFetchFailure();
                    }
                });
            }
        }
    }

    private void notifyRestaurantDetailFetchSuccess(final Restaurant restaurant) {
        if (doorDashListenerList != null && doorDashListenerList.size() > 0) {
            for (final DoorDashListener doorDashListener : doorDashListenerList) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        doorDashListener.onRestaurantDetailFetchSuccess(restaurant);
                    }
                });
            }
        }
    }

    private void notifyRestaurantDetailFetchFailure() {
        if (doorDashListenerList != null && doorDashListenerList.size() > 0) {
            for (final DoorDashListener doorDashListener : doorDashListenerList) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        doorDashListener.onRestaurantDetailFetchFailure();
                    }
                });
            }
        }
    }

}
