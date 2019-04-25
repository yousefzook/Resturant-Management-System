package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import model.actionresults.EmptyResponse;
import model.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AddDishController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea descText;

    @FXML
    private TextField nameText, priceText, timeText;

    private String photoPath;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public AddDishController() {
    }

    public void save() {
        if (isValid()) {
            Dish dish = Dish.builder()
                    .name(nameText.getText())
                    .description(descText.getText())
                    .price(Float.valueOf(priceText.getText()))
                    .timeToPrepare(Integer.valueOf(timeText.getText()))
                    .active(true)
                    .imagePath(photoPath)
                    .build();
            EmptyResponse r = managerController.addDish(dish);
            if (!r.isSuccess()) {
                showError(r.getMessage());
            }
            try {
                System.out.println("Leaving AddDishController");
                backToMenu();
            } catch (IOException e) {
                showError(e.getMessage());
            }
        } else {
            showError("Check input");
        }
    }

    private void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(message);
        System.out.println(message);
        errorAlert.setResizable(true);
        errorAlert.showAndWait();
    }

    private boolean isValid() {
        // name and description
        Pattern namPattern = Pattern.compile("^[a-zA-Z\\s()0-9]+$");
        Matcher nameMatcher = namPattern.matcher(nameText.getText().trim());
        Pattern descPattern = Pattern.compile("^[a-zA-Z\\s().,0-9]+$");
        Matcher descMatcher = descPattern.matcher(descText.getText().trim());
        if (!nameMatcher.matches() || !descMatcher.matches()) {
            return false;
        }
        try {
            Double.parseDouble(priceText.getText().trim());
            Integer.parseInt(timeText.getText().trim());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void backToMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerMenu.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((MenuController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
    }

    public void addPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        photoPath = selectedFile.toPath().toString();


        File file = new File(photoPath);
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
    }
}
