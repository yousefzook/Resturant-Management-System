package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Cook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCookController implements Initializable {

    private ManagerController managerController;
    private Stage primaryStage;

    @FXML
    private TextField firstText, lastText;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public AddCookController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        managerController = ManagerController.getInstance();
    }

    public AddCookController() {

    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/hire.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("addDish.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void save(MouseEvent mouseEvent) throws IOException {
        // save data to dataBase
        Cook cook = new Cook(null, firstText.getText(), lastText.getText());
        backToMenu(mouseEvent);
    }

    private void backToMenu(MouseEvent mouseEvent) throws IOException {
        Node node = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) node.getScene().getWindow();
        CookController c = new CookController(stage);
        c.showUp();
    }

    @FXML
    public void back(MouseEvent mouseEvent) throws IOException {
        backToMenu(mouseEvent);
    }
}
