package ca.ulaval.ima.mp.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;

public class NotificationsFragment extends Fragment {

    private CompteFragmentListner mListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.displaytoolbar();


    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Button button = root.findViewById(R.id.loging1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userMail = root.findViewById(R.id.edit_userMail);
                EditText userPassWord = root.findViewById(R.id.edit_userpassword);
                mListener.displayUserAccount(userMail.getText().toString(),userPassWord.getText().toString());
            }
        });


        return root;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NotificationsFragment.CompteFragmentListner) {
            mListener = (NotificationsFragment.CompteFragmentListner) context;
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


    public interface CompteFragmentListner {
        // TODO: Update argument type and name
        void displaytoolbar();
        void displayUserAccount(String userMail,String userPassWord);

    }
}