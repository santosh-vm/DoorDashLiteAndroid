package android.santosh.com.doordashlite;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Santosh on 8/4/17.
 */

public class Restaurant {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("cover_img_url")
    String coverImageUrl;
    @SerializedName("status")
    String status;
    @SerializedName("delivery_fee")
    int deliveryFee;

    boolean isFavorite;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
