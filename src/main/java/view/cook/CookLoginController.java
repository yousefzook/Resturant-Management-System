package view.cook;

import controller.CookController;
import controller.CustomerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
public class CookLoginController implements Initializable{

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private CookController cookController;

    @FXML
    ScrollPane scPane;

    @FXML
    Button enter;

    @FXML
    private MenuButton menu;

    int CookID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO GET IDS FROM DATA BASE AND SHOW THEM IN MENU ITEM
        CookID = -1;
        for(int i = 0 ;i < 5 ; i++){
            MenuItem item = new MenuItem("helloooo "+ i);
            menu.getItems().add(item);
            int x = i;
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CookID = x;
                    menu.setText(String.valueOf(x));
                    System.out.println(x);
                }
            });

        }

    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setResizable(true);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public void enter() throws IOException {

        if(CookID == -1)
        {
            showError("Please select you ID");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CookOrders.fxml"));
        loader.setControllerFactory(appContext::getBean);

        Parent p = loader.load();

        Scene scene = new Scene(p);
        scene.getStylesheets().add("/css/menu.css");

        CookOrdersController controller = loader.getController();
        controller.setCookID(CookID);

        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
    }


}
