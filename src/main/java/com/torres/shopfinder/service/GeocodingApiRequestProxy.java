package com.torres.shopfinder.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
@Service
public class GeocodingApiRequestProxy {

    @Value("${api.key}")
    private String apiKey;

    @Value("${geo.api.query.rate.limit}")
    private int queryRateLimit;

    @Value("${geo.api.query.retry.timeout}")
    private int retryTimeout;

    @Value("${geo.api.query.connect.timeout}")
    private int connectTimeout;

    public GeocodingResult[] connect(String address) throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
        context.setQueryRateLimit(queryRateLimit);
        context.setRetryTimeout(retryTimeout, TimeUnit.MILLISECONDS);
        context.setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS);

        return GeocodingApi.newRequest(context).address(address).await();
    }
}
