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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.ulaval.ima.mp.domain.Restaurant;
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
        TextView txtLundi = findViewById(R.id.t11);
        TextView txtMardi = findViewById(R.id.t22);
        TextView txtMercredi = findViewById(R.id.t33);
        TextView txtJeudi = findViewById(R.id.t44);
        TextView txtVendredi = findViewById(R.id.t55);
        TextView txtSamedi = findViewById(R.id.t66);
        TextView txtDimanche = findViewById(R.id.t77);

        ImageView imageView = findViewById(R.id.img);
        TextView txtName = findViewById(R.id.txt_title);
        TextView txtype = findViewById(R.id.txt_type);
        TextView txtdist = findViewById(R.id.textdistance);
        RatingBar ratingBar = findViewById(R.id.MyRatingDetails);
        TextView txtCount = findViewById(R.id.textcount);
        TextView phone = findViewById(R.id.textcall);
        TextView link = findViewById(R.id.textlink);
        TextView eval = findViewById(R.id.txteval);

        Intent intent = getIntent();
        if (intent != null){
            ArrayList<String> reviews = intent.getStringArrayListExtra("reviewsCards");
            ArrayList<String> reviewsStar = intent.getStringArrayListExtra("reviewsStars");
            ArrayList<String> reviewsNames = intent.getStringArrayListExtra("reviewsNames");
            ArrayList<String> reviewsComment = intent.getStringArrayListExtra("reviewsComs");
            ArrayList<String> reviewsImg = intent.getStringArrayListExtra("reviewsImgs");
            Restaurant detailsResto = intent.getParcelableExtra("resto");
            ArrayList<String> reviewHour = intent.getStringArrayListExtra("reviewsHeures");
            String evaluations = intent.getStringExtra("eval");

            if (reviews != null && reviewsStar != null &&
                    reviewsNames != null && reviewsComment != null &&
                    reviewsImg != null && detailsResto != null && reviewHour != null && evaluations != null){

                txtLundi.setText(reviewHour.get(1));
                txtMardi.setText(reviewHour.get(2));
                txtMercredi.setText(reviewHour.get(3));
                txtJeudi.setText(reviewHour.get(4));
                txtVendredi.setText(reviewHour.get(5));
                txtSamedi.setText(reviewHour.get(6));
                txtDimanche.setText(reviewHour.get(0));

                Picasso.with(this).load(detailsResto.getImage()).resize(230,150).into(imageView);
                txtName.setText(detailsResto.getRestaurantName());
                txtype.setText(detailsResto.getType()+"/bar - Repas Gastronomique");
                txtdist.setText(detailsResto.getDistance()+" km");
                ratingBar.setRating(detailsResto.getReview_average());
                txtCount.setText("("+detailsResto.getReview_count()+")");

                String[] s = detailsResto.getPhone().split("");
                String newphone = "("+s[1]+s[2]+s[3]+")"+" "+s[4]+s[5]+s[6]+"-"+s[7]+s[8]+s[9]+s[10];

                phone.setText(newphone);
                //link.setText(detailsResto.getWebSite());
                eval.setText("("+evaluations+")");



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
