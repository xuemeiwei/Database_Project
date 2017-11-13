package com.xuemeiwei.model;

public class MainCategory {
    String business_id;
    String mainName;
    public MainCategory (String business_id, String mainName) {
        this.business_id = business_id;
        this.mainName = mainName;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public String getMainName() {
        return mainName;
    }
}
