package ca.ulaval.ima.mp.api;

import ca.ulaval.ima.mp.domain.RestaurantResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {
    @GET("restaurant/search/")
    Call<RestaurantResponse> getPopularMovies(@Query("page") int restoPage,
                                              @Query("page_size") int restoPageSize,
                                              @Query("latitude") double restoLatitude,
                                              @Query("longitude") double restoLongitude,
                                              @Query("radius") int restoRadius);
}
