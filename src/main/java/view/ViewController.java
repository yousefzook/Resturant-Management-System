package view;

import controller.ManagerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class ViewController {
    private ManagerController managerController;
    private Stage primaryStage;

    public ViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        managerController = new ManagerController();
    }

    public ViewController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerView.fxml"));

        primaryStage.setScene(new Scene(root));
        System.out.println(primaryStage.toString());
        primaryStage.show();
    }

    @FXML
    public void showAnalysis(MouseEvent mouseEvent) throws IOException {
        Node node = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) node.getScene().getWindow();
        AnalysisController c = new AnalysisController(stage);
        c.showUp();
    }

    @FXML
    public void showEmployees(MouseEvent mouseEvent) throws IOException {
        Node node = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) node.getScene().getWindow();
        CookController c = new CookController(stage);
        c.showUp();
    }

    @FXML
    public void showMenu(MouseEvent mouseEvent) throws IOException {
        Node node = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) node.getScene().getWindow();
        CookController c = new CookController(stage);
        c.showUp();
    }
}
