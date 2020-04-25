package ca.ulaval.ima.mp;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ulaval.ima.mp.api.Client;
import ca.ulaval.ima.mp.api.Service;
import ca.ulaval.ima.mp.domain.Restaurant;
import ca.ulaval.ima.mp.domain.RestaurantResponse;
import ca.ulaval.ima.mp.domain.Result;
import ca.ulaval.ima.mp.ui.restaurant.RestaurantListFragment;
import ca.ulaval.ima.mp.ui.home.HomeFragment;
import ca.ulaval.ima.mp.ui.compte.CompteFragment;
import ca.ulaval.ima.mp.utils.PaginationAdapter;
import ca.ulaval.ima.mp.utils.PaginationScrollListener;
import ca.ulaval.ima.mp.utils.exceptions.DialogOwner;
import ca.ulaval.ima.mp.utils.exceptions.NotIdentificate;
import ca.ulaval.ima.mp.utils.exceptions.ProfileException;
import ca.ulaval.ima.mp.utils.exceptions.UserDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements HomeFragment.MapFragmentListener,
        CompteFragment.CompteFragmentListner, RestaurantListFragment.RestaurantFragmentListener {

    GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private ListView myListView;

    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    private Service restoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(googleServicesAvailable()){
            Toast.makeText(this,"Bienvenue !!",Toast.LENGTH_LONG).show();
        }

        View view = findViewById(R.id.card11);
        view.bringToFront();
        getSupportActionBar().hide();
        settings();

    }

    public boolean googleServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        }else if (api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this,isAvailable,0);
            dialog.show();
        }else{
            Toast.makeText(this,"Can't connect to play services", Toast.LENGTH_LONG ).show();
        }
        return false;
    }


    @Override
    public void show() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(!mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.e("DEBUG", "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            double latitude = currentLocation.getLatitude();
                            double longitude = currentLocation.getLongitude();
                            getAroundRestaurant(latitude,longitude);

                        }else{
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("DEBUG", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void getAroundRestaurant(final double latitude, final double longitude){

        rv =  findViewById(R.id.mainRecycler);
        progressBar = findViewById(R.id.main_progress);
        adapter = new PaginationAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage(latitude,longitude);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        restoService = Client.getClient().create(Service.class);

        loadFirstPage(latitude,longitude);

    }



    private void loadFirstPage(double l, double lon) {
        Log.e(TAG, "loadFirstPage: ");

        callTopRatedMoviesApi(l,lon).enqueue(new Callback<RestaurantResponse>() {

            @Override
            public void onResponse(Call<RestaurantResponse> call,  retrofit2.Response<RestaurantResponse> response) {
                List<Result> results = fetchResults(response);

                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    private List<Result> fetchResults(retrofit2.Response<RestaurantResponse> response) {
        RestaurantResponse res = response.body();


        return res.getContent().getResults();
    }

    private void loadNextPage(double a, double b) {
        Log.e(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi(a,b).enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, retrofit2.Response<RestaurantResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private Call<RestaurantResponse> callTopRatedMoviesApi(double la,double longi) {
        return restoService.getPopularMovies(currentPage,10, la,longi,10);
    }

    private void getRestaurantDescription(final int id, final double lat, final double longitude){
        final ArrayList<String> reviewsDate = new ArrayList<>();
        final ArrayList<String> reviewsRates = new ArrayList<>();
        final ArrayList<String> reviewsName = new ArrayList<>();
        final ArrayList<String> reviewsDesc = new ArrayList<>();
        final ArrayList<String> reviewsImages = new ArrayList<>();
        final ArrayList<String> hour = new ArrayList<>();

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/restaurant/"+id+"/?latitude="+lat+"&longitude="+longitude;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final JSONObject jsonObject = response.getJSONObject("content");
                            JSONObject objLocation = jsonObject.getJSONObject("location");
                            double lat = objLocation.getDouble("latitude");
                            double longitude = objLocation.getDouble("longitude");

                            JSONArray reviewsArray = jsonObject.getJSONArray("reviews");
                            String evluation = String.valueOf(reviewsArray.length());
                            for(int i =0 ; i < 3;i++){
                                if(reviewsArray.length() != 0 && !(reviewsArray.isNull(i))) {

                                    JSONObject object1 = reviewsArray.getJSONObject(i);
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
                            double[] latLng = {lat,longitude};

                            JSONArray jsonArray1 = jsonObject.getJSONArray("opening_hours");
                            if (jsonArray1.length() == 0){
                                for(int l = 0; l<8;l++){
                                    hour.add("Aucun");
                                }
                            }else {
                                for(int i =0 ; i < jsonArray1.length();i++){

                                    if(jsonArray1.length() != 0){
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        String open = jsonObject1.getString("opening_hour");
                                        String close = jsonObject1.getString("closing_hour");
                                        if(open.isEmpty() && close.isEmpty()){
                                            hour.add("Fermé");
                                        }else{
                                            String[] word = open.split(":");
                                            String[] word1 = close.split(":");
                                            String nnw = word[0]+":"+word[1];
                                            String nw1 = word1[0]+":"+word1[1];
                                            String resultFinal = nnw+" à "+nw1;

                                            hour.add(resultFinal);
                                        }
                                    }
                                }
                            }

                            String name = jsonObject.getString("name");
                            String distance = jsonObject.getString("distance");
                            int reviewCount = jsonObject.getInt("review_count");
                            float revAverage = (float)jsonObject.getDouble("review_average");
                            String imgrev = jsonObject.getString("image");
                            String webSite = jsonObject.getString("website");
                            String phone = jsonObject.getString("phone_number");
                            String typeRest = jsonObject.getString("type");

                            Restaurant restaurant = new Restaurant(name,webSite,phone,typeRest,reviewCount,revAverage,
                                    imgrev,distance);

                            Intent intent = new Intent(MainActivity.this, RestaurantDetails.class);
                            intent.putExtra("location", latLng);
                            intent.putExtra("resto",restaurant);
                            intent.putExtra("eval",evluation);
                            intent.putExtra("restoId",String.valueOf(id));
                            intent.putStringArrayListExtra("reviewsHeures",hour);
                            intent.putStringArrayListExtra("reviewsCards",reviewsDate);
                            intent.putStringArrayListExtra("reviewsStars",reviewsRates);
                            intent.putStringArrayListExtra("reviewsNames",reviewsName);
                            intent.putStringArrayListExtra("reviewsComs",reviewsDesc);
                            intent.putStringArrayListExtra("reviewsImgs",reviewsImages);
                            startActivityForResult(intent,12345);



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

        queue.add(getRequest);
    }


    @Override
    public LatLng showRestaurantPlace() {
        LatLng lng = new LatLng(1,1);
        Intent intent = getIntent();
        if (intent != null){
            double[] restoCoord = intent.getDoubleArrayExtra("resultMap");

            if (restoCoord != null){
                lng = new LatLng(restoCoord[0],restoCoord[1]);
            }
        }
        return lng;
    }

    @Override
    public void goToDetails(int id,double lat,double longitude) {
        getRestaurantDescription(id,lat,longitude);
    }


    @Override
    public void displaytoolbar() {
        View view = findViewById(R.id.card11);
        view.setVisibility(View.INVISIBLE);


    }

    @Override
    public void displayUserAccount(final String userMail, final String userpassWord) {

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/account/login/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        try {
                            JSONObject c = new JSONObject(response);
                            JSONObject jsonObject = c.getJSONObject("content");
                            String token = jsonObject.getString("access_token");
                            String tokentype = jsonObject.getString("token_type");
                            display(token,tokentype);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        ProfileException userDialog = new ProfileException();
                        userDialog.show(getSupportFragmentManager(), "dialog");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", "STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0");
                params.put("client_secret", "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLpr" +
                        "UmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWle" +
                        "QGYyApC2a469l36TdlXFqAG1tpK");
                params.put("email", userMail);
                params.put("password", userpassWord);

                return params;
            }
        };
        queue.add(postRequest);


    }


    private  void display(final String token, final String tokenType){
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/account/me/";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("content");
                            String lastName = jsonObject.getString("last_name");
                            String firstName = jsonObject.getString("first_name");
                            String owner = lastName+" "+firstName;
                            String mail = jsonObject.getString("email");
                            int reviewsTotal = jsonObject.getInt("total_review_count");
                            final RelativeLayout relativeLayout = findViewById(R.id.pageAccount);
                            relativeLayout.setVisibility(View.INVISIBLE);


                            final View view = findViewById(R.id.pageLogging);
                            view.setVisibility(View.VISIBLE);
                            View view1 = findViewById(R.id.cardd);
                            view1.setVisibility(View.VISIBLE);

                            TextView textView = findViewById(R.id.txtName);
                            TextView textMail = findViewById(R.id.txtEmail);
                            TextView textNumber = findViewById(R.id.txtReviews);
                            textView.setText(owner);
                            textMail.setText(mail);
                            textNumber.setText(String.valueOf(reviewsTotal));
                            Log.e("Error.Response", "ggggg");


                            Button button = findViewById(R.id.loging);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    view.setVisibility(View.INVISIBLE);

                                }
                            });

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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization",tokenType+" "+token);

                return params;
            }
        };

        queue.add(getRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void settings(){
        Intent intent = getIntent();
        if (intent != null){
            String s = intent.getStringExtra("result");
            if (s != null){
                BottomNavigationView mBottomNavigationView = findViewById(R.id.nav_view);
                mBottomNavigationView.setSelectedItemId(R.id.navigation_notifications);

            }
        }
    }

    @Override
    public void setTheReviews(final String userMail, final String userpassWord) {

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/account/login/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        try {
                            JSONObject c = new JSONObject(response);
                            JSONObject jsonObject = c.getJSONObject("content");
                            String token = jsonObject.getString("access_token");
                            String tokentype = jsonObject.getString("token_type");
                            display1(token,tokentype);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        NotIdentificate userDialog = new NotIdentificate();
                        userDialog.show(getSupportFragmentManager(), "dialog");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", "STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0");
                params.put("client_secret", "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLpr" +
                        "UmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWle" +
                        "QGYyApC2a469l36TdlXFqAG1tpK");
                params.put("email", userMail);
                params.put("password", userpassWord);

                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void userApplayingKungry(final String name, final String lastName, final String mail, final String pass) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/account/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        UserDialog userDialog = new UserDialog();
                        userDialog.show(getSupportFragmentManager(), "dialog");

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        DialogOwner d = new DialogOwner();
                        d.show(getSupportFragmentManager(), "dialog");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", "STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0");
                params.put("client_secret", "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLpr" +
                        "UmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWle" +
                        "QGYyApC2a469l36TdlXFqAG1tpK");
                params.put("first_name", name );
                params.put("last_name", lastName);
                params.put("email", mail);
                params.put("password", pass);

                return params;
            }
        };
        queue.add(postRequest);
    }


    private  void display1(final String token, final String tokenType){

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/account/me/";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final View view = findViewById(R.id.pageLogging);
                        view.setVisibility(View.INVISIBLE);
                        // display response
                        Intent intent = getIntent();
                        if (intent != null){
                            String s = intent.getStringExtra("result");
                            if (s != null){
                                intent.putExtra("result1","get");
                                intent.putExtra("tokenForReview",token);
                                intent.putExtra("tokenForReviewType",tokenType);
                                setResult(12345,intent);
                                finish();

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization",tokenType+" "+token);

                return params;
            }
        };

        queue.add(getRequest);
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!= 12345)
        {
            settings();

        }else {

            BottomNavigationView mBottomNavigationView = findViewById(R.id.nav_view);
            mBottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

        }

    }
}
