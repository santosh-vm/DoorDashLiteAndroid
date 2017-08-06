package android.santosh.com.doordashlite;

import android.os.Handler;

import org.junit.After;
import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Created by Santosh on 8/5/17.
 */
public class DoorDashControllerTest {
    private DoorDashController doorDashController;
    private SharedPreferencesWrapper sharedPreferencesWrapper;

    @Before
    public void setup() {
        sharedPreferencesWrapper = new SharedPreferencesWrapper(getContext());
        doorDashController = new DoorDashController(new Handler(), sharedPreferencesWrapper);
    }

    @After
    public void destroy() {
        doorDashController = null;
        sharedPreferencesWrapper = null;
    }

}