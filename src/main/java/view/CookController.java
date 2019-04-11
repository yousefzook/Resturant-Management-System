package view;

import controller.ManagerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CookController {
    private ManagerController managerController;
    private Stage primaryStage;

    public CookController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        managerController = new ManagerController();
    }

    public CookController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerCook.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
