package com.example.altayattractions.domain;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private final long id;
    private final String name;
    private final String pathToImage;
    private final String address;
    private final String informations;
    private final LatLng latLng;

    public Place(long id, String name, String pathToImage, String address, String informations, LatLng latLng){
        this.id = id;
        this.name = name;
        this.pathToImage = pathToImage;
        this.address = address;
        this.informations = informations;
        this.latLng = latLng;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public String getAddress() {
        return address;
    }

    public String getInformations() {
        return informations;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
