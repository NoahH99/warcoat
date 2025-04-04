package com.noahhendrickson.warcoat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.noahhendrickson.warcoat",
        "com.noahhendrickson.common"
})
public class WarcoatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarcoatApplication.class, args);
    }
}
