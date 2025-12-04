package com.simplehealth.estoque.application.config;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.simplehealth.estoque.infrastructure.repositories")
public class CassandraConfig {

  @Bean
  public CqlSession cassandraSession(
      @Value("${spring.cassandra.contact-points}") String contactPoints,
      @Value("${spring.cassandra.port}") int port,
      @Value("${spring.cassandra.username}") String username,
      @Value("${spring.cassandra.password}") String password
  ) {
    int attempts = 0;
    while (attempts < 10) {
      try {
        return CqlSession.builder()
            .addContactPoint(new InetSocketAddress(contactPoints, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(username, password)
            .build();
      } catch (Exception e) {
        attempts++;
        try {
          Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
      }
    }
    throw new IllegalStateException("Não foi possível conectar ao Cassandra");
  }

  @Bean
  public CassandraTemplate cassandraTemplate(CqlSession session) {
    return new CassandraTemplate(session);
  }
}
