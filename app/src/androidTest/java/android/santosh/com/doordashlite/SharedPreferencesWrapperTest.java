package android.santosh.com.doordashlite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Created by Santosh on 8/5/17.
 */
public class SharedPreferencesWrapperTest {
    SharedPreferencesWrapper sharedPreferencesWrapper;

    @Before
    public void setup() {
       sharedPreferencesWrapper = new SharedPreferencesWrapper(getContext());
    }

    @Test
    public void testNullRestaurantFavoriteListValue(){
        String obtainedString = sharedPreferencesWrapper.getRestaurantFavoriteSetAsString();
        assertEquals(null,obtainedString);
    }

    @Test
    public void testNonNullRestaurantFavoriteListValue(){
        sharedPreferencesWrapper.saveRestaurantFavoriteSetAsString("[30,20]");
        String obtainedString = sharedPreferencesWrapper.getRestaurantFavoriteSetAsString();
        assertEquals("[30,20]",obtainedString);
    }

    @After
    public void destroy() {
        sharedPreferencesWrapper = null;
    }

}