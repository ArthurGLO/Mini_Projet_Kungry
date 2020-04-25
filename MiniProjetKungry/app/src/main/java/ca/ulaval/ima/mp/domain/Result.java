
package ca.ulaval.ima.mp.domain;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cuisine")
    @Expose
    private List<Cuisine> cuisine = null;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("review_average")
    @Expose
    private float reviewAverage;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("location")
    @Expose
    private Location location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cuisine> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<Cuisine> cuisine) {
        this.cuisine = cuisine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public float getReviewAverage() {
        return reviewAverage;
    }

    public void setReviewAverage(float reviewAverage) {
        this.reviewAverage = reviewAverage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
