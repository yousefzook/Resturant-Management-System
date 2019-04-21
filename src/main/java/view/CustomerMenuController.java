package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import model.Dish;
import model.actionresults.DishResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CustomerMenuController implements Initializable {

    @Setter
    private Stage primaryStage;

    private Map<HBox, Integer> map = new HashMap<HBox, Integer>();

    @FXML
    ScrollPane scPane;

    @FXML
    Button payAndOrderBtn;


    public CustomerMenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public CustomerMenuController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerMenu.fxml"));

        Scene scene = new Scene(root);

        scene.getStylesheets().add("menu.css");

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        map = new HashMap<HBox, Integer>();
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        DishResponse r = ManagerController.getInstance().getDishes();
        if (!r.isSuccess())
            showError(r.getMessage());
        else {
            List<Dish> list = r.getDishes();
            for (Dish d : list) {
                HBox crnt = getItem(d);
                map.put(crnt, d.getId());
                vBox.getChildren().add(crnt);
            }
            addPayAndOrderAction();
            scPane.setContent(vBox);
        }

    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setResizable(true);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private void addPayAndOrderAction() {

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
        TextField name = new TextField(d.getName());
        name.setEditable(false);
        TextArea desc = new TextArea(d.getDescription());
        vBox.getChildren().addAll(name, desc);
        vBox.setSpacing(10);
        desc.setMaxHeight(70);
        desc.setMaxWidth(300);
        desc.setWrapText(true);
        desc.setEditable(false);


        Label rating = new Label("Rate: " + d.getRate());
        rating.setId("rateLable");

        Label time = new Label("Time: " + d.getTimeToPrepare());
        rating.setId("timeLabel");

        Label price = new Label("Price: " + d.getPrice());
        rating.setId("priceLabel");


        //buttons
        Button decrease = new Button();
        decrease.setId("decreaseBtn");

        Button increase = new Button();
        increase.setId("increaseBtn");

        Label amount = new Label("0");
        amount.setId("amountLable");

        HBox quantity = new HBox(decrease, amount, increase);
        quantity.setSpacing(10);

        VBox vBox2 = new VBox(price, rating, time, quantity);
        vBox2.setSpacing(10);


        HBox crnt = new HBox(imageView, vBox, vBox2);
        crnt.setId("menuBox");
        crnt.setSpacing(10);
        crnt.setPadding(new Insets(10, 10, 10, 10));
        actionListener(decrease, increase);

        return crnt;
    }

    private void actionListener(Button decrease, Button increase) {
        addIncreaseAction(decrease);
        addDecreaseAction(increase);
    }

    private void addIncreaseAction(Button increase) {
    }

    private void addDecreaseAction(Button decrease) {
    }
}
