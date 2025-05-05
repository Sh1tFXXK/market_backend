package org.example.market_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan(basePackages = "org.example.market_backend.Mapper")
@RestController
public class MarketBackendApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MarketBackendApplication.class, args);
    }
    
    @GetMapping("/")
    public String home() {
        return "Market Backend Application is running...";
    }

}