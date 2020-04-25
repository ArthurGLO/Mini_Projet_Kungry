
package ca.ulaval.ima.mp.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RestaurantResponse {

    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("results")
    @Expose
    private ArrayList<Result> results = new ArrayList<>();

    public Content getContent() {
        return content;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
