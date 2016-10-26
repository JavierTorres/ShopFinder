package com.torres.shopfinder.service;

import com.google.maps.model.GeocodingResult;
import com.torres.shopfinder.model.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
@Service
public class GMapShopFinderService implements ShopFinderService {
    private static final Logger logger = LoggerFactory.getLogger(GMapShopFinderService.class);

    GeocodingApiRequestProxy geocodingApiRequestProxy;

    @Autowired
    public GMapShopFinderService(GeocodingApiRequestProxy geocodingApiRequestProxy) {
        this.geocodingApiRequestProxy = geocodingApiRequestProxy;
    }

    @Async
    public void findGeolocation(Shop shop) {
        try {
            GeocodingResult[] result = geocodingApiRequestProxy.connect(shop.getAddress() + " " + shop.getPostCode());
            if (result.length > 0) {
                shop.setGeometry(Optional.of(result[0].geometry));
            }
        } catch (Exception e) {
            logger.warn("There is a problem finding the geolocation for the shop [{}]", shop.getName(), e);
        }

        logger.info("Shop [{}] with the following coordinates [{}]",
                shop.getName(),  shop.getGeometry().isPresent() ? shop.getGeometry().get().location : "not found");
    }
}
