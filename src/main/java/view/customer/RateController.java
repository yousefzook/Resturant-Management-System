package view.customer;

import controller.CustomerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;
import model.entity.Dish;
import model.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class RateController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private CustomerController customerController;


    private Map<Dish, Integer> order;

    @FXML
    public Label thanks;

    @FXML
    public Label request;

    @FXML
    public Group G1, G2, G3, G4, G5;

    @FXML
    public Button back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setDisable(true);
        request.setVisible(true);
        thanks.setVisible(false);
    }

    void setView(Map<Dish, Integer> order, Stage stage) {
        this.primaryStage = stage;
        this.order = order;
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
        primaryStage.show();
    }

    public void rateOne() {
        setRate(1, G2, G3, G4, G5);
    }

    public void rateTwo() {
        setRate(2, G1, G3, G4, G5);
    }

    public void rateThree() {
        setRate(3, G1, G2, G4, G5);
    }

    public void rateFour() {
        setRate(4, G1, G2, G3, G5);
    }

    public void rateFive() {
        setRate(5, G1, G2, G3, G4);
    }

    private void setRate(int r, Group g1, Group g2, Group g3, Group g4) {
        Order orderObj = new Order(order);
        customerController.rateOrder(orderObj, r);
        g1.setVisible(false);
        g2.setVisible(false);
        g3.setVisible(false);
        g4.setVisible(false);
        back.setDisable(false);
        thanks.setVisible(true);
        request.setVisible(false);
    }
}
