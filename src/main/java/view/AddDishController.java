package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Dish;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddDishController implements Initializable {

    private ManagerController managerController;
    private Stage primaryStage;

    @FXML
    private Button saveBtn, addPhoto, close;

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea descText;

    @FXML
    private TextField nameText, priceText, timeText;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public AddDishController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        managerController = new ManagerController();
    }

    public AddDishController() {

    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/addDish.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("addDish.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void save(MouseEvent mouseEvent) throws IOException {
        // save data to dataBase
        Dish dish = Dish.builder()
                .name(nameText.getText())
                .description(descText.getText())
                .price(Float.valueOf(priceText.getText()))
                .timeToPrepare(Integer.valueOf(timeText.getText()))
                .imagePath(timeText.getText())
                .build();
//        Dish dish = new Dish(null, nameText.getText(),
//                null, descText.getText(), Float.valueOf(priceText.getText()),
//                null, null, Integer.valueOf(timeText.getText()));

        backToMenu(mouseEvent);
    }

    private void backToMenu(MouseEvent mouseEvent) throws IOException {
        Node node = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) node.getScene().getWindow();
        MenuController c = new MenuController(stage);
        c.showUp();
    }

    @FXML
    public void addPhoto(MouseEvent mouseEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
                , new FileChooser.ExtensionFilter("JPEG", "*.jpg")
        );
        Node node = (Node) mouseEvent.getSource();
        File selectedFile = fileChooser.showOpenDialog((Stage) node.getScene().getWindow());
        Image image1 = new Image(selectedFile.toURI().toURL().toString());
        System.out.println(image1);
        imageView.setImage(image1);
    }

    @FXML
    public void back(MouseEvent mouseEvent) throws IOException {
        backToMenu(mouseEvent);
    }
}
