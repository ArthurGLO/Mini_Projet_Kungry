package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ca.ulaval.ima.mp.ui.fragmentpackage.RestaurantDescription;

public class RestaurantDetails extends AppCompatActivity implements RestaurantDescription.RestoFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        ImageView imageView = findViewById(R.id.img1);
        imageView.bringToFront();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void showDetails() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
