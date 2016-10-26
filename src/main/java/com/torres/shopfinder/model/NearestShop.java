package com.torres.shopfinder.model;

import com.google.maps.model.LatLng;
import com.torres.shopfinder.model.Shop;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
public final class NearestShop {
    private final Shop shop;
    private final double distance;
    private final static double DISTANCE_UNIT = 60 * 1.1515; // In miles
    private final static double UNIT_CONVERT = 180.0;

    public NearestShop(final double long1, final double lat1, final Shop shop) {
        if (shop == null) {
            throw new IllegalArgumentException("Shop is required");
        }
        this.shop = shop;
        distance = calculateDistance(long1, lat1);
    }

    public Shop getShop() {
        Shop shopNew = new Shop(shop.getName(), shop.getAddress(), shop.getPostCode());
        shopNew.setGeometry(shop.getGeometry());
        return shopNew;
    }

    public double getDistance() {
        return distance;
    }

    /**
     * Calculates the distance between the shop coordiantes and the coordinates passed as input parameters.
     *
     * @param long1
     * @param lat1
     * @return the distance calculation in miles
     */
    private double calculateDistance(final double long1, final double lat1) {
        LatLng shopLoc = shop.getGeometry().get().location;
        double theta = long1 - shopLoc.lng;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(shopLoc.lat))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(shopLoc.lat))
                * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist * DISTANCE_UNIT;
    }

    /**
     * Converts decimal degrees to radians.
     *
     * @param deg
     * @return
     */
    private double deg2rad(double deg) {
        return (deg * Math.PI / UNIT_CONVERT);
    }

    /**
     * Converts radians to decimal degrees.
     *
     * @param rad
     * @return
     */
    private double rad2deg(double rad) {
        return (rad * UNIT_CONVERT / Math.PI);
    }
}
