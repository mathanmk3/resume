package com.maveric.ce;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CurrencyExchangeApplication {

	private static final Logger Log= LoggerFactory.getLogger(CurrencyExchangeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
