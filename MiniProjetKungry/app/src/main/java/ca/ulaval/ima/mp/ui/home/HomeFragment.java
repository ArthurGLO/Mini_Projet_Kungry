package ca.ulaval.ima.mp.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.utils.MapStateManager;


public class HomeFragment extends Fragment implements OnMapReadyCallback{

    MapView mapView;
    GoogleMap map;
    //private FusedLocationProviderClient mFusedLocationClient;
    private MapFragmentListener mListener;

    public HomeFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //Gets the MapView from the XML layout and creates it

        initMap();

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

    }








    private void goToLocationZoom(double v, double v1) {
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(v,v1))
                .zoom(10)
                .bearing(0)
                .tilt(45)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(v,v1))
                .title("Spider Man"));

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
        void modelDescription();
    }

}
