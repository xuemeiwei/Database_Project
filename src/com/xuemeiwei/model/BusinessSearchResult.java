package com.xuemeiwei.model;

public class BusinessSearchResult {
    String bid;
    String address;
    String city;
    String state;
    String stars;
    String reviews;
    String checkins;
    public BusinessSearchResult(String result) {
        if (result == null || result.equals("")) {
            return;
        }
        String[] results = result.split(",");
        bid = results[0];
        address = results[1];
        city = results[2];
        state = results[3];
        stars = results[4];
        reviews = results[5];
        checkins = results[6];
    }
}
