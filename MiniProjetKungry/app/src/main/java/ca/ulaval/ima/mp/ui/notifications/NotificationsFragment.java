package ca.ulaval.ima.mp.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private  EditText textFirstName;
    private  EditText textLastName;
    private  EditText textMails;
    private  EditText textPassword;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.displaytoolbar();

        Button button = getActivity().findViewById(R.id.loging1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userMail = getActivity().findViewById(R.id.edit_userMail);
                EditText userPassWord = getActivity().findViewById(R.id.edit_userpassword);
                mListener.displayUserAccount(userMail.getText().toString(),userPassWord.getText().toString());

            }
        });

        Intent intent = getActivity().getIntent();
        if (intent != null){
            String s = intent.getStringExtra("result");
            if (s != null){
                final Button view = getView().findViewById(R.id.loging01);
                Button vieww = getActivity().findViewById(R.id.loging1);
                vieww.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                Button button1 = getActivity().findViewById(R.id.loging01);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText userMail = getActivity().findViewById(R.id.edit_userMail);
                        EditText userPassWord = getActivity().findViewById(R.id.edit_userpassword);
                        mListener.setTheReviews(userMail.getText().toString(),userPassWord.getText().toString());

                    }
                });
             }
        }


    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        textFirstName = root.findViewById(R.id.edit_userName);
        textLastName = root.findViewById(R.id.edit_userfirstName);
        textMails = root.findViewById(R.id.editM);
        textPassword = root.findViewById(R.id.editPass);

        TextView textView = root.findViewById(R.id.userInscription);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = root.findViewById(R.id.pageAccount);
                view.setVisibility(View.GONE);
                View view1 = root.findViewById(R.id.pageUser);
                view1.setVisibility(View.VISIBLE);

                Button buttonApplay = root.findViewById(R.id.logingUser);
                buttonApplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.userApplayingKungry(textFirstName.getText().toString(),
                                textLastName.getText().toString(),textMails.getText().toString(),
                                textPassword.getText().toString());
                    }
                });

                TextView textCon = root.findViewById(R.id.userInscription1);
                textCon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View view = root.findViewById(R.id.pageAccount);
                        view.setVisibility(View.VISIBLE);
                        View view1 = root.findViewById(R.id.pageUser);
                        view1.setVisibility(View.GONE);

                    }
                });


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
    public void onResume() {
        Button button = getActivity().findViewById(R.id.loging1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userMail = getActivity().findViewById(R.id.edit_userMail);
                EditText userPassWord = getActivity().findViewById(R.id.edit_userpassword);
                Intent intent = getActivity().getIntent();
                if (intent != null){
                    String s = intent.getStringExtra("result");
                    if (s != null){
                        mListener.setTheReviews(userMail.getText().toString(),userPassWord.getText().toString());

                    }
                }

                mListener.displayUserAccount(userMail.getText().toString(),userPassWord.getText().toString());


            }
        });
        super.onResume();

    }

    @Override
    public void onPause() {
        Button button = getActivity().findViewById(R.id.loging1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userMail = getActivity().findViewById(R.id.edit_userMail);
                EditText userPassWord = getActivity().findViewById(R.id.edit_userpassword);
                Intent intent = getActivity().getIntent();
                if (intent != null){
                    String s = intent.getStringExtra("result");
                    if (s != null){
                        mListener.setTheReviews(userMail.getText().toString(),userPassWord.getText().toString());

                    }
                }

                mListener.displayUserAccount(userMail.getText().toString(),userPassWord.getText().toString());


             }
        });
        super.onPause();
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
        void setTheReviews(String userMail,String userPassWord);
        void userApplayingKungry(String Name, String lastName,String mail,String pass);
    }
}