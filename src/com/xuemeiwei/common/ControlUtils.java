package com.xuemeiwei.common;

import com.xuemeiwei.model.Yelp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ControlUtils {

    public static Map<String, String> fromQueryMap = new HashMap<>();
    public static Map<String, String> whereQueryMap = new HashMap<>();
    public static String resQuery = "";

    public static List<String> getSubcategoryList (Yelp yelp, String logicConnection) throws SQLException {
        List<String> mainCategories = yelp.getMainCategories();
        int length = mainCategories.size();
        if (length == 0) {
            return new ArrayList<>();
        }

        fromQueryMap.put(ModelUtils.LABEL_MAINCATEGORY,
                ", (SELECT BID, (LISTAGG(MAINCG, ',') WITHIN GROUP (ORDER BY MAINCG)) AS MAINCGS " +
                        "FROM MAINCATEGORY GROUP BY BID) BMG");
        whereQueryMap.put(ModelUtils.LABEL_MAINCATEGORY,
                ControlUtils.logicSelectionJoint("BMG.MAINCGS", logicConnection, mainCategories));
        Connection connection = SqlUtils.connection;
        resQuery = "SELECT DISTINCT SUBCG FROM SUBCATEGORY S, BUSINESS B"
                + fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY)
                + " WHERE B.BID = S.BID AND S.BID = BMG.BID AND "
                + whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY);
        List<String> subcategoryResults = SqlUtils.executeQuery(connection, resQuery, 1);
        return subcategoryResults;
    }

    public static List<String> getAttributesList (Yelp yelp, String logicConnection) throws SQLException {
        List<String> subCategories = yelp.getSubCategories();
        if (subCategories.size() == 0) {
            return new ArrayList<>();
        }
        fromQueryMap.put(ModelUtils.LABEL_SUBCATEGORY,
                ", (SELECT BID, (LISTAGG(SUBCG, ',') WITHIN GROUP(ORDER BY SUBCG)) AS SUBCGS FROM SUBCATEGORY GROUP BY BID) BSG");
        whereQueryMap.put(ModelUtils.LABEL_SUBCATEGORY,
                ControlUtils.logicSelectionJoint("BSG.SUBCGS", logicConnection, subCategories));

        Connection connection = SqlUtils.connection;
        resQuery = "SELECT DISTINCT ATTR FROM ATTRS A, BUSINESS B" +
                fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) +
                " WHERE B.BID = A.BID AND BMG.BID = B.BID AND BSG.BID = B.BID AND " +
                whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY);
        List<String> attributesResults = SqlUtils.executeQuery(connection, resQuery, 1);
        return attributesResults;
    }

    public static List<String> getLocationList (Yelp yelp, String logicConnection) throws SQLException {
        List<String> attributes = yelp.getAttributes();
        if (attributes.size() == 0) {
            return new ArrayList<>();
        }
        fromQueryMap.put(ModelUtils.LABEL_ATTRIBUTES,
                ", (SELECT BID, (LISTAGG(ATTR, ',') WITHIN GROUP (ORDER BY ATTR)) AS ATTRIBUTES FROM ATTRS GROUP BY BID) BA");
        whereQueryMap.put(ModelUtils.LABEL_ATTRIBUTES,
                ControlUtils.logicSelectionJoint("BA.ATTRIBUTES", logicConnection, attributes));

        Connection connection = SqlUtils.connection;
        resQuery = "SELECT DISTINCT CITY FROM BUSINESS B" +
                fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_ATTRIBUTES) +
                " WHERE B.BID = BMG.BID AND B.BID = BSG.BID AND B.BID = BA.BID AND " +
                whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_ATTRIBUTES);
        List<String> citiesResults = SqlUtils.executeQuery(connection, resQuery, 1);
        return citiesResults;
    }

    public static String logicSelectionJoint(String name, String logicSelection, List<String> list) {
        StringBuilder innerQuery = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            String item = list.get(i);
            innerQuery.append(name);
            innerQuery.append(" LIKE ");
            innerQuery.append("'%" + item + "%'");
            if (i < list.size() - 1) {
                innerQuery.append(" " + logicSelection + " ");
            }
        }
        String res = String.format("(%s)", innerQuery);
        return res;
    }
}
