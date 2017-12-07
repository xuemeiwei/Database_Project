package com.xuemeiwei.view;

import com.sun.tools.classfile.Opcode;
import com.xuemeiwei.common.ControlUtils;
import com.xuemeiwei.common.ModelUtils;
import com.xuemeiwei.common.SqlUtils;
import com.xuemeiwei.model.BusinessSearchResult;
import com.xuemeiwei.model.Yelp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static com.xuemeiwei.common.ControlUtils.*;

public class BusinessController implements Observer{

    @FXML
    ComboBox daysOfWeekSelection, fromTimeSelection, toTimeSelection,
            andOrSelection, locationSelection;

    @FXML
    VBox mainCategoryView, subCategoryView, attributeView;

    @FXML
    VBox businessResultView;

    @FXML
    Button searchButton;

    private Yelp yelp;
    public void setYelp (Yelp yelp) {
        this.yelp = yelp;
    }

    public void initialize() {
        andOrSelection.getItems().addAll("AND", "OR");
        andOrSelection.setValue("AND");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                businessResultView.getChildren().clear();
                String fromQuery = "";
                String whereQuery = "";
                String subcatogory = ControlUtils.fromQueryMap.get(ModelUtils.LABEL_SUBCATEGORY);
                if (subcatogory != null && subcatogory != "") {
                    fromQuery += subcatogory;
                    whereQuery += " AND " + ControlUtils.whereQueryMap.get(ModelUtils.LABEL_SUBCATEGORY)
                            + " AND B.BID = BSG.BID ";
                }
                String attribute = ControlUtils.fromQueryMap.get(ModelUtils.LABEL_ATTRIBUTES);
                if (attribute != null && attribute != "") {
                    fromQuery += attribute;
                    whereQuery += " AND " + ControlUtils.whereQueryMap.get(ModelUtils.LABEL_ATTRIBUTES)
                            + " AND B.BID = BA.BID";
                }
                if (cityQuery != "") {
                    whereQuery += " AND " + cityQuery;
                }
                if (stateQuery != "") {
                    whereQuery += " AND " + stateQuery;
                }
                if (fromTimeQuery != "") {
                    whereQuery += " AND " + fromTimeQuery;
                }
                if (toTimeQuery != "") {
                    whereQuery += " AND " + toTimeQuery;
                }
                if (dayQuery != "") {
                    whereQuery += " AND " + dayQuery;
                }
//                String resQuery = "SELECT DISTINCT B.BID, B.ADDRESS, B.CITY, B.STATE, B.STARS, BR.R_CNT, CH.CIN FROM BUSINESS B, CHECKIN CH, HOURS H" +
//                        ", (SELECT BID, COUNT(*) AS R_CNT FROM REVIEW GROUP BY BID) BR" + fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
//                        fromQuery + " WHERE B.BID = CH.BID AND H.BID = B.BID AND H.BID = BMG.BID AND B.BID = BR.BID " + " AND "
//                        + whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + whereQuery;
                String resQuery = "SELECT DISTINCT B.BID, B.ADDRESS, B.CITY, B.STATE, B.STARS, B.REVIEW_CNT, CH.CIN FROM BUSINESS B, CHECKIN CH, HOURS H" +
                         fromQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) +
                        fromQuery + " WHERE B.BID = CH.BID AND H.BID = B.BID AND H.BID = BMG.BID " + " AND "
                        + whereQueryMap.get(ModelUtils.LABEL_MAINCATEGORY) + whereQuery;

