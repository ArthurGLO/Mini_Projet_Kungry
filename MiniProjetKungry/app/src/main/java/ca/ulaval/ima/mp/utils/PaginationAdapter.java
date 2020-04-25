package ca.ulaval.ima.mp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.RestaurantDetails;
import ca.ulaval.ima.mp.domain.Restaurant;
import ca.ulaval.ima.mp.domain.Result;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Result> restoResults;
    private Context context;

    private boolean isLoadingAdded = false;


    public PaginationAdapter(Context context) {
        this.context = context;
        restoResults = new ArrayList<>();
    }

    public List<Result> getRestos() {
        return restoResults;
    }

    public void setRestos(List<Result> movieResults) {
        this.restoResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.listviewofrestaurant, parent, false);
        viewHolder = new RestautVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Result result = restoResults.get(position);


        switch (getItemViewType(position)) {
            case ITEM:
                final RestautVH restoVH = (RestautVH) holder;

                restoVH.name.setText(result.getName());
                restoVH.descript.setText(result.getType()+"/Food • Confort food");
                restoVH.kilometer.setText(result.getDistance()+" "+"km");
                Picasso.with(context).load(result.getImage()).resize(140,115).into(restoVH.imageView);
                restoVH.ratings.setRating(result.getReviewAverage());
                restoVH.reviews.setText("("+result.getReviewCount()+")");

                restoVH.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRestaurantDescription(result.getId(),result.getLocation().getLatitude(),result.getLocation().getLongitude());
                    }
                });

                Glide
                        .with(context)
                        .load(result.getImage())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                restoVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                restoVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(restoVH.imageView);


                break;

            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return restoResults == null ? 0 : restoResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == restoResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }



    private void getRestaurantDescription(final int id, final double lat, final double longitude){
        final ArrayList<String> reviewsDate = new ArrayList<>();
        final ArrayList<String> reviewsRates = new ArrayList<>();
        final ArrayList<String> reviewsName = new ArrayList<>();
        final ArrayList<String> reviewsDesc = new ArrayList<>();
        final ArrayList<String> reviewsImages = new ArrayList<>();
        final ArrayList<String> hour = new ArrayList<>();

        final RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "https://kungry.ca/api/v1/restaurant/"+id+"/?latitude="+lat+"&longitude="+longitude;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final JSONObject jsonObject = response.getJSONObject("content");
                            JSONObject objLocation = jsonObject.getJSONObject("location");
                            double lat = objLocation.getDouble("latitude");
                            double longitude = objLocation.getDouble("longitude");

                            JSONArray reviewsArray = jsonObject.getJSONArray("reviews");
                            String evluation = String.valueOf(reviewsArray.length());
                            for(int i =0 ; i < 3;i++){
                                if(reviewsArray.length() != 0 && !(reviewsArray.isNull(i))) {

                                    JSONObject object1 = reviewsArray.getJSONObject(i);
                                    String revDate = object1.getString("date");

                                    String[] words = revDate.split("-");

                                    String result = null;
                                    if (words[1].equals("01")) {
                                        result = "janvier";

                                    } else if (words[1].equals("02")) {
                                        result = "février";
                                    } else if (words[1].equals("03")) {
                                        result = "mars";
                                    } else if (words[1].equals("04")) {
                                        result = "avril";
                                    } else if (words[1].equals("05")) {
                                        result = "mai";
                                    } else if (words[1].equals("06")) {
                                        result = "juin";
                                    } else if (words[1].equals("07")) {
                                        result = "juillet";
                                    } else if (words[1].equals("08")) {
                                        result = "août";
                                    } else if (words[1].equals("09")) {
                                        result = "septembre";
                                    } else if (words[1].equals("10")) {
                                        result = "octobre";
                                    } else if (words[1].equals("11")) {
                                        result = "novembre";
                                    } else if (words[1].equals("12")) {
                                        result = "décembre";
                                    }

                                    String newDate = words[2] + " " +
                                            result + " " +
                                            words[0];
                                    reviewsDate.add(newDate);

                                    float rate = (float) object1.getDouble("stars");
                                    String comment = object1.getString("comment");
                                    String reviewimg = object1.getString("image");
                                    String revfirstName = object1.getJSONObject("creator").getString("first_name");
                                    String revlastName = object1.getJSONObject("creator").getString("last_name");

                                    reviewsImages.add(reviewimg);
                                    reviewsDesc.add(comment);
                                    reviewsName.add(revfirstName + " " + revlastName);
                                    reviewsRates.add(String.valueOf(rate));
                                }

                            }
                            double[] latLng = {lat,longitude};

                            JSONArray jsonArray1 = jsonObject.getJSONArray("opening_hours");
                            if (jsonArray1.length() == 0){
                                for(int l = 0; l<8;l++){
                                    hour.add("Aucun");
                                }
                            }else {
                                for(int i =0 ; i < jsonArray1.length();i++){

                                    if(jsonArray1.length() != 0){
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        String open = jsonObject1.getString("opening_hour");
                                        String close = jsonObject1.getString("closing_hour");
                                        if(open.isEmpty() && close.isEmpty()){
                                            hour.add("Fermé");
                                        }else if(jsonArray1.length() > 0 && open.equals("null") && close.equals("null")){

                                        }else{
                                            String[] word = open.split(":");
                                            String[] word1 = close.split(":");
                                            String nnw = word[0]+":"+word[1];
                                            String nw1 = word1[0]+":"+word1[1];
                                            String resultFinal = nnw+" à "+nw1;

                                            hour.add(resultFinal);
                                        }
                                    }
                                }
                            }

                            String name = jsonObject.getString("name");
                            String distance = jsonObject.getString("distance");
                            int reviewCount = jsonObject.getInt("review_count");
                            float revAverage = (float)jsonObject.getDouble("review_average");
                            String imgrev = jsonObject.getString("image");
                            String webSite = jsonObject.getString("website");
                            String phone = jsonObject.getString("phone_number");
                            String typeRest = jsonObject.getString("type");

                            Restaurant restaurant = new Restaurant(name,webSite,phone,typeRest,reviewCount,revAverage,
                                    imgrev,distance);

                            Intent intent = new Intent(context, RestaurantDetails.class);
                            intent.putExtra("location", latLng);
                            intent.putExtra("restdist", distance);
                            intent.putExtra("resto",restaurant);
                            intent.putExtra("eval",evluation);
                            intent.putExtra("restoId",String.valueOf(id));
                            intent.putStringArrayListExtra("reviewsHeures",hour);
                            intent.putStringArrayListExtra("reviewsCards",reviewsDate);
                            intent.putStringArrayListExtra("reviewsStars",reviewsRates);
                            intent.putStringArrayListExtra("reviewsNames",reviewsName);
                            intent.putStringArrayListExtra("reviewsComs",reviewsDesc);
                            intent.putStringArrayListExtra("reviewsImgs",reviewsImages);
                            ((Activity) context).startActivityForResult(intent,12345);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        queue.add(getRequest);
    }



    /*
   _________________________________________________________________________________________________
    */

    public void add(Result r) {
        restoResults.add(r);
        notifyItemInserted(restoResults.size() - 1);
    }

    public void addAll(List<Result> moveResults) {
        for (Result result : moveResults) {
            add(result);
        }
    }

    public void remove(Result r) {
        int position = restoResults.indexOf(r);
        if (position > -1) {
            restoResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = restoResults.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            restoResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return restoResults.get(position);
    }


   /*
   _________________________________________________________________________________________________
    */

    protected class RestautVH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView descript;
        private TextView kilometer;
        private RatingBar ratings;
        private TextView reviews;
        private ImageView imageView;
        private ProgressBar mProgress;

        private View mView;

        public RestautVH(View v) {
            super(v);

            name = v.findViewById(R.id.restoname);
            descript = v.findViewById(R.id.descriptiontype);
            kilometer = v.findViewById(R.id.textdistance);
            ratings = v.findViewById(R.id.MyRating);
            imageView = v.findViewById(R.id.imageView);
            reviews = v.findViewById(R.id.reviewsCount);
            mProgress = v.findViewById(R.id.resto_progress);

            mView = v;
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}