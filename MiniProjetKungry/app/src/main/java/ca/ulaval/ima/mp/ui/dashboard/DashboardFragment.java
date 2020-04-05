package ca.ulaval.ima.mp.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.domain.Restaurant;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RestaurantFragmentListener mListener;
    private ArrayList<Restaurant> restaurants;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.show();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //mListener.show();

        return root;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DashboardFragment.RestaurantFragmentListener) {
            mListener = (DashboardFragment.RestaurantFragmentListener) context;
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


    public interface RestaurantFragmentListener {
        // TODO: Update argument type and name
        void show();
    }
}