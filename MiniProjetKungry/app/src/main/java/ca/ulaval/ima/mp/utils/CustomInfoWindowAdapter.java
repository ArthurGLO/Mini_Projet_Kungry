package ca.ulaval.ima.mp.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.domain.Restaurant;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.listviewofrestaurant, null);

        TextView tvTitle =  view.findViewById(R.id.restoname);
        TextView dist = view.findViewById(R.id.textdistance);
        Restaurant restaurant = new Restaurant();
         restaurant = (Restaurant) marker.getTag();
        TextView txtreviews =  view.findViewById(R.id.reviewsCount);
        ImageView img =  view.findViewById(R.id.imageView);
        TextView type =  view.findViewById(R.id.descriptiontype);
        RatingBar rate =  view.findViewById(R.id.MyRating);


        tvTitle.setText(marker.getTitle());
        dist.setText(marker.getSnippet()+" "+"km");
        txtreviews.setText(String.valueOf(restaurant.getReview_count()));
        type.setText(restaurant.getType()+"/Food + Comfort food");
        rate.setRating(restaurant.getReview_average());
        Picasso.with(context).load(restaurant.getImage()).resize(140,115).into(img);



        return view;
    }
}
