package ca.ulaval.ima.mp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.ulaval.ima.mp.utils.RecyclerAdapter;

public class ReviewsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        getSupportActionBar().hide();
        ImageView imageView = findViewById(R.id.iconBack);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent != null){
            ArrayList<String> reviews = intent.getStringArrayListExtra("reviewsCards");
            ArrayList<String> reviewsStar = intent.getStringArrayListExtra("reviewsStars");
            ArrayList<String> reviewsNames = intent.getStringArrayListExtra("reviewsNames");
            ArrayList<String> reviewsComment = intent.getStringArrayListExtra("reviewsComs");
            ArrayList<String> reviewsImg = intent.getStringArrayListExtra("reviewsImgs");
            TextView textView = findViewById(R.id.txteval12);
            if (reviews != null && reviewsStar != null &&
                    reviewsNames != null && reviewsComment != null &&
                    reviewsImg != null){

                textView.setText("("+reviews.size()+")");



                recyclerView = findViewById(R.id.recycler_view1);

                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new RecyclerAdapter(this,reviews,reviewsStar,reviewsNames,reviewsComment,reviewsImg);
                recyclerView.setAdapter(adapter);

            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
