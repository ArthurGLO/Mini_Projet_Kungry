package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserReview extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText editText;
    private int myrates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        getSupportActionBar().hide();
        ImageView imageView = findViewById(R.id.iconBack);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ratingBar = findViewById(R.id.ratingIt);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myrates = (int)ratingBar.getRating();
            }
        });

        editText = findViewById(R.id.postcomments);
        final String coms = editText.getText().toString();

        Button button = findViewById(R.id.post);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = getIntent();
                if (intent != null){
                    final String restauId = intent.getStringExtra("restoIdd");
                    final String revToken = intent.getStringExtra("tokenForReview1");
                    final String revTokenType = intent.getStringExtra("tokenForReviewType1");

                    if (restauId != null
                            && revToken != null  && revTokenType != null){
                        final RequestQueue queue = Volley.newRequestQueue(UserReview.this);
                        final String url = "https://kungry.ca/api/v1/review/";
                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                      onBackPressed();
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Log.d("Error.Response", error.toString());
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String>  params = new HashMap<>();
                                params.put("Authorization",revTokenType+" "+revToken);

                                return params;
                            }

                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> params = new HashMap<>();
                                params.put("restaurant_id", restauId);
                                params.put("stars", myrates+"");
                                params.put("comment", coms);
                                return params;
                            }
                        };
                        queue.add(postRequest);
                    }
                }
            }
        });

    }
}
