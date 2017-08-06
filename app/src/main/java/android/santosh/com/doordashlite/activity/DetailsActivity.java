package android.santosh.com.doordashlite.activity;

import android.os.Bundle;
import android.santosh.com.doordashlite.DoorDashListener;
import android.santosh.com.doordashlite.R;
import android.santosh.com.doordashlite.Restaurant;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Santosh on 8/5/17.
 */

public class DetailsActivity extends BaseActivity implements DoorDashListener {
    private static String TAG = DetailsActivity.class.getSimpleName();
    private int restaurantId;
    private View errorView, detailsView, progressView;
    private ImageView headerImageView;
    private TextView restaurantNameTextView;
    private TextView restaurantDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        restaurantId = getIntent().getIntExtra(RESTAURANT_DETAIL_RESTAURANT_ID_INTENT_KEY, 30);
        Log.d(TAG, "onCreate restaurantId: " + restaurantId);
        bindUIElements();
        doorDashAPI.getDoorDashController().addDoorDashListener(this);
        doorDashAPI.getDoorDashController().getRestaurantDetails(restaurantId);
    }

    private void bindUIElements() {
        errorView = findViewById(R.id.error_view);
        errorView.setVisibility(View.GONE);
        detailsView = findViewById(R.id.details_view);
        detailsView.setVisibility(View.GONE);
        progressView = findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);

        //I'm currently adding only restaurant name, coverimage and details in the detail activity,
        headerImageView = (ImageView) findViewById(R.id.header_image_view);
        restaurantNameTextView = (TextView) findViewById(R.id.restaurant_name_text_view);
        restaurantDetailsTextView = (TextView) findViewById(R.id.restaurant_details_text_view);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doorDashAPI.getDoorDashController().removeDoorDashListener(this);
    }

    @Override
    public void onRestaurantListFetchSuccess(List<Restaurant> restaurantList) {

    }

    @Override
    public void onRestaurantListFetchFailure() {

    }

    @Override
    public void onRestaurantDetailFetchSuccess(Restaurant restaurant) {
        Log.d(TAG, "onRestaurantDetailFetchSuccess, restaurant: " + restaurant.getName());
        progressView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        detailsView.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(restaurant.getCoverImageUrl())) {
            Picasso.with(this).load(restaurant.getCoverImageUrl()).into(headerImageView);
        }
        restaurantNameTextView.setText(restaurant.getName());
        restaurantDetailsTextView.setText(restaurant.getDescription());
    }

    @Override
    public void onRestaurantDetailFetchFailure() {
        Log.d(TAG, "onRestaurantDetailFetchFailure()");
        progressView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        detailsView.setVisibility(View.GONE);
    }
}
