package com.xuemeiwei.common;

import com.xuemeiwei.model.Yelp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ControlUtils {

    public static String resQuery = "";
    public static String cityQuery = "";
    public static String stateQuery = "";
    public static String dayQuery = "";
    public static String fromTimeQuery = "";
    public static String toTimeQuery = "";

    public static Map<String, String> fromQueryMap = new HashMap<>();
    public static Map<String, String> whereQueryMap = new HashMap<>();

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
                ControlUtils.joinLogic("BMG.MAINCGS", logicConnection, mainCategories));
        Connection connection = SqlUtils.connection;
        resQuery = "SELECT DISTINCT SUBCG FROM SUBCATEGORY S, BUSINESS B"
                + fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY)
                + " WHERE B.BID = S.BID AND S.BID = BMG.BID AND "
                + whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY);
        List<String> subcategoryResults = SqlUtils.executeQuery(connection, resQuery, 1, " ");
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
                ControlUtils.joinLogic("BSG.SUBCGS", logicConnection, subCategories));

        Connection connection = SqlUtils.connection;
        resQuery = "SELECT DISTINCT ATTR FROM ATTRS A, BUSINESS B" +
                fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) +
                " WHERE B.BID = A.BID AND BMG.BID = B.BID AND BSG.BID = B.BID AND " +
                whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY);
        List<String> attributesResults = SqlUtils.executeQuery(connection, resQuery, 1, " ");
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
                ControlUtils.joinLogic("BA.ATTRIBUTES", logicConnection, attributes));

        Connection connection = SqlUtils.connection;
        resQuery = "SELECT DISTINCT CITY, STATE FROM BUSINESS B" +
                fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_ATTRIBUTES) +
                " WHERE B.BID = BMG.BID AND B.BID = BSG.BID AND B.BID = BA.BID AND " +
                whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_ATTRIBUTES);
        List<String> citiesResults = SqlUtils.executeQuery(connection, resQuery, 2, ", ");
        return citiesResults;
    }

    public static List<String> getTimeList (Yelp yelp) throws SQLException {
        List<String> attributes = yelp.getAttributes();
        if (attributes.size() == 0) {
            return new ArrayList<>();
        }
        resQuery = "SELECT DISTINCT H.DAYS, H.FROM_TIME, H.TO_TIME FROM HOURS H, BUSINESS B" +
        fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
        fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) +
        fromQueryMap.get(ModelUtils.LABEL_ATTRIBUTES) +
        " WHERE H.BID = B.BID AND H.BID = BMG.BID AND H.BID = BSG.BID AND B.BID = BA.BID AND " +
        whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + " AND " +
        whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) + " AND " +
        whereQueryMap.get(ModelUtils.LABEL_ATTRIBUTES);
        Connection connection = SqlUtils.connection;
        List<String> timeList = SqlUtils.executeQuery(connection, resQuery, 3, ",");
        return timeList;
    }

    public static String joinLogic(String name, String logicSelection, List<String> list) {
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

    public static List<String> getBusinessList(Yelp yelp) throws SQLException {
        String day = yelp.getDay();
        String fromTime = yelp.getFromTime();
        String toTime = yelp.getToTime();
        String city = yelp.getCity();
        String state = yelp.getState();
        if (day.equals("") || fromTime.equals("") || toTime.equals("") || city.equals("") || state.equals("")) {
            return new ArrayList<>();
        }

        cityQuery = " B.CITY = '" + city + "'";
        stateQuery = " B.STATE = '" + state + "'";
        dayQuery = " H.DAYS = '" + day + "'";
        fromTimeQuery = " H.FROM_TIME = '" + fromTime + "'";
        toTimeQuery = " H.TO_TIME = '" + toTime + "'";

//        resQuery = "SELECT DISTINCT B.BID, B.ADDRESS, B.CITY, B.STATE, B.STARS, BR.R_CNT, CH.CIN FROM BUSINESS B, CHECKIN CH, HOURS H" +
//                fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
//                fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) +
//                fromQueryMap.get(ModelUtils.LABEL_ATTRIBUTES) +
//                ", (SELECT BID, COUNT(*) AS R_CNT FROM REVIEW GROUP BY BID) BR" +
//                " WHERE B.BID = CH.BID AND H.BID = B.BID AND H.BID = BMG.BID AND H.BID = BSG.BID AND B.BID = BA.BID AND B.BID = BR.BID" +
//                " AND " +
//                whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + " AND " +
//                whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) + " AND " +
//                whereQueryMap.get(ModelUtils.LABEL_ATTRIBUTES) +
//                " AND " + cityQuery + " AND " + stateQuery
//                + " AND " + dayQuery + " AND " + fromTimeQuery + " AND " + toTimeQuery;

        resQuery = "SELECT DISTINCT B.BID, B.ADDRESS, B.CITY, B.STATE, B.STARS, B.REVIEW_CNT, CH.CIN FROM BUSINESS B, CHECKIN CH, HOURS H" +
                fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) +
                fromQueryMap.get(ModelUtils.LABEL_ATTRIBUTES) +
                " WHERE B.BID = CH.BID AND H.BID = B.BID AND H.BID = BMG.BID AND H.BID = BSG.BID AND B.BID = BA.BID " +
                " AND " +
                whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY) + " AND " +
                whereQueryMap.get(ModelUtils.LABEL_ATTRIBUTES) +
                " AND " + cityQuery + " AND " + stateQuery
                + " AND " + dayQuery + " AND " + fromTimeQuery + " AND " + toTimeQuery;

        Connection connection = SqlUtils.connection;
        List<String> businessList = SqlUtils.executeQuery(connection, resQuery, 7, ",");
        return businessList;
    }

}
