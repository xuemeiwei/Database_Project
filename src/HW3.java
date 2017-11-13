
import com.xuemeiwei.common.SqlUtils;
import com.xuemeiwei.model.Yelp;
import com.xuemeiwei.view.BusinessController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HW3 extends Application{
    private Stage primaryStage;
    private GridPane rootPane;
    private Yelp yelp = new Yelp();

    @Override
    public void start (Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Yelp");

        initRootLayout();
        showMainCategoryView();
    }

    private void initRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HW3.class.getResource("com/xuemeiwei/view/RootLayout.fxml"));
        rootPane = (GridPane) loader.load();
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainCategoryView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HW3.class.getResource("com/xuemeiwei/view/YelpSearchLayout.fxml"));
        AnchorPane layoutPane = (AnchorPane) loader.load();
        rootPane.add(layoutPane, 0, 0);
        BusinessController controller = loader.getController();
        controller.setYelp(yelp);
        yelp.addObserver(controller);

    }

    public static void main (String[] args) {
        SqlUtils.loadJDBC();
        launch(args);
    }
}
