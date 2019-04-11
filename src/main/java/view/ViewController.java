package view;

import controller.ManagerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewController {
    private ManagerController managerController;
    private Stage primaryStage;

    public ViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        managerController = new ManagerController();
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerView.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
