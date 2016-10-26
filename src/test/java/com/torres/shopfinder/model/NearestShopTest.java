package com.torres.shopfinder.model;

import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.torres.shopfinder.model.NearestShop;
import com.torres.shopfinder.model.Shop;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
public class NearestShopTest {
    NearestShop instance;
    private static final double DELTA = 1e-15;

    @Test(expected = IllegalArgumentException.class)
    public void noShop() {
        instance = new NearestShop(0.0, 0.0, null);
    }

    @Test
    public void shop() {
        Geometry geometry = new Geometry();
        geometry.location = new LatLng(-0.232, 41.22);
        instance = new NearestShop(0.0, 0.0, new Shop("shop1", "1 High Street", "se1 1rf", Optional.of(geometry)));
        assertEquals(2847.926843023928, instance.getDistance(), DELTA);
    }
}
