package com.edqueeneland.geonames;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarthQuakes {

    @SerializedName("earthquakes")
    @Expose
    private List<EarthQuake> earthquakes = null;

    public List<EarthQuake> getEarthquakes() {
        return earthquakes;
    }

    public void setEarthquakes(List<EarthQuake> earthquakes) {
        this.earthquakes = earthquakes;
    }

}