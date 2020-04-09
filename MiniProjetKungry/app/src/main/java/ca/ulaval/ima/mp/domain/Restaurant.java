package ca.ulaval.ima.mp.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {

    private String restaurantName;
    private String cookName;
    private String type;
    private int review_count;
    private  float review_average;
    private String image;
    private String distance;
    private String location;
    private String webSite ;
    private String phone;

    public Restaurant( String restaurantName, String webSite, String phone, String type,
                       int review_count, float review_average, String image, String distance){

        this.restaurantName = restaurantName;
        this.webSite = webSite;
        this.phone = phone;
        this.type = type;
        this.review_count = review_count;
        this.review_average = review_average;
        this.image = image;
        this.distance = distance;
    }


    public Restaurant( String restaurantName, String type,
                       int review_count, float review_average, String image, String distance){

        this.restaurantName = restaurantName;
        this.type = type;
        this.review_count = review_count;
        this.review_average = review_average;
        this.image = image;
        this.distance = distance;
    }

    public Restaurant(){}

    public String getWebSite() {
        return webSite;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public float getReview_average() {
        return review_average;
    }

    public int getReview_count() {
        return review_count;
    }

    public String getCookName() {
        return cookName;
    }

    public String getImage() {
        return image;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getDistance() {
        return distance;
    }

    public String getLocation() {
        return location;
    }

    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    public String getType() {
        return type;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setReview_average(float review_average) {
        this.review_average = review_average;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    protected Restaurant(Parcel in) {
        restaurantName = in.readString();
        cookName = in.readString();
        type = in.readString();
        review_count = in.readInt();
        review_average = in.readFloat();
        image = in.readString();
        distance = in.readString();
        location = in.readString();
        webSite = in.readString();
        phone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(restaurantName);
        dest.writeString(cookName);
        dest.writeString(type);
        dest.writeInt(review_count);
        dest.writeFloat(review_average);
        dest.writeString(image);
        dest.writeString(distance);
        dest.writeString(location);
        dest.writeString(webSite);
        dest.writeString(phone);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}