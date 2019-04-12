package view;

import controller.ManagerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import model.Cook;
import model.actionresults.CookResponse;
import model.actionresults.EmptyResponse;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CookController implements Initializable {

    @Setter
    private Stage primaryStage;

    @FXML
    ScrollPane scPane;

    @FXML
    Button hireBtn, backBtn;

    @FXML
    TextField firstText, lastName;

    private Map<HBox, Integer> map;

    public CookController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public CookController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerCook.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("menu.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        map = new HashMap<HBox, Integer>();
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        CookResponse r =  ManagerController.getInstance().getCooks();
        if(!r.isSuccess())
            showError(r.getMessage());
        else {
           List<Cook> list = r.getCooks();
           for(Cook c : list) {
                HBox crnt = getItem(c);
                map.put(crnt, c.getId());
                vBox.getChildren().add(crnt);
            }
            scPane.setContent(vBox);
        }
    }

    private HBox getItem(Cook cook) {
        //name and description
        VBox vBox = new VBox();
        Label fName = new Label("First Name: " + cook.getFirstName());
        Label lName = new Label("Last Name: " + cook.getLastName());
        vBox.getChildren().addAll(fName, lName);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(15, 320, 15, 10));

        //buttons
        Button delete = new Button();
        delete.setId("delBtn");
        delete.setPadding(new Insets(150, 150, 150, 150));

        HBox employee = new HBox(vBox, delete);
        employee.setSpacing(150);
        employee.setPadding(new Insets(20, 20, 20, 20));
        actionListener( delete);
        employee.setId("menuBox");

        return employee;
    }

    private void actionListener(final Button delete){
        addDeleteAction(delete);
        addBackAction();
        addHireAction();
    }

    private void addHireAction() {
        hireBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Node node = (Node) actionEvent.getSource();
                final Stage stage = (Stage) node.getScene().getWindow();
                AddCookController c = new AddCookController(stage);
                try {
                    c.showUp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addBackAction()  {
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Node node = (Node) actionEvent.getSource();
                final Stage stage = (Stage) node.getScene().getWindow();
                ViewController c = new ViewController(stage);
                try {
                    c.showUp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addDeleteAction(final Button delete) {
        delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                for(Node n : delete.getParent().getParent().getChildrenUnmodifiable()){
                    if(n.equals(delete.getParent())){
                        EmptyResponse r = ManagerController.getInstance().fireCook(map.get((HBox) delete.getParent()));
                        if(r.isSuccess()) {
                            ((VBox) delete.getParent().getParent()).getChildren().remove(n);
                        } else {
                            showError(r.getMessage());
                        }
                        break;
                    }
                }
            }
        });
    }

    private void showError(String s) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(s);
        errorAlert.showAndWait();
    }
}
