package com.torres.shopfinder.dto;

import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.torres.shopfinder.model.Shop;

import java.util.Optional;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
public class ShopDto {
    private String name;

    private String address;

    private String postCode;

    private double latitude;

    private double longitude;

    public ShopDto(){

    }

    public ShopDto(String name, String address, String postCode, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Shop toShop() {
        LatLng latLng = new LatLng(latitude, longitude);
        Geometry geometry = new Geometry();
        geometry.location = latLng;
        return new Shop(name, address, postCode, Optional.of(geometry));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
