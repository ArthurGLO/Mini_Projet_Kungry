package ca.ulaval.ima.mp.domain;

public class Restaurant {

    private String restaurantName;
    private String cookName;
    private String type;
    private int review_count;
    private  float review_average;
    private String image;
    private String distance;
    private String location;

    public Restaurant( String restaurantName, String cookName, String type,
                       int review_count, float review_average, String image, String distance, String location){

        this.restaurantName = restaurantName;
        this.cookName = cookName;
        this.type = type;
        this.review_count = review_count;
        this.review_average = review_average;
        this.image = image;
        this.distance = distance;
        this.location = location;
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
}


