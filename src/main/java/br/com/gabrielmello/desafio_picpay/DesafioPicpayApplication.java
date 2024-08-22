package br.com.gabrielmello.desafio_picpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableJdbcAuditing
@SpringBootApplication
public class DesafioPicpayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioPicpayApplication.class, args);
	}

}
