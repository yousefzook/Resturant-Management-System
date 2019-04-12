package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Cook;
import model.actionresults.EmptyResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCookController implements Initializable {

    private ManagerController managerController;
    private Stage primaryStage;

    @FXML
    private TextField firstText, lastText;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public AddCookController(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
        if(!isValid()) {
           showError("Name should only contain letters and spaces");
        } else {
            Cook cook = new Cook(null, firstText.getText(), lastText.getText());
            EmptyResponse r = ManagerController.getInstance().addCook(cook);
            if(!r.isSuccess()) {
                showError(r.getMessage());
            }
            backToMenu(mouseEvent);
        }
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private boolean isValid() {
        Pattern pattern = Pattern.compile(new String ("^[a-zA-Z\\s]+$"));
        Matcher matcherF = pattern.matcher(firstText.getText().trim());
        Matcher matcherL = pattern.matcher(lastText.getText().trim());
        if(! matcherF.matches() || !matcherL.matches()) {
            return false;
        }
        return true;
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
