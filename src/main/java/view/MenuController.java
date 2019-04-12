package view;

import controller.ManagerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

public class MenuController {
    @Setter
    private ManagerController managerController;
    private Stage primaryStage;

    public MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MenuController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenu.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
