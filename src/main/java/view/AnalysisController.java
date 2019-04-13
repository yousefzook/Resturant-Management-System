package view;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import model.actionresults.DishResponse;
import model.actionresults.NumericResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AnalysisController implements Initializable {

    @Setter
    private Stage primaryStage;

    @FXML
    private Text incomeToday, incomeMonth, dishTop;

    public AnalysisController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public AnalysisController() {
    }

    public void showUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerAnalysis.fxml"));

//        NumericResponse r1 = ManagerController.getInstance().getIncomeToday();
//        NumericResponse r2 = ManagerController.getInstance().getIncomeThisMonth();
//        DishResponse r3 = ManagerController.getInstance().getTopDishes(1);
//
//        if (!r1.isSuccess() || !r2.isSuccess() || !r3.isSuccess()) {
//            showError("unable to fetch data from database");
//
//            ViewController c = new ViewController(primaryStage);
//            c.showUp();
//
//        }
//        else {
//            incomeToday.setText(String.valueOf(r1.getNumber()));
//            incomeMonth.setText(String.valueOf(r2.getNumber()));
//            incomeToday.setText("TEXT");
//            incomeMonth.setText("TEXT");
//            if (r3.getDishes().size() == 0)
//                dishTop.setText("NO Dishes Yet");
//            else
//                dishTop.setText(r3.getDishes().get(0).getName());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
//        }


    }

    public void goBack(MouseEvent mouseEvent) throws IOException {
        Node node = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) node.getScene().getWindow();
        ViewController c = new ViewController(stage);
        c.showUp();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        incomeToday.setText("TEXT");
        incomeMonth.setText("TEXT");
        dishTop.setText("NO Dishes Yet");
    }

    private void showError(String s) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(s);
        errorAlert.showAndWait();
    }
}
