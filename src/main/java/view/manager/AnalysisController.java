package view.manager;

import controller.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import model.actionresults.CookResponse;
import model.actionresults.DishResponse;
import model.actionresults.NumericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AnalysisController implements Initializable {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ManagerController managerController;

    @FXML
    private Text incomeToday, incomeMonth, dishTop, cookTop;

    public AnalysisController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public AnalysisController() {
    }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerView.fxml"));
        System.out.println(appContext);
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        incomeToday.setText("-");
        incomeMonth.setText("-");
        dishTop.setText("-");
        cookTop.setText("-");

        NumericResponse r1 = managerController.getIncomeToday();
        NumericResponse r2 = managerController.getIncomeThisMonth();
        DishResponse r3 = managerController.getTopDishes(1);
        CookResponse r4 = managerController.getTopCooks(1);

        if (!r1.isSuccess() || !r2.isSuccess() || !r3.isSuccess() || !r4.isSuccess()) {
            showError("unable to fetch data from database");
        }
        if (r1.isSuccess()) {
            incomeToday.setText(String.valueOf(r1.getNumber()));
        }

        if (r2.isSuccess()) {
            incomeMonth.setText(String.valueOf(r2.getNumber()));
        }

        if (r3.isSuccess()) {
            if (r3.getDishes().size() == 0)
                dishTop.setText("NO Dishes Yet");
            else
                dishTop.setText(r3.getDishes().get(0).getName());
        }

        if (r4.isSuccess() && r4.getCooks().size() != 0) {
            cookTop.setText(r4.getCooks().get(0).getFirstName() + " " + r4.getCooks().get(0).getLastName());
        }
    }

    private void showError(String s) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(s);
        errorAlert.showAndWait();
    }
}
