package com.torres.shopfinder.controller;

import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.torres.shopfinder.model.Shop;
import com.torres.shopfinder.service.ShopFinderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopRestControllerTest {

    @MockBean
    private ShopFinderService shopFinderService;

    private MediaType contentType = new MediaType("application", "hal+json");

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void addShop() throws Exception {
        String shop1 = json(new Shop("Torres3", "3 High Street", "sg3 8au").toDto());

        this.mockMvc.perform(post("/shops")
                .contentType(contentType)
                .content(shop1))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/shops/Torres3"));
    }

    @Test
    public void findByNameFound() throws Exception {
        LatLng latLng1 = new LatLng(55.802037, -0.086374);
        Geometry geometry1 = new Geometry();
        geometry1.location = latLng1;
        String shop1 = json(new Shop("Torres4", "4 High Street", "sg4 8au", Optional.of(geometry1)).toDto());

        this.mockMvc.perform(post("/shops")
                .contentType(contentType)
                .content(shop1))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/shops/Torres4"));;

        this.mockMvc.perform(get("/shops/Torres4"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Torres4\",\"address\":\"4 High Street\"," +
                        "\"postCode\":\"sg4 8au\",\"latitude\":55.802037,\"longitude\":-0.086374}"));
    }

    @Test
    public void findByNameNotFound() throws Exception {
        this.mockMvc.perform(get("/shops/notExist"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void nearest() throws Exception {
        LatLng latLng1 = new LatLng(51.802037, -0.066374);
        Geometry geometry1 = new Geometry();
        geometry1.location = latLng1;
        String shop1 = json(new Shop("Torres", "1 High Street", "sg1 7au", Optional.of(geometry1)).toDto());

        this.mockMvc.perform(post("/shops")
                .contentType(contentType)
                .content(shop1))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/shops/Torres"));;

        LatLng latLng2 = new LatLng(53.643237, -0.264374);
        Geometry geometry2 = new Geometry();
        geometry2.location = latLng2;
        String shop2 = json(new Shop("Torres2", "10 Princess Street", "E1 8FG", Optional.of(geometry2)).toDto());

        this.mockMvc.perform(post("/shops")
                .contentType(contentType)
                .content(shop2))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/shops/Torres2"));;

        // Check the nearest shop is Torres 1 High Street sg1 7au
        this.mockMvc.perform(get("/shops/nearest?customerLongitude=-0.066374&customerLatitude=51.802037"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Torres\",\"address\":\"1 High Street\"," +
                        "\"postCode\":\"sg1 7au\",\"latitude\":51.802037,\"longitude\":-0.066374}"));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
