package com.torres.shopfinder.controller;

import com.torres.shopfinder.dto.ShopDto;
import com.torres.shopfinder.model.NearestShop;
import com.torres.shopfinder.model.Shop;
import com.torres.shopfinder.service.ShopFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Comparator.comparing;

/**
 * Created by javierbracerotorres on 26/10/2016.
 */
@RestController
@RequestMapping("/shops")
class ShopRestController {

    private static final Logger logger = LoggerFactory.getLogger(ShopRestController.class);

    Queue<Shop> shops = new ConcurrentLinkedQueue<Shop>();

    @Autowired
    ShopFinderService shopFinderService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody ShopDto shopDto) {
        Shop shop = shopDto.toShop();
        logger.info("Request to search for the shop [{}]",
                shop.getName(), shop.getAddress(), shop.getPostCode());


        shops.add(shop);
        shopFinderService.findGeolocation(shop);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{name}")
                .buildAndExpand(shop.getName()).toUri());

        logger.info("Finished request for shop [{}]", shop.getName());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{name}")
    ResponseEntity<?> findByName(@PathVariable String name) {

        Optional<Shop> shop = shops.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();

        if (shop.isPresent()) {
            return new ResponseEntity<>(shop.get().toDto(), null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value="/nearest")
    ResponseEntity<?> nearest(@RequestParam("customerLongitude") double longitude,
                              @RequestParam("customerLatitude") double latitude) {

        if (shops.isEmpty()) {
            return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
        }

        NearestShop shopNearest = shops.stream()
                .filter(s -> s.getGeometry().isPresent())
                .map(s -> new NearestShop(longitude, latitude, s))
                .sorted(comparing(NearestShop::getDistance))
                .findFirst()
                .get();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{name}")
                .buildAndExpand(shopNearest.getShop().getName()).toUri());


        return new ResponseEntity<>(shopNearest.getShop().toDto(), httpHeaders, HttpStatus.OK);
    }
}
