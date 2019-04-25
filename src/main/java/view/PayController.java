package view;


import controller.ManagerController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import model.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Component
public class PayController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;

    private Map<Dish, Integer> order;

    @FXML
    public TextField cardNo;

    @FXML
    public TextField csv;

    @FXML
    public Button confirm;

    private final int csvLimit = 3;

    private final int cardNoLimit = 19;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setView(Map<Dish, Integer> order, Stage stage) {
        this.primaryStage = stage;
        this.order = order;

        csv.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (csv.getText().length() >= csvLimit) {
                        csv.setText(csv.getText().substring(0, csvLimit));
                    }
                }
            }
        });

        cardNo.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (cardNo.getText().length() >= cardNoLimit) {
                        cardNo.setText(cardNo.getText().substring(0, cardNoLimit));
                    }
                }
            }
        });
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setResizable(true);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public void backToOrder() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Order.fxml"));
        loader.setControllerFactory(appContext::getBean);

        Parent p = loader.load();

        Scene scene = new Scene(p);
        scene.getStylesheets().add("css/order.css");


        OrderController controller = loader.getController();
        controller.setView(order, primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void confirmPayment() throws IOException {


        if (csv.getText().trim().length() < csvLimit|| cardNo.getText().trim().length() < cardNoLimit) {
            showError("You Must Enter valid Payment Details !");
            return;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Boolean matches = pattern.matcher(csv.getText().trim()).matches();
        Boolean matches2 = pattern.matcher(cardNo.getText().trim()).matches();

        if (!matches || !matches2) {
            showError("Invalid Payment Details !");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Rate.fxml"));
        loader.setControllerFactory(appContext::getBean);

        Parent p = loader.load();

        Scene scene = new Scene(p);
        scene.getStylesheets().add("css/rate.css");


        RateController controller = loader.getController();
        controller.setView(order, primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}


