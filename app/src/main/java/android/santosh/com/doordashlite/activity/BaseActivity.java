package android.santosh.com.doordashlite.activity;

import android.os.Bundle;
import android.santosh.com.doordashlite.DoorDashAPI;
import android.santosh.com.doordashlite.DoorDashApplication;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Santosh on 8/4/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected DoorDashAPI doorDashAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doorDashAPI = ((DoorDashApplication)getApplication()).getDoorDashAPI();
    }
}
