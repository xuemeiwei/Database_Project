package com.xuemeiwei.view;

import com.xuemeiwei.common.SqlUtils;
import com.xuemeiwei.model.Yelp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReviewController{
    private Yelp yelp;

    @FXML
    Button reviewSearchButton;

    @FXML
    VBox reviewResultsView;

    public void setYelp(Yelp yelp) {
        this.yelp = yelp;
    }

    public void initialize() {
        reviewSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reviewResultsView.getChildren().clear();
                String business = yelp.getBusiness();
                if (business == "") {
                    return;
                }
                String bid = business.split(",")[0];
                String resQuery = "SELECT R.RDATE, R.STARS, R.USERID, R.UVOTES, U.UNAME FROM REVIEW R, USERS U " +
                        "WHERE R.USERID = U.USERID AND R.BID = '" + bid + "'";
                Connection connection = SqlUtils.connection;
                try {
                    List<String> reviewList = SqlUtils.executeQuery(connection, resQuery, 5, ",");
                    for (String review: reviewList) {
                        CheckBox item = new CheckBox(review);
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                            }
                        });
                        item.setPadding(new Insets(0, 0, 6, 5));
                        reviewResultsView.getChildren().add(item);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
