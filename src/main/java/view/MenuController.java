package view;

import controller.ManagerController;
import javafx.collections.ObservableList;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class MenuController implements Initializable {

    @Setter
    private ManagerController managerController;
    private Stage primaryStage;

    private Map<HBox, Integer> map = new HashMap<HBox, Integer>();

    @FXML
    ScrollPane scPane;

    @FXML
    Button addBtn, backBtn;


    public MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MenuController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("menu.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get dishes from DB
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        int t = 10 ;
        while(t-- >0) {
            HBox crnt = getItem();
            map.put(crnt, t);
            vBox.getChildren().add(crnt);
        }
        scPane.setContent(vBox);
    }

    private HBox getItem() {
        //image //ByteArray
        Image  image = new Image("/photos/chicken1.jpg");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        //name and description
        VBox vBox = new VBox();
        TextField name = new TextField("Chicken with Rosemary Butter Sauce");
        name.setEditable(false);
        TextArea desc = new TextArea("Sauteed chicken breasts smothered in a creamy rosemary butter sauce.");
        vBox.getChildren().addAll(name, desc);
        vBox.setSpacing(10);
        desc.setMaxHeight(70);
        desc.setMaxWidth(300);
        desc.setWrapText(true);
        desc.setEditable(false);

        //buttons
        Button settings = new Button();
        settings.setId("settingsBtn");

        Button delete = new Button();
        delete.setId("delBtn");

        HBox deleteAndSettings = new HBox(settings, delete);
        deleteAndSettings.setSpacing(10);

        Label rating = new Label("Rate: ");
        rating.setId("rateLable");

        Label time = new Label("Time: ");
        rating.setId("timeLabel");

        Label price = new Label("Price: ");
        rating.setId("priceLabel");

        Button  save = new Button();
        save.setId("saveBtn");
        save.setVisible(false);

        VBox vBox2 = new VBox(deleteAndSettings , rating, time,price, save);
        vBox2.setSpacing(10);

        HBox crnt = new HBox(imageView, vBox, vBox2);
        crnt.setId("menuBox");
        crnt.setSpacing(10);
        crnt.setPadding(new Insets(10, 10, 10, 10));
        actionListener(settings, save, delete);

        return crnt;
    }

    private void actionListener(Button settings, Button save, Button delete){
        addSettingsAction(settings);
        addSaveAction(save);
        addDeleteAction(delete);
        addBackAction();
        addDishAction();
    }

    private void addSettingsAction(final Button settings) {
        settings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                editRoutine(true , settings);
            }
        });
    }

    private void addSaveAction(final Button save) {
        save.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                editRoutine(false , save);
                HBox hbox = (HBox) save.getParent().getParent();
                //addDish
               // managerController.addDish().builder.build()
                //call database
            }
        });
    }

    private void addDeleteAction(final Button delete) {
        // delete from DB
        delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {

                for(Node n : delete.getParent().getParent().getParent().getParent().getChildrenUnmodifiable()){
                    if(n.equals(delete.getParent().getParent().getParent())){
                        ( (VBox) delete.getParent().getParent().getParent().getParent()).getChildren().remove(n);
                        break;
                    }
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

    private void addDishAction() {
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Node node = (Node) actionEvent.getSource();
                final Stage stage = (Stage) node.getScene().getWindow();
                AddDishController c = new AddDishController(stage);
                try {
                    c.showUp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void editRoutine(Boolean bool , final Button button){
        ObservableList<Node> ol;
        if(bool == true)
           ol = button.getParent().getParent().getChildrenUnmodifiable();
        else
            ol = button.getParent().getChildrenUnmodifiable();

        for(Node n : ol){
            String className = n.getClass().getName().split("\\.")[3];
            if(className.equals("Button")){
                Button b = (Button) n;
                b.setVisible(bool);
            }
        }

        if(bool == true)
            ol = button.getParent().getParent().getParent().getChildrenUnmodifiable();
        else
            ol = button.getParent().getParent().getChildrenUnmodifiable();


        for(Node n : ol){
            System.out.println(n.getClass().getName());
            String className = n.getClass().getName().split("\\.")[3];
            if(className.equals("VBox")){
                VBox b = (VBox) n;

                for(Node n2 : ((VBox) n).getChildren()){

                    String className2 = n2.getClass().getName().split("\\.")[3];

                    if(className2.equals("TextArea")){
                        TextArea t = (TextArea) n2;
                        System.out.println(bool + className2);
                        ((TextArea) n2).setEditable(bool);
                        if(!bool) {
                            ((TextArea) n2).setText(((TextArea) n2).getText());
                        }
                    }
                    if(className2.equals("TextField")){
                        TextField t = (TextField) n2;
                        ((TextField) n2).setEditable(bool);
                        if(!bool) {
                            ((TextField) t).setText(((TextField) t).getText());
                        }
                    }

                }

            }
        }

    }
}
