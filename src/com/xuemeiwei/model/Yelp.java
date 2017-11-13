package com.xuemeiwei.model;

import com.xuemeiwei.common.ModelUtils;

import java.util.ArrayList;
import java.util.Observable;

public class Yelp extends Observable{


    private ArrayList<String> mainCategories;
    private ArrayList<String> subCategories;
    private ArrayList<String> attributes;
    private ArrayList<String> locations;
    private ArrayList<String> states;
    private String location;
    private String label = "";

    public Yelp() {
        mainCategories = new ArrayList<>();
        subCategories = new ArrayList<>();
        attributes = new ArrayList<>();
        locations = new ArrayList<>();
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
        label = ModelUtils.LABEL_ADDRESS;
        this.location = location;
        setChanged();
        notifyObservers();
    }

    public String getLabel() {
        return label;
    }

    public void clearList(ArrayList<String> list) {
        list.clear();
    }
}
