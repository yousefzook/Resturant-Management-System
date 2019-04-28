package main.manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import view.manager.ViewController;

@SpringBootApplication
@ComponentScan(value = {"controller", "view.manager"})
@EnableJpaRepositories(value = "model.repository")
@EntityScan(value = "model.entity")
public class ManagerApp extends Application {
    private ConfigurableApplicationContext springContext;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader.setLocation(getClass().getResource("/fxml/ManagerView.fxml"));
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        ((ViewController) fxmlLoader.getController()).setPrimaryStage(primaryStage);
        primaryStage.show();
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(ManagerApp.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void stop() {
        springContext.stop();
    }
}