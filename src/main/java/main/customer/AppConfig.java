package main.customer;

import controller.CustomerController;
import controller.UploadCareService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

        @Bean
    public CustomerController managerController() {
        return new CustomerController();
    }

    @Bean
    public UploadCareService uploadCareService() {
        return new UploadCareService();
    }
}
