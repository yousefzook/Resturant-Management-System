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
import model.Dish;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class MenuController implements Initializable {

    @Setter
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
            addBackAction();
            addDishAction();
            scPane.setContent(vBox);
        }
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private HBox getItem(Dish d) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(d.getImage()));
            ImageIO.write(img, "jpg", new File(d.getName() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = new Image("/resources/" + d.getName() + "jpg");
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

        //buttons
        Button settings = new Button();
        settings.setId("settingsBtn");

        Button delete = new Button();
        delete.setId("delBtn");

        HBox deleteAndSettings = new HBox(settings, delete);
        deleteAndSettings.setSpacing(10);

        Label rating = new Label("Rate: " + d.getRate());
        rating.setId("rateLable");

        Label time = new Label("Time: " + d.getTimeToPrepare());
        rating.setId("timeLabel");

        Label price = new Label("Price: " + d.getPrice());
        rating.setId("priceLabel");

        Button save = new Button();
        save.setId("saveBtn");
        save.setVisible(false);

        VBox vBox2 = new VBox(deleteAndSettings, rating, time, price, save);
        vBox2.setSpacing(10);

        HBox crnt = new HBox(imageView, vBox, vBox2);
        crnt.setId("menuBox");
        crnt.setSpacing(10);
        crnt.setPadding(new Insets(10, 10, 10, 10));
        actionListener(settings, save, delete);

        return crnt;
    }

    private void actionListener(Button settings, Button save, Button delete) {
        addSettingsAction(settings);
        addSaveAction(save);
        addDeleteAction(delete);
    }

    private void addSettingsAction(final Button settings) {
        settings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                editRoutine(true, settings);
            }
        });
    }

    private void addSaveAction(final Button save) {
        final HBox hbox = (HBox) save.getParent().getParent();
        final Dish d = Dish.builder().build();
        ;
        save.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                editRoutine(false, save);
                for (Node n : hbox.getChildrenUnmodifiable()) {

                    String className = n.getClass().getName().split("\\.")[3];
                    if (className.equals("VBox")) {
                        VBox b = (VBox) n;

                        for (Node n2 : b.getChildren()) {
                            String className2 = n2.getClass().getName().split("\\.")[3];
                            if (className2.equals("TextArea")) {
                                TextArea t = (TextArea) n2;
                                d.setDescription(t.getText());
                            }
                            if (className2.equals("TextField")) {
                                TextField t = (TextField) n2;
                                d.setName((t.getText()));
                            }
                        }
                        break;
                    }
                }
                EmptyResponse r = ManagerController.getInstance().updateDish(map.get(hbox), d);
                if (!r.isSuccess()) {
                    showError(r.getMessage());
                }
            }
        });
    }

    private void addDeleteAction(final Button delete) {
        delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {

                for (Node n : delete.getParent().getParent().getParent().getParent().getChildrenUnmodifiable()) {
                    if (n.equals(delete.getParent().getParent().getParent())) {
                        EmptyResponse r = ManagerController.getInstance().removeDish(map.get((HBox) delete.getParent()));
                        if (r.isSuccess()) {
                            ((VBox) delete.getParent().getParent().getParent().getParent()).getChildren().remove(n);
                        } else {
                            showError(r.getMessage());
                        }
                        break;
                    }
                }
            }
        });
    }

    private void addBackAction() {
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

    private void editRoutine(Boolean bool, final Button button) {
        ObservableList<Node> ol;
        if (bool == true)
            ol = button.getParent().getParent().getChildrenUnmodifiable();
        else
            ol = button.getParent().getChildrenUnmodifiable();

        for (Node n : ol) {
            String className = n.getClass().getName().split("\\.")[3];
            if (className.equals("Button")) {
                Button b = (Button) n;
                b.setVisible(bool);
            }
        }

        if (bool == true)
            ol = button.getParent().getParent().getParent().getChildrenUnmodifiable();
        else
            ol = button.getParent().getParent().getChildrenUnmodifiable();


        for (Node n : ol) {

            String className = n.getClass().getName().split("\\.")[3];
            if (className.equals("VBox")) {
                VBox b = (VBox) n;

                for (Node n2 : ((VBox) n).getChildren()) {

                    String className2 = n2.getClass().getName().split("\\.")[3];

                    if (className2.equals("TextArea")) {
                        TextArea t = (TextArea) n2;
                        System.out.println(bool + className2);
                        ((TextArea) n2).setEditable(bool);
                        if (!bool) {
                            ((TextArea) n2).setText(((TextArea) n2).getText());
                        }
                    }
                    if (className2.equals("TextField")) {
                        TextField t = (TextField) n2;
                        ((TextField) n2).setEditable(bool);
                        if (!bool) {
                            ((TextField) t).setText(((TextField) t).getText());
                        }
                    }

                }

            }
        }

    }
}
