package view.cook;

import controller.CookController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import model.actionresults.CookResponse;
import model.entity.Cook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class CookLoginController implements Initializable {

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

    private Cook signedCook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO GET IDS FROM DATA BASE AND SHOW THEM IN MENU ITEM
        signedCook = null;
        CookResponse r = cookController.getCooks();
        if (!r.isSuccess())
            showError(r.getMessage());
        else {
            List<Cook> cook = r.getCooks();
            for (Cook c : cook) {
                MenuItem item = new MenuItem(c.getId() + ": " + c.getFirstName() + " " + c.getLastName());
                menu.getItems().add(item);
                item.setOnAction(event -> {
                    signedCook = c;
                    menu.setText(c.getId() + ": " + c.getFirstName() + " " + c.getLastName());
                });

            }
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

        if (signedCook == null) {
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
        controller.setCook(signedCook);

        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
    }


}
