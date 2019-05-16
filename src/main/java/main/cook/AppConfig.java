package main.cook;

import controller.CookController;
import controller.UploadCareService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CookController cookController() {
        return new CookController();
    }
}
