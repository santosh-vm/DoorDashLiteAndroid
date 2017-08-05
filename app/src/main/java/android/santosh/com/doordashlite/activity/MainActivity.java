package android.santosh.com.doordashlite.activity;

import android.content.Intent;
import android.santosh.com.doordashlite.DoorDashClickInterface;
import android.santosh.com.doordashlite.DoorDashListener;
import android.santosh.com.doordashlite.R;
import android.santosh.com.doordashlite.Restaurant;
import android.os.Bundle;
import android.santosh.com.doordashlite.adapter.FavoritesAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends BaseActivity implements DoorDashListener, DoorDashClickInterface {
    private static String TAG = MainActivity.class.getSimpleName();
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView restaurantsRecyclerView;
    private TextView errorMessageTextView;
    private ProgressBar progressBar;
    private static double DOORDASH_HQ_LATITUDE = 37.422740;
    private static double DOORDASH_HQ_LONGITUDE = -122.139956;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_main);
        bindUIElements();
        doorDashAPI.getDoorDashController().addDoorDashListener(this);
        //Currently Hardcoding latitude and longitude to  Doordash HQ
        doorDashAPI.getDoorDashController().getHotelList(DOORDASH_HQ_LATITUDE, DOORDASH_HQ_LONGITUDE);
    }

    private void bindUIElements() {
        //Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //RecyclerView
        favoritesAdapter = new FavoritesAdapter(this, this);
        restaurantsRecyclerView = (RecyclerView) findViewById(R.id.restaurant_recycler_view);
        LinearLayoutManager restaurantsLinearLayoutManger = new LinearLayoutManager(this);
        restaurantsLinearLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        restaurantsRecyclerView.setLayoutManager(restaurantsLinearLayoutManger);
        restaurantsRecyclerView.setAdapter(favoritesAdapter);
        restaurantsRecyclerView.setVisibility(View.GONE);

        //TextView
        errorMessageTextView = (TextView) findViewById(R.id.error_message_text_view);
        errorMessageTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doorDashAPI.getDoorDashController().removeDoorDashListener(this);
    }

    @Override
    public void onHotelListFetchSuccess(List<Restaurant> restaurantList) {
        Log.d(TAG, "onHotelListFetchSuccess restaurantList.size(): " + restaurantList.size());
        progressBar.setVisibility(View.GONE);
        restaurantsRecyclerView.setVisibility(View.VISIBLE);
        favoritesAdapter.setRestaurantList(restaurantList);

        errorMessageTextView.setVisibility(View.GONE);
    }

    @Override
    public void onHotelListFetchFailure() {
        Log.d(TAG, "onHotelListFetchFailure.");
        progressBar.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        restaurantsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onFavoriteSelected(int position, boolean newIsFavorite, Restaurant restaurant) {
        Log.d(TAG, "onFavoriteSelected, position: " + position + ", restaurant: " + restaurant + ", newIsFavorite: " + newIsFavorite);
        doorDashAPI.getDoorDashController().updateRestaurantFavoriteStatus(position, newIsFavorite, restaurant);
    }

    @Override
    public void onRestaurantSelected(int position, Restaurant restaurant) {
        Log.d(TAG,"onRestaurantSelected position: "+position+", name: "+restaurant.getName()+", restaurant id: "+restaurant.getId());
        startActivity(new Intent(this, DetailsActivity.class));
    }
}
