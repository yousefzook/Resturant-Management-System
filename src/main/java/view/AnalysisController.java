package view;

import controller.ManagerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AnalysisController implements Initializable {
    private ManagerController managerController;
    private Stage primaryStage;

    @FXML
    private Text incomeToday, incomeMonth, cookTop, dishTop;

    public AnalysisController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        managerController = new ManagerController();
        System.out.println(primaryStage.toString());
    }

    public AnalysisController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerAnalysis.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        // fill analysis texts from DB
    }

    public void goBack(MouseEvent mouseEvent) throws IOException {
        Node node = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) node.getScene().getWindow();
        ViewController c = new ViewController(stage);
        c.showUp();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
}
