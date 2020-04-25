package ca.ulaval.ima.mp.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.ulaval.ima.mp.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<String> reviewsDate ;
    private ArrayList<String> reviewsRate ;
    private ArrayList<String> reviewsName ;
    private ArrayList<String> reviewsDesc ;
    private ArrayList<String> reviewsImg;
    private Activity context;

    public RecyclerAdapter (Activity context, ArrayList<String> reviewsDate,
                          ArrayList<String> reviewsRate,
                          ArrayList<String> reviewsName,
                          ArrayList<String> reviewsDesc,
                          ArrayList<String> reviewsImg) {
        super();
        this.context = context;
        this.reviewsDate = reviewsDate;
        this.reviewsName = reviewsName;
        this.reviewsRate = reviewsRate;
        this.reviewsDesc = reviewsDesc;
        this.reviewsImg = reviewsImg;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImage;
        public TextView itemDate;
        public TextView itemName;
        public TextView itemDesc;
        public RatingBar itemrate;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.imgcard);
            itemDate = itemView.findViewById(R.id.item_date);
            itemName = itemView.findViewById(R.id.item_name);
            itemDesc = itemView.findViewById(R.id.item_desc);
            itemrate = itemView.findViewById(R.id.MyRatingCard);
            relativeLayout = itemView.findViewById(R.id.cardviewlayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.itemDate.setText(reviewsDate.get(i));
        viewHolder.itemrate.setRating(Float.parseFloat(reviewsRate.get(i)));
        viewHolder.itemDesc.setText(reviewsDesc.get(i));
        viewHolder.itemName.setText(reviewsName.get(i));
        if(reviewsImg.get(i) == null){
            viewHolder.relativeLayout.removeView(viewHolder.itemImage);
            viewHolder.itemImage.setVisibility(View.GONE);
        }else {
            Picasso.with(context).load(reviewsImg.get(i)).resize(100,100).into(viewHolder.itemImage);
        }

        viewHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageView img = context.findViewById(R.id.img1);
                if(img != null){
                    img.setVisibility(View.GONE);
                    RelativeLayout relativeLayoutImg = context.findViewById(R.id.shapImg);
                    relativeLayoutImg.setVisibility(View.VISIBLE);
                    ImageView userImg = context.findViewById(R.id.backImg1);
                    Picasso.with(context).load(reviewsImg.get(i)).resize(440,250).into(userImg);

                }

                if(img != null){
                    ImageView userImg1 = context.findViewById(R.id.backImg2);
                    userImg1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            img.setVisibility(View.VISIBLE);
                            RelativeLayout relativeLayoutImg = context.findViewById(R.id.shapImg);
                            relativeLayoutImg.setVisibility(View.INVISIBLE);

                        }
                    });
                }



                final RelativeLayout relativeLayoutImg = context.findViewById(R.id.shapImg1);
                ImageView userImg1 = context.findViewById(R.id.backImg21);
                final ImageView userback = context.findViewById(R.id.cardImg);
                if(relativeLayoutImg != null){
                    userback.setVisibility(View.GONE);
                    relativeLayoutImg.setVisibility(View.VISIBLE);
                    ImageView userImg = context.findViewById(R.id.backImg11);
                    Picasso.with(context).load(reviewsImg.get(i)).resize(440,250).into(userImg);

                }
                if(relativeLayoutImg != null){
                    userImg1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userback.setVisibility(View.VISIBLE);
                            relativeLayoutImg.setVisibility(View.INVISIBLE);

                        }
                    });
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return reviewsDate.size();
    }
}
