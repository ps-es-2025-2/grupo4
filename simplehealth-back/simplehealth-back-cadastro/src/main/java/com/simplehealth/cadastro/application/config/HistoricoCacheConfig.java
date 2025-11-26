package com.simplehealth.cadastro.application.config;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistoricoCacheConfig {

  @Bean
  public ConcurrentHashMap<String, Object> historicoCache() {
    return new ConcurrentHashMap<>();
  }
}
