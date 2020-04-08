package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import ca.ulaval.ima.mp.ui.fragmentpackage.MapFragmentClone;
import ca.ulaval.ima.mp.ui.fragmentpackage.RestaurantDescription;
import ca.ulaval.ima.mp.ui.home.HomeFragment;

public class RestaurantDetails extends AppCompatActivity implements RestaurantDescription.RestoFragmentListener, MapFragmentClone.DescriptionMapFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        ImageView imageView = findViewById(R.id.img1);
        imageView.bringToFront();
        TextView textView = findViewById(R.id.geolocation);
        textView.bringToFront();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RelativeLayout relativeLayout = findViewById(R.id.reldays);
        relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        RelativeLayout relativeLayout1 = findViewById(R.id.txtgeolocation);
        relativeLayout1.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

    }

    @Override
    public void showDetails() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public LatLng getRestoLocation() {
        LatLng location = new LatLng(1,1);
        Intent intent = getIntent();
        if (intent != null){
            double [] locate = intent.getDoubleArrayExtra("location");
            if (locate != null){
                LatLng lng = new LatLng(locate[0],locate[1]);
                location = lng;
            }
        }

        return location;
    }

}
