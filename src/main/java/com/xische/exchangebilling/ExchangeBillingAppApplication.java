package com.xische.exchangebilling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.xische.exchangebilling.repository")
@EntityScan(basePackages = "com.xische.exchangebilling.model")
public class ExchangeBillingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeBillingAppApplication.class, args);
    }

}
