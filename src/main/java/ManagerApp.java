import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewController;

public class ManagerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewController viewController = new ViewController(primaryStage);
        viewController.showUp();
    }
}
