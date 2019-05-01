package view.customer;


import controller.CustomerController;
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
import model.actionresults.EmptyResponse;
import model.entity.Dish;
import model.entity.Order;
import model.entity.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Component
public class PayController implements Initializable {

    private static final int TABLE_NUMBER = 5;
    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private CustomerController customerController;

    private Map<Dish, Integer> order;

    @FXML
    public TextField cardNo;

    @FXML
    public TextField csv;

    @FXML
    public Button confirm;

    private final int csvLimit = 3;

    private final int minCardNoLimit = 16;

    private final int maxCardNoLimit = 19;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setView(Map<Dish, Integer> order, Stage stage) {
        this.primaryStage = stage;
        this.order = order;

        csv.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (csv.getText().length() >= csvLimit) {
                    csv.setText(csv.getText().substring(0, csvLimit));
                }
            }
        });

        cardNo.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (cardNo.getText().length() >= maxCardNoLimit) {
                    cardNo.setText(cardNo.getText().substring(0, maxCardNoLimit));
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
    }

    public void confirmPayment() throws IOException {


        if (csv.getText().trim().length() < csvLimit || cardNo.getText().trim().length() < minCardNoLimit) {
            showError("You Must Enter valid Payment Details !");
            return;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        boolean matches = pattern.matcher(csv.getText().trim()).matches();
        boolean matches2 = pattern.matcher(cardNo.getText().trim()).matches();

        if (!matches || !matches2) {
            showError("Invalid Payment Details !");
            return;
        }
        Order orderObj = new Order(order);
        orderObj.setTable(new Table(TABLE_NUMBER));
        EmptyResponse response = customerController.confirmOrder(orderObj);

        if (!response.isSuccess()) {
            showError(response.getMessage());
            backToMenu();
        } else {
            loadRateController();
        }
    }

    public void backToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CustomerMenu.fxml"));
        loader.setControllerFactory(appContext::getBean);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add("/css/menu.css");
        MenuController controller = loader.getController();
        controller.setMap(new HashMap<>(), primaryStage);
        primaryStage.setScene(scene);
    }

    private void loadRateController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Rate.fxml"));
        loader.setControllerFactory(appContext::getBean);

        Parent p = loader.load();

        Scene scene = new Scene(p);
        scene.getStylesheets().add("css/rate.css");


        RateController controller = loader.getController();
        controller.setView(order, primaryStage);

        primaryStage.setScene(scene);
    }
}


