package ca.ulaval.ima.mp.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.domain.Restaurant;
import ca.ulaval.ima.mp.utils.CustomInfoWindowAdapter;
import ca.ulaval.ima.mp.utils.MapStateManager;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MapFragmentListener mListener;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private TextView name1;
    private TextView descript;
    private TextView kilometer;
    private RatingBar ratings;
    private TextView reviews1;
    private ImageView imageView;

    public HomeFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View includedLayout = getView().findViewById(R.id.card);
        includedLayout.setVisibility(View.INVISIBLE);

        View includedLayout1 = getView().findViewById(R.id.cardshow);
        includedLayout1.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //Gets the MapView from the XML layout and creates it

        getLocationPermission();

        return v;
    }


    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        map = mMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            showRestaurantPlaces();

            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }

    private void showRestaurantPlaces(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            double latitude = currentLocation.getLatitude();
                            double longitude = currentLocation.getLongitude();
                            showPlaces(latitude,longitude);

                        }else{
                            Log.e("DEBUG", "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("DEBUG", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }


    private void  showPlaces(double latitude, double longitude){

        name1 = getView().findViewById(R.id.restoname1);
        descript = getView().findViewById(R.id.descriptiontype1);
        kilometer = getView().findViewById(R.id.textdistance1);
        ratings = getView().findViewById(R.id.MyRating1);
        imageView = getView().findViewById(R.id.imageView1);
        reviews1 = getView().findViewById(R.id.reviewsCount1);
        final ArrayList<Restaurant> restaurants = new ArrayList<>();

        final RequestQueue queue = Volley.newRequestQueue(getContext());
        final String url = "https://kungry.ca/api/v1/restaurant/search/?latitude="+latitude+"&longitude="+longitude+"&radius=6";

        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
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

                                final String name = objecti.getString("name");
                                final String dist = objecti.getString("distance");
                                final String image1 = objecti.getString("image");
                                final int countReview = objecti.getInt("review_count");
                                final String typeRestaut = objecti.getString("type");
                                final float rate = (float)objecti.getDouble("review_average");

                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat,longitude))
                                        .title(name));
                                Restaurant restaurant = new Restaurant(name,typeRestaut,countReview,rate,image1,dist);
                                restaurants.add(restaurant);


                               /** MarkerOptions markerOpt = new MarkerOptions();
                                markerOpt.position(new LatLng(lat, longitude))
                                        .title(name)
                                        .snippet(dist).infoWindowAnchor(1,9);
                                String image = objecti.getString("image");
                                String typeRestaut = objecti.getString("type");
                                float rate = (float)objecti.getDouble("review_average");
                                Restaurant restaurant = new Restaurant();

                                restaurant.setReview_average(rate);
                                restaurant.setImage(image);
                                restaurant.setType(typeRestaut);
                                restaurant.setReview_count(reviews);

                                //Set Custom InfoWindow Adapter
                                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getActivity());
                                map.setInfoWindowAdapter(adapter);

                                Marker m = map.addMarker(markerOpt);
                                m.setTag(restaurant);
                                m.showInfoWindow();
                                View includedLayout = getView().findViewById(R.id.card);
                                includedLayout.setVisibility(View.INVISIBLE);

                                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                                {
                                    @Override
                                    public void onInfoWindowClick(Marker marker)
                                    {
                                        // Called when ANY InfoWindow is clicked
                                        Toast.makeText(getContext(), "Welcomeeeeeee", Toast.LENGTH_SHORT).show();

                                    }
                                });*/

                            }

                            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {

                                    for(int i = 0; i < restaurants.size(); i++){
                                        if(marker.getTitle().equals(restaurants.get(i).getRestaurantName())){
                                            View includedLayout1 = getView().findViewById(R.id.cardshow);
                                            includedLayout1.setVisibility(View.INVISIBLE);
                                            View includedLayout = getView().findViewById(R.id.card);
                                            includedLayout.setVisibility(View.VISIBLE);

                                            name1.setText(restaurants.get(i).getRestaurantName());
                                            descript.setText(restaurants.get(i).getType()+"/Food • Confort food");
                                            kilometer.setText(restaurants.get(i).getDistance()+" "+"km");
                                            Picasso.with(getContext()).load(restaurants.get(i).getImage()).resize(140,115).into(imageView);
                                            ratings.setRating(restaurants.get(i).getReview_average());
                                            reviews1.setText("("+restaurants.get(i).getReview_count()+")");
                                        }
                                    }
                                    return false;
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


    private void getDeviceLocation(){
        Log.e("DEBUG", "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.e("DEBUG", "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            double latitude = currentLocation.getLatitude();
                            double longitude = currentLocation.getLongitude();

                            goToLocationZoom(latitude, longitude);

                        }else{
                            Log.e("DEBUG", "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("DEBUG", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng){
        Log.e("DEBUG", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, HomeFragment.DEFAULT_ZOOM));
    }

    private void getLocationPermission(){
        Log.e("DEBUG", "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("DEBUG", "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.e("DEBUG", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.e("DEBUG", "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }



    private void goToLocationZoom(double v, double v1) {
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(v,v1))
                .zoom(13)
                .bearing(0)
                .tilt(45)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(v,v1))
                .title("Vous êtes ici"));

    }


    @Override
    public void onPause() {
        super.onPause();
        MapStateManager mgr = new MapStateManager(getContext());
        mgr.saveMapState(map);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMap();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.MapFragmentListener) {
            mListener = (HomeFragment.MapFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface MapFragmentListener {
        // TODO: Update argument type and name
        void showRestaurantPlace();

    }

}
