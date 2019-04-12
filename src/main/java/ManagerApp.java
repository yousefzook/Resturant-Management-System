import controller.ManagerController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.DBMethods;
import view.ViewController;

public class ManagerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewController viewController = new ViewController(primaryStage);
        DBMethods db = null;
        ManagerController managerController = ManagerController.getInstance();
        managerController.setDb(db);
        viewController.setManagerController(managerController);
        viewController.showUp();
    }
}
