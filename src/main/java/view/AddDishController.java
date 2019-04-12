package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Dish;
import model.actionresults.EmptyResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDishController implements Initializable {

    private Stage primaryStage;

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea descText;

    @FXML
    private TextField nameText, priceText, timeText;

    private Dish dish;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public AddDishController(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
        if(isValid()) {
            dish = Dish.builder()
                    .name(nameText.getText())
                    .description(descText.getText())
                    .price(Float.valueOf(priceText.getText()))
                    .timeToPrepare(Integer.valueOf(timeText.getText()))
                    .build();
            EmptyResponse r = ManagerController.getInstance().addDish(dish);
            if(!r.isSuccess()) {
                showError(r.getMessage());
            }
            backToMenu(mouseEvent);
        } else {
          showError("Check input");
        }
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private boolean isValid() {
        // name and description
        Pattern pattern = Pattern.compile(new String ("^[a-zA-Z\\s]+$"));
        Matcher matcherF = pattern.matcher(nameText.getText().trim());
        Matcher matcherL = pattern.matcher(descText.getText().trim());
        if(! matcherF.matches() || !matcherL.matches()) {
            return false;
        }
        try {
            double p = Double.parseDouble(priceText.getText().trim());
            int t = Integer.parseInt(timeText.getText().trim());
        } catch (Exception e) {
            return false;
        }
        return true;
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
        dish.setImagePath(selectedFile.toURI().toString());
    }

    @FXML
    public void back(MouseEvent mouseEvent) throws IOException {
        backToMenu(mouseEvent);
    }
}
