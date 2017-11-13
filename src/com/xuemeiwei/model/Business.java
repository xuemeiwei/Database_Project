package com.xuemeiwei.model;

public class Business {
    String business_id;
    String city;
    String state;
    String name;
    double stars;
    Business (String business_id, String city, String state, String name, double stars) {
        this.business_id = business_id;
        this.city = city;
        this.state = state;
        this.name = name;
        this.stars = stars;
    }
}
