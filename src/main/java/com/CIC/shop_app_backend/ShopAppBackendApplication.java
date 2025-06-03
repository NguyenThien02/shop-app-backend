package com.CIC.shop_app_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ShopAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopAppBackendApplication.class, args);
	}

}
