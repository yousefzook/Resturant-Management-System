package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import model.actionresults.CookResponse;
import model.actionresults.EmptyResponse;
import model.entity.Cook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class CookController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;

    @FXML
    ScrollPane scPane;

    private Map<HBox, Integer> map;

    CookController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public CookController() {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        map = new HashMap<>();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 80));

        CookResponse r = managerController.getHiredCooks();
        if (!r.isSuccess())
            showError(r.getMessage());
        else {
            List<Cook> list = r.getCooks();
            for (Cook c : list) {
                HBox crnt = getItem(c);
                map.put(crnt, c.getId());
                vBox.getChildren().add(crnt);
            }
            scPane.setContent(vBox);
        }
    }

    private HBox getItem(Cook cook) {
        //name and description
        VBox vBox = new VBox();
        Label fName = new Label("First Name: " + cook.getFirstName());
        Label lName = new Label("Last Name: " + cook.getLastName());
        vBox.getChildren().addAll(fName, lName);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(15, 350, 15, 10));

        //buttons
        Button delete = new Button();
        delete.setId("delBtn");
        delete.setPadding(new Insets(150, 150, 150, 150));

        HBox employee = new HBox(vBox, delete);
        employee.setSpacing(200);
        employee.setPadding(new Insets(20, 20, 20, 20));

        addDeleteAction(delete);
        employee.setId("menuBox");

        return employee;
    }

    public void hireAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/hire.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((AddCookController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
    }

    public void backAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerView.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
    }

    private void addDeleteAction(final Button delete) {
        delete.setOnAction(actionEvent -> {
            for (Node n : delete.getParent().getParent().getChildrenUnmodifiable()) {
                if (n.equals(delete.getParent())) {
                    EmptyResponse r = managerController.fireCook(map.get(delete.getParent()));
                    if (r.isSuccess()) {
                        ((VBox) delete.getParent().getParent()).getChildren().remove(n);
                    } else {
                        showError(r.getMessage());
                    }
                    break;
                }
            }
        });
    }

    private void showError(String s) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(s);
        errorAlert.showAndWait();
    }
}
