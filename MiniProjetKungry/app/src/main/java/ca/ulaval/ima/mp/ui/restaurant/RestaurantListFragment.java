package ca.ulaval.ima.mp.ui.restaurant;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ca.ulaval.ima.mp.R;

public class RestaurantListFragment extends Fragment {

    private RestaurantFragmentListener mListener;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.show();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        return root;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RestaurantListFragment.RestaurantFragmentListener) {
            mListener = (RestaurantListFragment.RestaurantFragmentListener) context;
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