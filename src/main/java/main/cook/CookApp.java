package main.cook;

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
import view.cook.CookLoginController;

import java.util.HashMap;

@SpringBootApplication
@ComponentScan(value = {"controller", "view.cook"})
@EnableJpaRepositories(value = "model.repository")
@EntityScan(value = "model.entity")
public class CookApp extends Application {
    private ConfigurableApplicationContext springContext;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader.setLocation(getClass().getResource("/fxml/CookLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("/css/CookLogin.css");
        CookLoginController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(CookApp.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void stop() {
        springContext.stop();
    }
}