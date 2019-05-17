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
import model.entity.Dish;
import model.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import view.customer.MenuController;

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

    int cookId;
    private Map<HBox, Order> map = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            buildScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCookID(int cookID) {
        this.cookId = cookID;
    }

    private void buildScene() throws Exception {

        ScrollPane scPane = new ScrollPane();
        scPane.setId("scPane");
        scPane.setLayoutX(10.0);
        scPane.setLayoutY(14.0);
        scPane.setPrefHeight(496.0);
        scPane.setPrefWidth(781.0);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 20));

        OrderResponse r1 = cookController.getOrdersAssignedTo(cookId);
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

        anchor.getChildren().add(scPane);
    }

//    private void buildScene() throws Exception {
//
//        ScrollPane scPane = new ScrollPane();
//        scPane.setId("scPane");
//        scPane.setLayoutX(10.0);
//        scPane.setLayoutY(14.0);
//        scPane.setPrefHeight(496.0);
//        scPane.setPrefWidth(781.0);
//        VBox vBox = new VBox();
//        vBox.setSpacing(10);
//        vBox.setPadding(new Insets(10, 0, 0, 20));
//
////        OrderResponse r1 = cookController.getOrdersAssignedTo(cookId);
////        OrderResponse r2 = cookController.getInQueueOrders();
//
////        if (!r1.isSuccess() || !r2.isSuccess())
////            showError(r1.getMessage() + " , " + r2.getMessage());
////        else {
////            List<Order> list = r1.getOrders();
////            list.addAll(r2.getOrders());
//
//        Order or1 = new Order();
//        Order or2 = new Order();
//
//        or1.setState(OrderState.inQueue);
//        or2.setState(OrderState.inQueue);
//
//        Map<Dish , Integer> temp = new HashMap<>();
//        Dish d = new Dish();
//        d.setPrice((float) 423);
//        d.setName("balala");
//        Dish d2 = new Dish();
//        d2.setPrice((float) 423);
//        d2.setName("helloooooooooooooo");
//
//        Dish d3 = new Dish();
//        d3.setPrice((float) 423);
//        d3.setName("ahleeeeeeeen");
//
//        Dish d4 = new Dish();
//        d4.setPrice((float) 423);
//        d4.setName("balaaaaaaaaaaaaaaaaaaaaaaaaaala 2 ");
//
//        Dish d5 = new Dish();
//        d5.setPrice((float) 423);
//        d5.setName("mar7abaaaaaaaaa ");
//
//        temp.put(d , 3);
//        temp.put(d2 , 4);
//        temp.put(d3 , 5);
//        temp.put(d4 , 6);
//        temp.put(d5 , 6);
//        or1.setDetails(temp);
////            for (Order o : list) {
//        HBox crnt = getItem(or1);
//        map.put(crnt, or1);
//        or2.setDetails(temp);
////            for (Order o : list) {
//        HBox crnt2 = getItem(or2);
//        map.put(crnt2, or2);
//
//        vBox.getChildren().addAll(crnt , crnt2);
////            }
//        scPane.setContent(vBox);
////        }
//
//        anchor.getChildren().add(scPane);
//    }

    private HBox getItem(Order o) throws Exception { // state, content
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
        scrollPane.setPrefWidth(500);
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
        actionListener(btn , state);

        return crnt;
    }

    private void actionListener(Button btn ,Label state) throws Exception {
        HBox hbox = (HBox) btn.getParent().getParent();
        Order currentOrder = map.get(hbox);

        if(btn.getText().equals("Accept")){

            EmptyResponse r = cookController.updateOrderState(cookId ,currentOrder.getId() , OrderState.inQueue );
           if(r.isSuccess()) {
               state.setText("ASSIGNED");
               btn.setText("Done");
           }
           else {
               buildScene();
           }
            //TODO SEND TO DATABSE NEW STATE
            //TODO CHANGE LABEL TO NEW STATE
            //TODO ASSIGN ORDER TO CURRENT COOOKID IN DATABASE
            //TODO BUILDSCENE() IF DISH IS ALREADY ACCEPTED
        } else {

            EmptyResponse r = cookController.updateOrderState(cookId , currentOrder.getId(), OrderState.Done);
            if (r.isSuccess()) {
                btn.getParent().getParent().getParent().getChildrenUnmodifiable().remove(hbox);
                map.remove(hbox);

            } else {
                showError(r.getMessage());
            }

            //TODO SEND TO DATABSE NEW STATE
            //TODO REMOVE CURRENT HBOX
        }
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setResizable(true);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public void refresh() throws Exception {
        buildScene();
    }

    public void logout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CookLogin.fxml"));
        loader.setControllerFactory(appContext::getBean);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add("/css/CookLogin.css");
        MenuController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
    }

}
