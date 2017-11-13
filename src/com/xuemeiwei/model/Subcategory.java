package com.xuemeiwei.model;

public class Subcategory {
    String business_id;
    String subName;
    public Subcategory (String business_id, String subName) {
        this.business_id = business_id;
        this.subName = subName;
    }
    public String getBusiness_id() {
        return business_id;
    }
    public String getSubName() {
        return subName;
    }
}
