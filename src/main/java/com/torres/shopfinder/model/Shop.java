package com.torres.shopfinder.model;

import com.google.maps.model.Geometry;
import com.torres.shopfinder.dto.ShopDto;

import java.util.Optional;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
public final class Shop {
    private final String name;

    private final String address;

    private final String postCode;

    private Optional<Geometry> geometry;

    public Shop(String name, String address, String postCode) {
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        geometry = Optional.empty();
    }

    public Shop(String name, String address, String postCode, Optional<Geometry> geometry) {
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    public Optional<Geometry> getGeometry() {
        return geometry;
    }

    public void setGeometry(Optional<Geometry> geometry) {
        this.geometry = geometry;
    }

    public ShopDto toDto() {
        double latitude = geometry.isPresent() ? geometry.get().location.lat : 0.0;
        double longitude = geometry.isPresent() ? geometry.get().location.lng : 0.0;

        return new ShopDto(name, address, postCode, latitude, longitude);
    }
}
