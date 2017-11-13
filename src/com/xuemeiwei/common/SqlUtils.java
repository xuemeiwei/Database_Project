package com.xuemeiwei.common;

import com.xuemeiwei.model.MainCategory;
import com.xuemeiwei.model.Subcategory;
import org.json.*;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class SqlUtils {

    public static Connection connection = null;
    public static HashMap<String, String> pathTable = new HashMap<>();

    static {
        pathTable.put("yelp_business.json", "Business");
        pathTable.put("yelp_checkin.json", "Checkin");
        pathTable.put("yelp_review.json", "Review");
        pathTable.put("yelp_user.json", "Users");
    }

    public static Set<String> MainCategories = new HashSet<>(Arrays.asList(
            "Active Life", "Arts & Entertainment", "Automotive", "Car Rental", "Cafes",
            "Beauty & Spas", "Convenience Stores", "Dentists", "Doctors", "Drugstores",
            "Department Stores", "Education", "Event Planning & Services", "Flowers & Gifts",
            "Food", "Health & Medical", "Home Services", "Home & Garden", "Hospitals",
            "Hotels & Travel", "Hardware Stores", "Grocery", "Medical Centers",
            "Nurseries & Gardening", "Nightlife", "Restaurants", "Shopping", "Transportation"));

    public static void businessInsertHelper(File file, Connection connection)
            throws IOException, JSONException, SQLException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            JSONObject obj = new JSONObject(line);
            insertIntoBusiness(obj, connection);
            insertIntoCategory(obj, connection);
            insertIntoAttribute(obj, connection);
        }
        bufferedReader.close();
        fileReader.close();
    }

    private static void insertIntoBusiness (JSONObject obj, Connection connection)
            throws JSONException, SQLException {
        String business_id = obj.getString("business_id");
        String city = obj.getString("city");
        String state = obj.getString("state");
        String name = obj.getString("name");
        Double stars = obj.getDouble("stars");

        String query = "INSERT INTO BUSINESS VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, business_id);
        statement.setString(2, city);
        statement.setString(3, state);
        statement.setString(4, name);
        statement.setDouble(5, stars);
        statement.executeUpdate();
        statement.close();
    }

    private static void insertIntoCategory (JSONObject obj, Connection connection)
            throws JSONException, SQLException {
        JSONArray categories = obj.getJSONArray("categories");
        String business_id = obj.getString("business_id");

        List<MainCategory> mainCategoryList = new ArrayList<>();
        List<Subcategory> subcategoryList = new ArrayList<>();

        for (int i = 0; i < categories.length(); ++i) {
            String category = categories.getString(i);
            if (MainCategories.contains(category)) {
                mainCategoryList.add(new MainCategory(business_id, category));
            } else {
                subcategoryList.add(new Subcategory(business_id, category));
            }
        }
        String query = "INSERT INTO MAINCATEGORY VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        for (MainCategory mainCategory: mainCategoryList) {
            statement.setString(1, mainCategory.getBusiness_id());
            statement.setString(2, mainCategory.getMainName());
            statement.executeUpdate();
        }
        statement.close();

        query = "INSERT INTO SUBCATEGORY VALUES (?, ?)";
        statement = connection.prepareStatement(query);
        for (Subcategory subcategory: subcategoryList) {
            statement.setString(1, subcategory.getBusiness_id());
            statement.setString(2, subcategory.getSubName());
            statement.executeUpdate();
        }
        statement.close();
    }

    private static void insertIntoAttribute (JSONObject obj, Connection connection)
            throws JSONException, SQLException {
        JSONObject attributes = obj.getJSONObject("attributes");
        String business_id = obj.getString("business_id");

        String query = "INSERT INTO ATTRS VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Iterator<?> keys = attributes.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            StringBuilder sb = new StringBuilder();
            if (attributes.get(key) instanceof JSONObject) {
                JSONObject subAttributes = attributes.getJSONObject(key);
                Iterator<?> subKeys = subAttributes.keys();
                while (subKeys.hasNext()) {
                    String subKey = (String) subKeys.next();
                    StringBuilder subSb = new StringBuilder();
                    subSb.append(subKey).append("_").append(subAttributes.get(subKey));
                    statement.setString(1, business_id);
                    statement.setString(2, subSb.toString());
                    statement.executeUpdate();
                }
            } else {
                sb.append(key).append("_").append(attributes.get(key));
                statement.setString(1, business_id);
                statement.setString(2, sb.toString());
                statement.executeUpdate();
            }
        }
        statement.close();
    }

    public static void reviewInsertHelper(File file, Connection connection)
            throws IOException, JSONException, SQLException {
        System.out.println("Inserting review table!");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;

        String query = "INSERT INTO REVIEW VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        while ((line = bufferedReader.readLine()) != null) {
            JSONObject obj = new JSONObject(line);
            String review_id = obj.getString("review_id");
            String user_id = obj.getString("user_id");
            String text = obj.getString("text");
            Date date = java.sql.Date.valueOf(obj.getString("date"));
            String business_id = obj.getString("business_id");
            int stars = obj.getInt("stars");
            int useful_votes = obj.getJSONObject("votes").getInt("useful");

            statement.setInt(1, useful_votes);
            statement.setString(2, user_id);
            statement.setString(3, review_id);
            statement.setString(4, business_id);
            statement.setInt(5, stars);
            statement.setDate(6, date);
            statement.setString(7, text);
            statement.execute();
        }
        statement.close();
        bufferedReader.close();
        fileReader.close();
    }

    public static void userInsertHelper(File file, Connection connection)
            throws IOException, SQLException, JSONException {
        System.out.println("Inserting user table!");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        String query = "INSERT INTO USERS VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        while ((line = bufferedReader.readLine()) != null) {
            JSONObject obj = new JSONObject(line);
            String user_id = obj.getString("user_id");
            String name = obj.getString("name");
            statement.setString(1, user_id);
            statement.setString(2, name);
            statement.executeUpdate();
        }
        statement.close();
        bufferedReader.close();
        fileReader.close();
    }

    public static void checkinInsertHelper(File file, Connection connection)
            throws IOException, SQLException, JSONException {
        System.out.println("Inserting checkin table!");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        String query = "INSERT INTO CHECKIN VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        while ((line = bufferedReader.readLine()) != null) {
            JSONObject obj = new JSONObject(line);
            String business_id = obj.getString("business_id");
            JSONObject checkin_infos = obj.getJSONObject("checkin_info");
            Iterator<?> keys = checkin_infos.keys();
            int cnt = 0;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                cnt += (int) checkin_infos.get(key);
            }
            statement.setString(1, business_id);
            statement.setInt(2, cnt);
            statement.executeUpdate();
        }
        statement.close();
        bufferedReader.close();
        fileReader.close();
    }

    public static void loadJDBC() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Class not found in oracle database");
        }
        connection = makeConnection("127.0.0.1", "orclcdb", 1521, "C##XWEI", "123456");
    }

    public static Connection makeConnection (String host, String serviceName, int port, String userName, String pwd) {
        String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + serviceName;
        try {
            connection = DriverManager.getConnection(oracleURL, userName, pwd);
        } catch (SQLException e) {
            e.printStackTrace();;
            System.out.println("Fail to connect with oracle database");
        }
        return connection;
    }

    public static List<String> executeQuery(Connection connection, String sql, int numOfResColumns) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<String> results = dealWithResultSets(resultSet, numOfResColumns);
        statement.close();
        return results;
    }

    private static List<String> dealWithResultSets (ResultSet resultSet, int numOfResColumns) throws SQLException {
        List<String> results = new ArrayList<>();
        while (resultSet.next()) {
            String[] tempRes = new String[numOfResColumns];
            for (int i = 0; i < numOfResColumns; ++i) {
                tempRes[i] = resultSet.getString(i + 1);
            }
            String resJoin = ToolUtils.join(ToolUtils.SEPERATOR, tempRes);
            results.add(resJoin);
        }
        return results;
    }
}
