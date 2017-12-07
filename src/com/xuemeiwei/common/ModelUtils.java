package com.xuemeiwei.common;

import java.util.HashMap;
import java.util.Map;

public class ModelUtils {
    public static final String LABEL_MAINCATEGORY = "mainCategory";
    public static final String LABEL_SUBCATEGORY = "subCategory";
    public static final String LABEL_ATTRIBUTES = "attributes";
    public static final String LABEL_TIME = "time";

    private static Map<String, String> logicConnections = new HashMap<>();

    public static void setLogicConnections (String key, String logicConnection) {
        logicConnections.put(key, logicConnection);
    }
}
