package com.xuemeiwei.model;

import com.xuemeiwei.common.ModelUtils;

import java.util.ArrayList;
import java.util.Observable;

public class Yelp extends Observable{


    private ArrayList<String> mainCategories;
    private ArrayList<String> subCategories;
    private ArrayList<String> attributes;
    private ArrayList<String> locations;
    private String location;
    private String city;
    private String state;
    private String day;
    private String fromTime;
    private String toTime;
    private String label = "";
    private String business = "";

    public Yelp() {
        mainCategories = new ArrayList<>();
        subCategories = new ArrayList<>();
        attributes = new ArrayList<>();
        locations = new ArrayList<>();
        location = "";
        day = "";
        fromTime = "";
        toTime = "";
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public ArrayList<String> getMainCategories() {
        return mainCategories;
    }

    public ArrayList<String> getSubCategories() {
        return subCategories;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public ArrayList<String> getLocationList() {
        return locations;
    }

    public void setMainCategory (String newMainCategory, boolean isAdd) {
        label = ModelUtils.LABEL_MAINCATEGORY;
        if (isAdd) {
            mainCategories.add(newMainCategory);
        } else {
            mainCategories.remove(newMainCategory);
        }
        setChanged();
        notifyObservers();
    }

    public void setSubCategory (String newSubCategory, boolean isAdd) {
        label = ModelUtils.LABEL_SUBCATEGORY;
        if (isAdd) {
            subCategories.add(newSubCategory);
        } else {
            subCategories.remove(newSubCategory);
        }
        setChanged();
        notifyObservers();
    }

    public void setAttribute (String newAttribute, boolean isAdd) {
        label = ModelUtils.LABEL_ATTRIBUTES;
        if (isAdd) {
            attributes.add(newAttribute);
        } else {
            attributes.remove(newAttribute);
        }
        setChanged();
        notifyObservers();
    }

    public void setLocation(String location) {
        label = ModelUtils.LABEL_TIME;
        this.location = location;
        this.city = location.split(",")[0].trim();
        this.state = location.split(",")[1].trim();
        setChanged();
        notifyObservers();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        label = ModelUtils.LABEL_TIME;
        this.day = day;
        setChanged();
        notifyObservers();
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        label = ModelUtils.LABEL_TIME;
        this.toTime = toTime;
        setChanged();
        notifyObservers();
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        label = ModelUtils.LABEL_TIME;
        this.fromTime = fromTime;
        setChanged();
        notifyObservers();
    }

    public String getState() {
        return state;
    }

    public String getLabel() {
        return label;
    }

    public void clearList(ArrayList<String> list) {
        list.clear();
    }
}
