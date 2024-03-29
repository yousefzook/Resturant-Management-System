package view.manager;

import controller.ManagerController;
import javafx.collections.ObservableList;
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
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
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

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;

    private Map<HBox, Integer> map = new HashMap<>();

    @FXML
    ScrollPane scPane;

    public MenuController() {
    }

    void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ManagerMenu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/menu.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        map = new HashMap<>();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 25));

        DishResponse r = managerController.getDishes();
        if (!r.isSuccess())
            showError(r.getMessage());
        else {
            List<Dish> list = r.getDishes();
            for (Dish d : list) {
                HBox crnt = getItem(d);
                map.put(crnt, d.getId());

                vBox.getChildren().add(crnt);
            }
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
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(0, 20, 0, 30));
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
        deleteAndSettings.setSpacing(50);

        Label rating = new Label("Rate: " + String.format("%.1f",d.getRate()) + " / 5");
        rating.setId("rateLable");

        Label time = new Label("Time: " + d.getTimeToPrepare() + " mins");
        rating.setId("timeLabel");

        Label price = new Label("Price: " + d.getPrice() + " $ ");
        rating.setId("priceLabel");

        Button save = new Button();
        save.setText("Save");
        save.setWrapText(true);
        save.setId("saveBtn");
        save.setVisible(false);

        VBox vBox2 = new VBox(deleteAndSettings, rating, time, price, save);
        vBox2.setSpacing(10);
        vBox2.setPadding(new Insets(0, 10, 0, 20));

        HBox crnt = new HBox(imageView, vBox, vBox2);
        crnt.setId("menuBox");
        crnt.setSpacing(20);
        crnt.setPadding(new Insets(20, 10, 10, 30));
        actionListener(settings, save, delete);

        return crnt;
    }

    private void actionListener(Button settings, Button save, Button delete) {
        addSettingsAction(settings);
        addSaveAction(save);
        addDeleteAction(delete);
    }

    private void addSettingsAction(final Button settings) {
        settings.setOnAction(actionEvent -> editRoutine(true, settings));
    }

    private void addSaveAction(final Button save) {
        final HBox hbox = (HBox) save.getParent().getParent();
        final Dish d = Dish.builder().build();
        save.setOnAction(actionEvent -> {
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
            EmptyResponse r = managerController.updateDish(map.get(hbox), d);
            if (!r.isSuccess()) {
                showError(r.getMessage());
            }
        });
    }

    private void addDeleteAction(final Button delete) {
        delete.setOnAction(actionEvent -> {

            for (Node n : delete.getParent().getParent().getParent().getParent().getChildrenUnmodifiable()) {
                if (n.equals(delete.getParent().getParent().getParent())) {

                    EmptyResponse r = managerController.removeDish(map.get(n));
                    if (r.isSuccess()) {
                        ((VBox) delete.getParent().getParent().getParent().getParent()).getChildren().remove(n);
                    } else {
                        showError(r.getMessage());
                    }
                    break;
                }
            }
        });
    }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerView.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
    }

    public void addDish() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddDish.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((AddDishController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
    }

    private void editRoutine(Boolean bool, final Button button) {
        ObservableList<Node> ol;
        if (bool)
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

        if (bool)
            ol = button.getParent().getParent().getParent().getChildrenUnmodifiable();
        else
            ol = button.getParent().getParent().getChildrenUnmodifiable();


        for (Node n : ol) {

            String className = n.getClass().getName().split("\\.")[3];
            if (className.equals("VBox")) {
                for (Node n2 : ((VBox) n).getChildren()) {

                    String className2 = n2.getClass().getName().split("\\.")[3];

                    if (className2.equals("TextArea")) {
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
                            t.setText(t.getText());
                        }
                    }

                }

            }
        }

    }
}
