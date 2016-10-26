package com.torres.shopfinder.service;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.torres.shopfinder.model.Shop;
import com.torres.shopfinder.service.GMapShopFinderService;
import com.torres.shopfinder.service.GeocodingApiRequestProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GMapShopFinderServiceTest {
    GMapShopFinderService gMapShopFinderService;

    GeocodingApiRequestProxy geocodingApiRequestProxy = mock(GeocodingApiRequestProxy.class);

    private static final double DELTA = 1e-15;

    @Test
    public void testFinderFound() throws Exception {
        Geometry geometry = new Geometry();
        LatLng latLng = new LatLng(11.11, 22.22);
        geometry.location = latLng;
        GeocodingResult geocodingResult = new GeocodingResult();
        geocodingResult.geometry = geometry;

        GeocodingResult[] result = new GeocodingResult[1];
        result[0] = geocodingResult;
        when(geocodingApiRequestProxy.connect(any())).thenReturn(result);
        gMapShopFinderService = new GMapShopFinderService(geocodingApiRequestProxy);

        Shop shop = new Shop("shop1", "High Street", "se1 5rt");
        gMapShopFinderService.findGeolocation(shop);
        assertEquals(11.11, shop.getGeometry().get().location.lat, DELTA);
        assertEquals(22.22, shop.getGeometry().get().location.lng, DELTA);
    }

    @Test
    public void testFinderNotFound() throws Exception {
        GeocodingResult[] result = new GeocodingResult[0];
        when(geocodingApiRequestProxy.connect(any())).thenReturn(result);
        gMapShopFinderService = new GMapShopFinderService(geocodingApiRequestProxy);

        Shop shop = new Shop("shop1", "High Street", "se1 5rt");
        gMapShopFinderService.findGeolocation(shop);
        assertFalse(shop.getGeometry().isPresent());
    }
}
