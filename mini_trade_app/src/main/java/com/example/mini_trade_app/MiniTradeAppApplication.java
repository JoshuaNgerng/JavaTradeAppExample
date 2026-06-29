package com.example.mini_trade_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.mini_trade_app.config.*;

@SpringBootApplication
@EnableConfigurationProperties({
	JwtProperties.class, PasswordProperties.class, AdminProperties.class,
	JpaConfig.class
})
public class MiniTradeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniTradeAppApplication.class, args);
	}

}
