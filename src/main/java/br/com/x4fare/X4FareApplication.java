package br.com.x4fare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients("br.com.x4fare.services.webclient")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class X4FareApplication {

    public static void main(String[] args) {
        SpringApplication.run(X4FareApplication.class, args);
    }

}
