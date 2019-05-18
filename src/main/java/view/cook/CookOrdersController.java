package view.cook;

import controller.CookController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import model.OrderState;
import model.actionresults.EmptyResponse;
import model.actionresults.OrderResponse;
import model.entity.Cook;
import model.entity.Dish;
import model.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class CookOrdersController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private CookController cookController;

    @FXML
    ScrollPane scPane;

    @FXML
    public AnchorPane anchor;

    @FXML
    public Label cook;

    private Cook signedCook;
    private Map<HBox, Order> map = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setCook(Cook signedCook) {
        this.signedCook = signedCook;
        try {
            buildScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildScene() {

        cook.setText(signedCook.getId() + ": " + signedCook.getFirstName() + signedCook.getLastName());
        scPane.setId("scPane");
        scPane.setLayoutX(10.0);
        scPane.setLayoutY(14.0);
        scPane.setMaxHeight(496.0);
        scPane.setPrefWidth(781.0);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 20));

        OrderResponse r1 = cookController.getOrdersAssignedTo(signedCook.getId());
        OrderResponse r2 = cookController.getInQueueOrders();

        if (!r1.isSuccess() || !r2.isSuccess())
            showError(r1.getMessage() + " , " + r2.getMessage());
        else {
            List<Order> list = r1.getOrders();
            list.addAll(r2.getOrders());

            for (Order o : list) {
                HBox crnt = getItem(o);
                map.put(crnt, o);

                vBox.getChildren().add(crnt);
            }
            scPane.setContent(vBox);
        }
    }

    private HBox getItem(Order o) { // state, content
        //state , button
        VBox vBox = new VBox();
        Label state = new Label(o.getState().toString());
        vBox.setSpacing(10);
        Button btn = new Button();
        if (o.getState().toString().equals("IN_QUEUE")) {
            btn.setText("Accept");
        } else {
            btn.setText("Done");
        }
        vBox.getChildren().addAll(state, btn);
        vBox.setPadding(new Insets(0, 10, 0, 15));

        // order details
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(450);
        VBox vBox1 = new VBox();
        for (Dish d : o.getDetails().keySet()) {
            Label label = new Label(d.getName() + "\t" + o.getDetails().get(d));
            vBox1.getChildren().add(label);
        }
        scrollPane.setContent(vBox1);


        HBox crnt = new HBox(scrollPane, vBox);
        crnt.setId("menuBox");
        crnt.setSpacing(40);
        crnt.setPadding(new Insets(20, 10, 10, 30));
        actionListener(btn, state);

        return crnt;
    }

    private void actionListener(Button btn, Label state) {
        btn.setOnAction(actionEvent -> {
            HBox hbox = (HBox) btn.getParent().getParent();
            Order currentOrder = map.get(hbox);

            if (btn.getText().equals("Accept")) {
                EmptyResponse r;
                try {
                    r = cookController.updateOrderState(signedCook.getId(), currentOrder.getId(), OrderState.Assigned);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (r.isSuccess()) {
                    state.setText("ASSIGNED");
                    btn.setText("Done");
                } else {
                    try {
                        showError(r.getMessage());
                        buildScene();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //TODO SEND TO DATABSE NEW STATE
                //TODO CHANGE LABEL TO NEW STATE
                //TODO ASSIGN ORDER TO CURRENT COOOKID IN DATABASE
                //TODO BUILDSCENE() IF DISH IS ALREADY ACCEPTED
            } else {

                EmptyResponse r;
                try {
                    r = cookController.updateOrderState(signedCook.getId(), currentOrder.getId(), OrderState.Done);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (r.isSuccess()) {
                    ((VBox) btn.getParent().getParent().getParent()).getChildren().remove(hbox);
                    map.remove(hbox);
                } else {
                    showError(r.getMessage());
                }

                //TODO SEND TO DATABSE NEW STATE
                //TODO REMOVE CURRENT HBOX
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

    public void refresh() {
        buildScene();
    }

    public void logout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CookLogin.fxml"));
        loader.setControllerFactory(appContext::getBean);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add("/css/CookLogin.css");
        CookLoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
    }

}
