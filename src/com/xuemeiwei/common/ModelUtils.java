package com.xuemeiwei.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelUtils {
    public static final String LABEL_MAINCATEGORY = "mainCategory";
    public static final String LABEL_SUBCATEGORY = "subCategory";
    public static final String LABEL_ATTRIBUTES = "attributes";
    public static final String LABEL_ADDRESS = "address";
    public static final String LABEL_RESULT = "result";

    private static List<String> selectedRes = new ArrayList<>();
    private static Map<String, String> logicConnections = new HashMap<>();

    public static void setSelectedRes(String item, boolean isAdd) {
        if (isAdd) {
            selectedRes.add(item);
        } else {
            selectedRes.remove(item);
        }
    }

    public static List<String> getSelectedRes() {
        return selectedRes;
    }

    public static void clearSelectedRes() {
        selectedRes.clear();
    }

    public static void setLogicConnections (String key, String logicConnection) {
        logicConnections.put(key, logicConnection);
    }

    public static String getLogicConnection (String key) {
        return logicConnections.get(key);
    }
}
