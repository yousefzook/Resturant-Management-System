package view.customer;

import controller.CustomerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

@Component
public class OrderController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private CustomerController customerController;

    @FXML
    ScrollPane scPane;

    @FXML
    Label price;

    @FXML
    Label time;

    private Map<Dish, Integer> order;
    private float totalPrice = 0;
    private int totalTime = Integer.MIN_VALUE;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private HBox getItem(Dish d) {
        HBox hBox = new HBox();
        Label name = new Label(d.getName());
        Label details = new Label(d.getPrice() + " x " + order.get(d) + " = " + d.getPrice() * order.get(d));
        totalPrice += d.getPrice() * order.get(d);
        totalTime = Math.max(totalTime, d.getTimeToPrepare());

        hBox.getChildren().addAll(name, details);
        hBox.setSpacing(50);
        hBox.setPadding(new Insets(15, 150, 15, 10));
        return hBox;
    }

    public void payAction() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Pay.fxml"));
        loader.setControllerFactory(appContext::getBean);

        Parent p = loader.load();

        Scene scene = new Scene(p);
        scene.getStylesheets().add("css/pay.css");


        PayController controller = loader.getController();
        controller.setView(order, primaryStage);

        primaryStage.setScene(scene);
    }

    public void backToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CustomerMenu.fxml"));
        loader.setControllerFactory(appContext::getBean);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add("/css/menu.css");
        MenuController controller = loader.getController();
        controller.setMap(order, primaryStage);
        primaryStage.setScene(scene);
    }

    void setView(Map<Dish, Integer> order, Stage stage) {
        this.primaryStage = stage;

        this.order = order;

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 80));

        for (Dish d : order.keySet()) {
            HBox crnt = getItem(d);
            vBox.getChildren().add(crnt);
        }

        scPane.setContent(vBox);

        time.setText(String.valueOf(totalTime) + "mins");
        price.setText(String.valueOf(totalPrice) + " $ ");
    }
}
