package com.xuemeiwei.data;

import com.xuemeiwei.common.SqlUtils;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class Populate {

    private static Connection connection = null;
    public static void main(String[] args) {
        Populate populate = new Populate();
        populate.loadJDBC();
        //populate.deleteOriginalData();

        for (String arg: args) {
            try {
                populate.insertData (arg);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        populate.connectionClose();
    }

    private void insertData(String filePath) throws IOException, JSONException, SQLException {
        File file = new File (filePath);
        String fileName = file.getName();
        String tableName = SqlUtils.pathTable.get(fileName);

        switch (tableName) {
            case "Business":
                SqlUtils.businessInsertHelper(file, connection);
                break;
            case "Review":
                SqlUtils.reviewInsertHelper(file, connection);
                break;
            case "Checkin":
                SqlUtils.checkinInsertHelper(file, connection);
                break;
            case "Users":
                SqlUtils.userInsertHelper(file, connection);
                break;
        }

    }

    private void connectionClose () {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteOriginalData() {
        Statement statement = createStatement();
        String getTableSql = "SELECT table_name FROM user_tables";
        ResultSet tableResultSet = null;
        try {
            tableResultSet = statement.executeQuery(getTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get all the tables");
        }
        if (tableResultSet == null) {
            return;
        }
        try {
            while (tableResultSet.next()) {
                statement = createStatement();
                if (statement != null) {
                    String tableName = tableResultSet.getString(1);
                    String deleteSql = String.format("TRUNCATE TABLE %s", tableName);
                    System.out.println(String.format("Deleting table %s ...", tableName));
                    statement.execute(deleteSql);
                    System.out.println(String.format("Deleted table %s!", tableName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Statement createStatement() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Statement created failed");
        }
        return statement;
    }

    private void loadJDBC() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Class not found in oracle database");
        }
        connection = makeConnection("127.0.0.1", "orclcdb", 1521, "C##XWEI", "123456");
    }

    private Connection makeConnection (String host, String serviceName, int port, String userName, String pwd) {
        String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + serviceName;
        try {
            connection = DriverManager.getConnection(oracleURL, userName, pwd);
        } catch (SQLException e) {
            e.printStackTrace();;
            System.out.println("Fail to connect with oracle database");
        }
        return connection;
    }
}
