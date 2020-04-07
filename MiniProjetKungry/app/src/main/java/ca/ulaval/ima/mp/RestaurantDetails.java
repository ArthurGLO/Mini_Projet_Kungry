package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ulaval.ima.mp.ui.fragmentpackage.RestaurantDescription;

public class RestaurantDetails extends AppCompatActivity implements RestaurantDescription.RestoFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
    }

    @Override
    public void showDetails() {

    }
}
