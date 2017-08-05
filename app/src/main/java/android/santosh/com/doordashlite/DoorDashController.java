package android.santosh.com.doordashlite;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
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
    private static String HOTEL_LIST_URL = "https://api.doordash.com/v2/restaurant/?lat=37.422740&lng=-122.139956";
    private static String HOTEL_DETAIL_URL = "https://api.doordash.com/v2/restaurant/%s/";
    private OkHttpClient client;
    private Gson gson;
    private List<Restaurant> restaurantList = new ArrayList<>();

    DoorDashController(Handler uiHandler) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.uiHandler = uiHandler;
        this.client = new OkHttpClient();
        this.gson = new GsonBuilder().create();
    }

    public void getHotelList() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Caching previously obtained result in memory, suggested improvement is to store in database and read from there
                        //and refresh database by making network once every hour.
                        if (restaurantList.size() <= 0) {
                            Request request = new Request.Builder()
                                    .url(HOTEL_LIST_URL)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String stringyfiedJson = new String(response.body().bytes(), "UTF-8");
                            switch (response.code()) {
                                case 200:
                                    restaurantList = gson.fromJson(stringyfiedJson, restaurantList.getClass());
                                    if(restaurantList!=null && restaurantList.size() > 0){
                                        //TODO: Report Success
                                    } else {
                                        //TODO: Report Failure
                                    }
                                    break;
                                default:
                                    //TODO: Report Failure
                                    break;
                            }
                        } else {
                            Log.d(TAG, "restaurantList.size(): " + restaurantList.size() + ", restaurantList: " + restaurantList);
                            //Report Success
                        }
                    } catch (IOException iex) {

                    }
                }
            });
        }
    }

}
