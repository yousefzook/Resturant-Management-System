package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private ManagerController managerController;

    @FXML
    ScrollPane scPane;

    @FXML
    Button payBtn;

    @FXML
    Label price;

    @FXML
    Label time;

    private Map<Dish, Integer> order;
    private float totalPrice = 0;
    private int totalTime = Integer.MIN_VALUE;

    public OrderController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public OrderController() {
    }

    void showUp() throws IOException {
        System.out.println("in showup");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Order.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/order.css");


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(" in initialize");




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

    public void backToMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CustomerMenu.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        CustomerMenuController controller = ((CustomerMenuController) fxmlLoader.getController());
        controller.setMap(order);
        controller.setPrimaryStage(primaryStage);
    }

    public void setView(Map<Dish, Integer> order , Stage stage) {
        this.primaryStage = stage;

        System.out.println("settong order " + order);
        this.order = order;

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 80));

        for(Dish d : order.keySet()) {
            HBox crnt = getItem(d);
            vBox.getChildren().add(crnt);
        }
        System.out.println("show pane");

        scPane.setContent(vBox);

        time.setText(String.valueOf(totalTime) + "mins");
        price.setText(String.valueOf(totalPrice) + " $ ");
    }
}
