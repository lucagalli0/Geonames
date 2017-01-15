package com.edqueeneland.geonames;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String BASE_URL = "http://api.geonames.org/";

    public interface GeoNamesService {
        @GET(BASE_URL+"earthquakesJSON")
        Call<EarthQuakes> getEarthQuakes(@Query("north") String north,
                                         @Query("south") String south,
                                         @Query("west") String west,
                                         @Query("east") String east,
                                         @Query("username") String username,
                                         @Query("password") String password
        );
    }

    private GoogleMap mMap;

    List<EarthQuake> le;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeoNamesService geo = retrofit.create(GeoNamesService.class);
        Call<EarthQuakes> call = geo.getEarthQuakes("44", "9", "22", "55", "jimb0", "gorillaz");
        Request r = call.request();
        Log.d("request", r.toString());

        call.enqueue(new Callback<EarthQuakes>() {
            @Override
            public void onResponse(Call<EarthQuakes> call, Response<EarthQuakes> response) {
                if (response.isSuccessful()) {
                    Log.d("resp", "sono dentro");
                    EarthQuakes lista = response.body();
                    le = lista.getEarthquakes();
                }
                else Log.d("resp", "response non è successfull");
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MainActivity.this);
            }

            @Override
            public void onFailure(Call<EarthQuakes> call, Throwable t) {

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (EarthQuake e : le) {
            LatLng pos = new LatLng(e.getLat(), e.getLng());
            mMap.addMarker(new MarkerOptions().position(pos).title(e.getDatetime() + " Magnitude " + e.getMagnitude()));
        }
    }

}
