import controller.ManagerController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.DBMethods;
import model.RestaurantDBLayer;
import view.ViewController;

public class ManagerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewController viewController = new ViewController(primaryStage);
        DBMethods db = new RestaurantDBLayer("RESTAURANT.db");
        ManagerController managerController = ManagerController.getInstance();
        managerController.setDb(db);
        managerController.setDb(db);
        viewController.setManagerController(managerController);
        viewController.showUp();
    }
}
