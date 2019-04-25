package view;

import controller.ManagerController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import model.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class RateController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;


    private Map<Dish, Integer> order;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setView(Map<Dish, Integer> order, Stage stage) {
        this.primaryStage = stage;

        this.order = order;
    }

    public void backToMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CustomerMenu.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((AddCookController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
    }
}
