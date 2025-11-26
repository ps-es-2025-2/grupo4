package com.simplehealth.cadastro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.simplehealth.cadastro.gateway")
@SpringBootApplication
public class SimpleHealthCadastroApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimpleHealthCadastroApplication.class, args);
  }

}
