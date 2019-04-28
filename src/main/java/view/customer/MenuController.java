package view.customer;

import controller.CustomerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import model.actionresults.DishResponse;
import model.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class MenuController implements Initializable {

    @FXML
    public AnchorPane anchor;

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private CustomerController customerController;

    private Map<HBox, Dish> map = new HashMap<>();

    private Map<Dish, Integer> order = new HashMap<>();


    @FXML
    ScrollPane scPane;

    @FXML
    Button payAndOrderBtn;

    @FXML


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        map = new HashMap<>();
    }

    private void buildScene() {

        ScrollPane scPane = new ScrollPane();
        scPane.setId("scPane");
        scPane.setLayoutX(10.0);
        scPane.setLayoutY(14.0);
        scPane.setPrefHeight(496.0);
        scPane.setPrefWidth(781.0);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 20));

        DishResponse r = customerController.getDishes();
        if (!r.isSuccess())
            showError(r.getMessage());
        else {
            List<Dish> list = r.getDishes();
            for (Dish d : list) {
                HBox crnt = getItem(d);
                map.put(crnt, d);

                vBox.getChildren().add(crnt);
            }
            scPane.setContent(vBox);
        }
        if (order.size() == 0)
            payAndOrderBtn.setDisable(true);
        else
            payAndOrderBtn.setDisable(false);

        anchor.getChildren().add(scPane);
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setResizable(true);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public void orderAction() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Order.fxml"));
        loader.setControllerFactory(appContext::getBean);

        Parent p = loader.load();

        Scene scene = new Scene(p);

        OrderController controller = loader.getController();
        controller.setView(order, primaryStage);

        primaryStage.setScene(scene);
    }

    private HBox getItem(Dish d) {
        File file = new File(d.getImagePath());
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        //name and description
        VBox vBox = new VBox();
        Label name = new Label(d.getName());
        Label desc = new Label(d.getDescription());
        vBox.setSpacing(10);
        desc.setMinHeight(70);
        desc.setMinWidth(300);
        desc.setMaxHeight(70);
        desc.setMaxWidth(300);
        desc.setWrapText(true);
        vBox.getChildren().addAll(name, desc);
        vBox.setPadding(new Insets(0, 10, 0, 15));


        Label rating = new Label("Rate: " + d.getRate() + " / 5");
        rating.setId("rateLable");

        Label time = new Label("Time: " + d.getTimeToPrepare() + " mins");
        rating.setId("timeLabel");


        Label price = new Label("Price: " + d.getPrice() + " $ ");
        rating.setId("priceLabel");

        //buttons
        Button decrease = new Button();
        decrease.setId("decreaseBtn");
        decrease.setWrapText(true);
        decrease.setText("  -  ");

        Button increase = new Button();
        increase.setId("increaseBtn");
        increase.setWrapText(true);
        increase.setText(" + ");

        Label amount = new Label("0");
        amount.setId("amountLable");

        if (order != null && order.containsKey(d))
            amount.setText(String.valueOf(order.get(d)));


        HBox quantity = new HBox(decrease, amount, increase);
        quantity.setAlignment(Pos.CENTER);
        quantity.setSpacing(30);

        VBox vBox2 = new VBox(price, rating, time, quantity);
        vBox2.setSpacing(20);
        vBox2.setPadding(new Insets(0, 15, 0, 15));


        HBox crnt = new HBox(imageView, vBox, vBox2);
        crnt.setId("menuBox");
        crnt.setSpacing(40);
        crnt.setPadding(new Insets(20, 10, 10, 30));
        actionListener(decrease, increase);

        return crnt;
    }

    private void actionListener(Button decrease, Button increase) {
        addIncreaseAction(increase);
        addDecreaseAction(decrease);
    }

    private void addIncreaseAction(Button increase) {
        increase.setOnAction(actionEvent -> {
            payAndOrderBtn.setDisable(false);
            for (Node n : increase.getParent().getChildrenUnmodifiable()) {
                String className = n.getClass().getName().split("\\.")[3];
                if (className.equals("Label")) {
                    Label amount = (Label) n;
                    int currentAmount = Integer.valueOf(amount.getText());
                    if (currentAmount == 10)
                        return;
                    ((Label) n).setText(String.valueOf(currentAmount + 1));
                }
            }

            HBox hbox = (HBox) increase.getParent().getParent().getParent();

            Dish dish = map.get(hbox);

            order.put(dish, order.getOrDefault(dish, 0) + 1);
        });
    }

    private void addDecreaseAction(Button decrease) {
        decrease.setOnAction(actionEvent -> {

            for (Node n : decrease.getParent().getChildrenUnmodifiable()) {
                String className = n.getClass().getName().split("\\.")[3];
                if (className.equals("Label")) {
                    Label amount = (Label) n;
                    int currentAmount = Integer.valueOf(amount.getText());
                    if (currentAmount == 0)
                        return;

                    ((Label) n).setText(String.valueOf(currentAmount - 1));
                }
            }

            HBox hbox = (HBox) decrease.getParent().getParent().getParent();

            Dish dish = map.get(hbox);

            order.put(dish, order.get(dish) - 1);

            if (order.get(dish) == 0)
                order.remove(dish);
            if (order.size() == 0)
                payAndOrderBtn.setDisable(true);

        });
    }

    public void setMap(Map<Dish, Integer> order, Stage s) {
        this.primaryStage = s;
        this.order = order;
        buildScene();
    }
}
