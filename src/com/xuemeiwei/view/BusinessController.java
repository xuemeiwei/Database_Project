package com.xuemeiwei.view;

import com.xuemeiwei.common.ControlUtils;
import com.xuemeiwei.common.ModelUtils;
import com.xuemeiwei.common.SqlUtils;
import com.xuemeiwei.model.Yelp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observer;

public class BusinessController implements Observer{

    @FXML
    ComboBox daysOfWeekSelection, fromTimeSelection, toTimeSelection,
            andOrSelection, locationSelection;

    @FXML
    VBox mainCategoryView, subCategoryView, attributeView, businessResultView;

    private Yelp yelp;
    public void setYelp (Yelp yelp) {
        this.yelp = yelp;
    }

    public void initialize() {
        andOrSelection.getItems().addAll("AND", "OR");
        andOrSelection.setValue("AND");

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
                                clearAttributes();
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

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    public void clearAttributes() {

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
