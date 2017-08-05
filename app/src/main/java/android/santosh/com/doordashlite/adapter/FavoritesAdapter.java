package android.santosh.com.doordashlite.adapter;

import android.content.Context;
import android.santosh.com.doordashlite.DoorDashClickInterface;
import android.santosh.com.doordashlite.R;
import android.santosh.com.doordashlite.Restaurant;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Santosh on 8/5/17.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = FavoritesAdapter.class.getSimpleName();
    private DoorDashClickInterface doorDashClickInterface;
    private Context context;
    private List<Restaurant> restaurantList = new ArrayList<>();

    public FavoritesAdapter(Context context, DoorDashClickInterface doorDashClickInterface) {
        this.context = context;
        this.doorDashClickInterface = doorDashClickInterface;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RestaurantViewHolder) {
            RestaurantViewHolder restaurantViewHolder = (RestaurantViewHolder) holder;
            Restaurant restaurant = restaurantList.get(position);
            restaurantViewHolder.hotelNameTextView.setText(restaurant.getName());
            restaurantViewHolder.hotelDescriptionTextView.setText(restaurant.getDescription());
            restaurantViewHolder.hotelStatusTextView.setText(restaurant.getStatus());
            Picasso.with(context).load(restaurant.getCoverImageUrl()).into(restaurantViewHolder.headerImageView);

            if (restaurant.isFavorite()) {
                restaurantViewHolder.favoriteIconImageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_favorite_black));
            } else {
                restaurantViewHolder.favoriteIconImageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_favorite_border_black));
            }

            restaurantViewHolder.favoriteView.setOnClickListener(new FavoriteViewClickListener(position, doorDashClickInterface, restaurant));
            restaurantViewHolder.rootLayout.setOnClickListener(new RootViewClickListener(position, restaurant, doorDashClickInterface));
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView headerImageView, favoriteIconImageView;
        TextView hotelNameTextView, hotelDescriptionTextView, hotelStatusTextView;
        View rootLayout, favoriteView;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            headerImageView = itemView.findViewById(R.id.header_imageview);
            hotelNameTextView = itemView.findViewById(R.id.hotel_name);
            hotelDescriptionTextView = itemView.findViewById(R.id.hotel_description);
            favoriteView = itemView.findViewById(R.id.favorite_view);
            favoriteIconImageView = itemView.findViewById(R.id.favorite_imageview);
            hotelStatusTextView = itemView.findViewById(R.id.hotel_status);
            rootLayout = itemView.findViewById(R.id.root_layout);
        }
    }

    private class RootViewClickListener implements View.OnClickListener {
        private int position;
        private Restaurant restaurant;
        private DoorDashClickInterface doorDashClickInterface;

        RootViewClickListener(int position, Restaurant restaurant, DoorDashClickInterface doorDashClickInterface) {
            this.position = position;
            this.restaurant = restaurant;
            this.doorDashClickInterface = doorDashClickInterface;
        }

        @Override
        public void onClick(View view) {
            doorDashClickInterface.onRestaurantSelected(position, restaurant);
        }
    }

    private class FavoriteViewClickListener implements View.OnClickListener {
        private int position;
        private DoorDashClickInterface doorDashClickInterface;
        private Restaurant restaurant;

        FavoriteViewClickListener(int position, DoorDashClickInterface doorDashClickInterface, Restaurant restaurant) {
            this.position = position;
            this.doorDashClickInterface = doorDashClickInterface;
            this.restaurant = restaurant;
        }

        @Override
        public void onClick(View view) {
            if (restaurant.isFavorite()) {
                doorDashClickInterface.onFavoriteSelected(position, false, restaurant);
            } else {
                doorDashClickInterface.onFavoriteSelected(position, true, restaurant);
            }
        }
    }
}
