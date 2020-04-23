package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.ulaval.ima.mp.domain.Restaurant;
import ca.ulaval.ima.mp.ui.fragmentpackage.MapFragmentClone;
import ca.ulaval.ima.mp.ui.fragmentpackage.RestaurantDescription;
import ca.ulaval.ima.mp.ui.home.HomeFragment;
import ca.ulaval.ima.mp.ui.notifications.NotificationsFragment;
import ca.ulaval.ima.mp.utils.RecyclerAdapter;

public class RestaurantDetails extends AppCompatActivity implements RestaurantDescription.RestoFragmentListener,
        MapFragmentClone.DescriptionMapFragmentListener, NotificationsFragment.CompteFragmentListner {

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
    public void goToReviews() {
        RelativeLayout relativeLayout = findViewById(R.id.toreviews);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (intent != null){
                    String restauId = intent.getStringExtra("restoId");

                    if (restauId != null){
                        gotToReviewsPage(Integer.parseInt(restauId));
                    }
                }
            }
        });
    }

    private void gotToReviewsPage(int id){
        final ArrayList<String> reviewsDate = new ArrayList<>();
        final ArrayList<String> reviewsRates = new ArrayList<>();
        final ArrayList<String> reviewsName = new ArrayList<>();
        final ArrayList<String> reviewsDesc = new ArrayList<>();
        final ArrayList<String> reviewsImages = new ArrayList<>();

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/restaurant/"+id+"/reviews/";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            final JSONArray jsonArray = response.getJSONObject("content").getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(jsonArray.length() != 0 && !(jsonArray.isNull(i))) {

                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    String revDate = object1.getString("date");

                                    String[] words = revDate.split("-");

                                    String result = null;
                                    if (words[1].equals("01")) {
                                        result = "janvier";

                                    } else if (words[1].equals("02")) {
                                        result = "février";
                                    } else if (words[1].equals("03")) {
                                        result = "mars";
                                    } else if (words[1].equals("04")) {
                                        result = "avril";
                                    } else if (words[1].equals("05")) {
                                        result = "mai";
                                    } else if (words[1].equals("06")) {
                                        result = "juin";
                                    } else if (words[1].equals("07")) {
                                        result = "juillet";
                                    } else if (words[1].equals("08")) {
                                        result = "août";
                                    } else if (words[1].equals("09")) {
                                        result = "septembre";
                                    } else if (words[1].equals("10")) {
                                        result = "octobre";
                                    } else if (words[1].equals("11")) {
                                        result = "novembre";
                                    } else if (words[1].equals("12")) {
                                        result = "décembre";
                                    }

                                    String newDate = words[2] + " " +
                                            result + " " +
                                            words[0];
                                    reviewsDate.add(newDate);
                                    float rate = (float) object1.getDouble("stars");
                                    String comment = object1.getString("comment");
                                    String reviewimg = object1.getString("image");
                                    String revfirstName = object1.getJSONObject("creator").getString("first_name");
                                    String revlastName = object1.getJSONObject("creator").getString("last_name");

                                    reviewsImages.add(reviewimg);
                                    reviewsDesc.add(comment);
                                    reviewsName.add(revfirstName + " " + revlastName);
                                    reviewsRates.add(String.valueOf(rate));
                                }
                            }
                            Intent intent = new Intent(RestaurantDetails.this, ReviewsActivity.class);
                            intent.putStringArrayListExtra("reviewsCards",reviewsDate);
                            intent.putStringArrayListExtra("reviewsStars",reviewsRates);
                            intent.putStringArrayListExtra("reviewsNames",reviewsName);
                            intent.putStringArrayListExtra("reviewsComs",reviewsDesc);
                            intent.putStringArrayListExtra("reviewsImgs",reviewsImages);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
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
    public void connectToApplayReviews() {
        Intent intent = new Intent();

        setResult(12345,intent);
        finish();//finishing activity
    }


    @Override
    public void goToMap(LatLng latLng) {
        Intent intent = new Intent(RestaurantDetails.this, MainActivity.class);
        double[] myDouble= {latLng.latitude,latLng.longitude};
        intent.putExtra("resultMap",myDouble);
        startActivityForResult(intent,12345);


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

    @Override
    public void displaytoolbar() {

    }

    @Override
    public void displayUserAccount(String userMail, String userPassWord) {

    }


}
