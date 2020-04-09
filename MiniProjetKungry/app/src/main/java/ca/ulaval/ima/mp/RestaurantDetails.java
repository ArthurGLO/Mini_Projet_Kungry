package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ca.ulaval.ima.mp.ui.fragmentpackage.MapFragmentClone;
import ca.ulaval.ima.mp.ui.fragmentpackage.RestaurantDescription;
import ca.ulaval.ima.mp.ui.home.HomeFragment;
import ca.ulaval.ima.mp.utils.RecyclerAdapter;

public class RestaurantDetails extends AppCompatActivity implements RestaurantDescription.RestoFragmentListener, MapFragmentClone.DescriptionMapFragmentListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

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
    public void showReviews() {
        Intent intent = getIntent();
        if (intent != null){
            ArrayList<String> reviews = intent.getStringArrayListExtra("reviewsCards");
            ArrayList<String> reviewsStar = intent.getStringArrayListExtra("reviewsStars");
            ArrayList<String> reviewsNames = intent.getStringArrayListExtra("reviewsNames");
            ArrayList<String> reviewsComment = intent.getStringArrayListExtra("reviewsComs");
            ArrayList<String> reviewsImg = intent.getStringArrayListExtra("reviewsImgs");

            Log.e("DEBUG",reviews.get(0));


            if (reviews != null && reviewsStar != null &&
                    reviewsNames != null && reviewsComment != null &&
                    reviewsImg != null){
                Log.e("DEBUG",reviews.get(0));
                Log.e("DEBUG",reviewsStar.get(0));
                Log.e("DEBUG",reviewsComment.get(0));


                recyclerView = findViewById(R.id.recycler_view);

                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new RecyclerAdapter(this,reviews,reviewsStar,reviewsNames,reviewsComment,reviewsImg);
                recyclerView.setAdapter(adapter);

            }


        }
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

    @Override
    public void setAdress(String adress) {
        TextView textView = findViewById(R.id.geolocation);
        textView.setText(adress);
    }

}
