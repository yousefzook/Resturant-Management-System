package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ViewController {

    @Setter
    private Stage primaryStage;

    @Autowired
    private ConfigurableApplicationContext appContext;

    public ViewController() {
    }

    public void showAnalysis() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerAnalysis.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((AnalysisController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
    }

    public void showEmployees() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerCook.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((CookController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
    }

    public void showMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ManagerMenu.fxml"));
        fxmlLoader.setControllerFactory(appContext::getBean);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((MenuController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
    }
}
