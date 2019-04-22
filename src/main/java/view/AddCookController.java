package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;
import model.actionresults.EmptyResponse;
import model.entity.Cook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AddCookController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;

    @FXML
    private TextField firstText, lastText;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public AddCookController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public AddCookController() {
    }


    public void save() throws IOException {
        if (!isValid()) {
            showError("Name should only contain letters and spaces");
        } else {
            Cook cook = new Cook(null, firstText.getText(), lastText.getText(), true);
            EmptyResponse r = managerController.addCook(cook);
            if (!r.isSuccess()) {
                showError(r.getMessage());
            }
            goBack();
        }
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private boolean isValid() {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]+$");
        Matcher matcherF = pattern.matcher(firstText.getText().trim());
        Matcher matcherL = pattern.matcher(lastText.getText().trim());
        return matcherF.matches() && matcherL.matches();
    }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerView.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
    }
}
