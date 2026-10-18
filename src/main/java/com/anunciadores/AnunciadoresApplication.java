package com.anunciadores;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableFeignClients
@EnableScheduling
@EnableAsync
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@SpringBootApplication
public class AnunciadoresApplication {
  public static void main(String[] args) {
    SpringApplication.run(com.anunciadores.AnunciadoresApplication.class, args);
  }
}