                Connection connection = SqlUtils.connection;
                try {
                    List<String> businessList = SqlUtils.executeQuery(connection, resQuery, 7, ",");
                    for (String business: businessList) {
                        CheckBox item = new CheckBox(business);
                        item.getStyleClass().add("big-check-box");
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                boolean isSelected = item.isSelected();
                                yelp.setBusiness(business);
                            }
                        });
                        item.setPadding(new Insets(0, 0, 6, 5));
                        businessResultView.getChildren().add(item);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        initializeMainCategoryView();
    }

    @Override
    public void update(java.util.Observable observable, Object arg) {
        Yelp yelp = (Yelp) observable;
        String label = yelp.getLabel();

        switch (label) {
            case ModelUtils.LABEL_MAINCATEGORY:
                subCategoryView.getChildren().clear();

                String logicSelection = andOrSelection.getValue().toString();
                ModelUtils.setLogicConnections(label, logicSelection);
                try {
                    List<String> subcategories = ControlUtils.getSubcategoryList(yelp, logicSelection);
                    Collections.sort(subcategories);
                    for (String subcategory: subcategories) {
                        CheckBox item = new CheckBox(subcategory);
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                boolean isSelected = item.isSelected();
                                yelp.setSubCategory(subcategory, isSelected);
                                yelp.clearList(yelp.getAttributes());
                            }
                        });
                        item.setPadding(new Insets(0, 0, 3, 5));
                        subCategoryView.getChildren().add(item);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case ModelUtils.LABEL_SUBCATEGORY:
                attributeView.getChildren().clear();
                logicSelection = andOrSelection.getValue().toString();
                ModelUtils.setLogicConnections(label, logicSelection);
                try {
                    List<String> attributes = ControlUtils.getAttributesList(yelp, logicSelection);
                    Collections.sort(attributes);
                    for (String attribute: attributes) {
                        CheckBox item = new CheckBox(attribute);
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                boolean isSelected = item.isSelected();
                                yelp.setAttribute(attribute, isSelected);
                                yelp.clearList(yelp.getLocationList());
                            }
                        });
                        item.setPadding(new Insets(0, 0, 3, 5));
                        attributeView.getChildren().add(item);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case ModelUtils.LABEL_ATTRIBUTES:
                locationSelection.getItems().clear();
                logicSelection = andOrSelection.getValue().toString();
                ModelUtils.setLogicConnections(label, logicSelection);

                daysOfWeekSelection.getItems().clear();
                fromTimeSelection.getItems().clear();
                toTimeSelection.getItems().clear();

                try {
                    List<String> locationLists = ControlUtils.getLocationList(yelp, logicSelection);
                    Collections.sort(locationLists);
                    locationSelection.getItems().addAll(locationLists);
                    locationSelection.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String newLocation = (String) locationSelection.getSelectionModel().getSelectedItem();
                            locationSelection.setValue(newLocation);
                            yelp.setLocation(newLocation);
                        }
                    });

                    List<String> timeList = ControlUtils.getTimeList(yelp);
                    Set<String> daysList = new HashSet<>();
                    Set<String> fromTimeList = new HashSet<>();
                    Set<String> toTimeList = new HashSet<>();
                    for (String time: timeList) {
                        String[] timeArr = time.split(",");
                        daysList.add(timeArr[0]);
                        fromTimeList.add(timeArr[1]);
                        toTimeList.add(timeArr[2]);
                    }
                    daysOfWeekSelection.getItems().addAll(daysList);
                    daysOfWeekSelection.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String newDay = (String) daysOfWeekSelection.getSelectionModel().getSelectedItem();
                            daysOfWeekSelection.setValue(newDay);
                            yelp.setDay(newDay);
                        }
                    });

                    fromTimeSelection.getItems().addAll(fromTimeList);
                    fromTimeSelection.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String newFromTime = (String) fromTimeSelection.getSelectionModel().getSelectedItem();
                            fromTimeSelection.setValue(newFromTime);
                            yelp.setFromTime(newFromTime);
                        }
                    });

                    toTimeSelection.getItems().addAll(toTimeList);
                    toTimeSelection.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String newToTime = (String) toTimeSelection.getSelectionModel().getSelectedItem();
                            toTimeSelection.setValue(newToTime);
                            yelp.setToTime(newToTime);
                        }
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case ModelUtils.LABEL_TIME:
                businessResultView.getChildren().clear();
                try {
                    List<String> businessResults = ControlUtils.getBusinessList(yelp);
                    for (String business: businessResults) {
                        CheckBox item = new CheckBox(business);
                        item.getStyleClass().add("big-check-box");
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                boolean isSelected = item.isSelected();
                                yelp.setBusiness(business);
                            }
                        });
                        item.setPadding(new Insets(0, 0, 6, 5));
                        businessResultView.getChildren().add(item);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    public void initializeMainCategoryView() {
        List<String> mainCategoryResults = new ArrayList<>(SqlUtils.MainCategories);
        Collections.sort(mainCategoryResults);
        int len = mainCategoryResults.size();
        for (int i = 0; i < len; ++i) {
            String mainCategory = mainCategoryResults.get(i);
            CheckBox item = new CheckBox(mainCategory);

            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    boolean isSelected = item.isSelected();
                    yelp.setMainCategory(mainCategory, isSelected);
                    yelp.clearList(yelp.getSubCategories());
                }
            });

            item.setPadding(new Insets(0, 0, 3, 5));
            mainCategoryView.getChildren().add(item);
        }
    }
}
