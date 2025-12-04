package com.simplehealth.estoque.application.config;

import com.datastax.oss.driver.api.core.CqlSession;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CassandraKeyspaceInit {

  private final CqlSession session;

  @PostConstruct
  public void init() {
    session.execute(
        "CREATE KEYSPACE IF NOT EXISTS simplehealth_db " +
            "WITH replication = {'class':'SimpleStrategy','replication_factor':1};"
    );

    session.execute("USE simplehealth_db");
  }

}
