package view;

import controller.ManagerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private ManagerController managerController;
    private Stage primaryStage;

    public MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        managerController = new ManagerController();
    }

    public MenuController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenu.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
