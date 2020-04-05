package ca.ulaval.ima.mp;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.ulaval.ima.mp.ui.dashboard.DashboardFragment;
import ca.ulaval.ima.mp.ui.home.HomeFragment;
import ca.ulaval.ima.mp.utils.CustomListview;


public class MainActivity extends AppCompatActivity implements HomeFragment.MapFragmentListener, DashboardFragment.RestaurantFragmentListener, OnMapReadyCallback {

    GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(googleServicesAvailable()){
            Toast.makeText(this,"Perfecto !!",Toast.LENGTH_LONG).show();
        }
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
                            Toast.makeText(MainActivity.this, latitude+" "+longitude, Toast.LENGTH_SHORT).show();
                            getArroudRestaurant(latitude,longitude);

                        }else{
                            Log.e("DEBUG", "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("DEBUG", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void getArroudRestaurant(final double latitude, final double longitude){
        final ArrayList<String> restoName = new ArrayList<>();
        final ArrayList<String> img = new ArrayList<>();
        final ArrayList<String> type = new ArrayList<>();
        final ArrayList<String> reviewsCount = new ArrayList<>();
        final ArrayList<String> restoKm = new ArrayList<>();
        final ArrayList<String> reviewsStars = new ArrayList<>();
        myListView = findViewById(R.id.resto_list);


        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/restaurant/search/?latitude="+latitude+"&longitude="+longitude+"&radius=6";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            final JSONObject jsonObject = response.getJSONObject("content");
                            final JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objecti = jsonArray.getJSONObject(i);
                                String name = objecti.getString("name");
                                String image = objecti.getString("image");
                                int dist = objecti.getInt("distance");
                                int countReview = objecti.getInt("review_count");
                                String typeRestaut = objecti.getString("type");
                                restoName.add(name);
                                 img.add(image);
                                type.add(typeRestaut);
                                 reviewsCount.add(String.valueOf(countReview));
                                 restoKm.add(String.valueOf(dist));
                               reviewsStars.add("3");
                            }

                            CustomListview customListview = new CustomListview(MainActivity.this,restoName,
                                    reviewsCount,restoKm,reviewsStars,img,type);

                            myListView.setAdapter(customListview);
                            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        if(position==i){
                                            try {
                                                JSONObject objectii = jsonArray.getJSONObject(i);
                                                int theRestaurantId = objectii.getInt("id");
                                                getRestaurantDescription(theRestaurantId,latitude,longitude);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
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
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public void showRestaurantPlace(){
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
                            showPlaces(latitude,longitude);

                        }else{
                            Log.e("DEBUG", "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("DEBUG", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }


    private void  showPlaces(double latitude, double longitude){
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/restaurant/search/?latitude="+latitude+"&longitude="+longitude+"&radius=6";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            final JSONObject jsonObject = response.getJSONObject("content");
                            final JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objecti = jsonArray.getJSONObject(i);
                                JSONObject locationObject = objecti.getJSONObject("location");
                                String restoLatitude = locationObject.getString("latitude");
                                String restoLongitude = locationObject.getString("longitude");
                                double lat = Double.parseDouble(restoLatitude);
                                double longitude = Double.parseDouble(restoLongitude);




                            }


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

    private void getRestaurantDescription(int id,double lat, double longitude){
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://kungry.ca/api/v1/restaurant/"+id+"/?latitude="+lat+"&longitude="+longitude;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            final JSONObject jsonObject = response.getJSONObject("content");
                            final JSONArray jsonArray = jsonObject.getJSONArray("cuisine");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String name = obj.getString("name");
                            Toast.makeText(MainActivity.this,"Welcome to "+" "+name, Toast.LENGTH_LONG ).show();


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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
