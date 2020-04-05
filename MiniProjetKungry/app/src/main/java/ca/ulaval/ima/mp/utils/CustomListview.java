package ca.ulaval.ima.mp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.ulaval.ima.mp.R;

public class CustomListview extends ArrayAdapter<String> {
    private  ArrayList<String> restoTitle ;
    private ArrayList<String> restoReviews ;
    private ArrayList<String> restoKilometer ;
    private ArrayList<String> restoStars ;
    private ArrayList<String> restoImg;
    private ArrayList<String> restoType;
    private Activity context;

    public CustomListview(Activity context, ArrayList<String> restoTitle,
                          ArrayList<String> restoReviews,
                          ArrayList<String> restoKilometer,
                          ArrayList<String> restoStars,
                          ArrayList<String> restoImg,
                          ArrayList<String> restoType) {
        super(context, R.layout.listviewofferbymodel,restoTitle);
        this.context = context;
        this.restoTitle = restoTitle;
        this.restoImg = restoImg;
        this.restoStars = restoStars;
        this.restoKilometer = restoKilometer;
        this.restoReviews = restoReviews;
        this.restoType = restoType;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;

        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listviewofferbymodel,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.name.setText(restoTitle.get(position));
        viewHolder.descript.setText(restoType.get(position)+"/Food + Comfort food");
        viewHolder.kilometer.setText(restoKilometer.get(position)+""+"km");
        Picasso.with(context).load(restoImg.get(position)).resize(140,115).into(viewHolder.imageView);
        viewHolder.ratings.setRating(Float.parseFloat(restoStars.get(position)));
        viewHolder.reviews.setText("("+restoReviews.get(position)+")");



        return r;

    }

    class ViewHolder{
        TextView name;
        TextView descript;
        TextView kilometer;
        RatingBar ratings;
        TextView reviews;
        ImageView imageView;

        ViewHolder(View v){
            name = v.findViewById(R.id.restoname);
            descript = v.findViewById(R.id.descriptiontype);
            kilometer = v.findViewById(R.id.textdistance);
            ratings = v.findViewById(R.id.MyRating);
            imageView = v.findViewById(R.id.imageView);
            reviews = v.findViewById(R.id.reviewsCount);
        }


    }
}
