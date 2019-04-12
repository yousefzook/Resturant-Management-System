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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CookController implements Initializable {

    @Setter
    private ManagerController managerController;
    private Stage primaryStage;

    @FXML
    ScrollPane scPane;

    @FXML
    Button hireBtn, backBtn;

    @FXML
    TextField firstText, lastName;

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
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        int t = 10 ;
        while(t-- >0) {
            HBox crnt = getItem();
           // map.put(crnt, t);
            vBox.getChildren().add(crnt);
        }
        scPane.setContent(vBox);
    }

    private HBox getItem() {


        //name and description
        VBox vBox = new VBox();
        Label fName = new Label("First Name:");
        Label lName = new Label("Last Name: ");
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
                        ( (VBox) delete.getParent().getParent()).getChildren().remove(n);
                        break;
                    }
                }

            }
        });
    }
}
