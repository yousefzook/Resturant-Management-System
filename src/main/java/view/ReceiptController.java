package view;


import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class ReceiptController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;

    private Map<HBox, Integer> map = new HashMap<>();

    @FXML
    ScrollPane scPane;


    public ReceiptController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ReceiptController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Receipt.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/receipt.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
