package com.simplehealth.cadastro.infrastructure.redis.subscribers;

import com.simplehealth.cadastro.domain.events.HistoricoAgendamentoResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoEstoqueResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoPagamentoResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoProcedimentoResponseEvent;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricoResponseSubscriber {

  private final ConcurrentHashMap<String, Object> cache;
  private final RedisConnectionFactory redisConnectionFactory;

  @PostConstruct
  public void subscribeChannels() {
    var conn = redisConnectionFactory.getConnection();

    // Agendamento
    conn.subscribe((msg, ch) -> {
      var response = (HistoricoAgendamentoResponseEvent)
          deserializeObject(msg.getBody());
      cache.put(response.getCorrelationId() + ":ag", response);
    }, "historico.agendamento.response".getBytes());

    // Procedimentos
    conn.subscribe((msg, ch) -> {
      var response = (HistoricoProcedimentoResponseEvent)
          deserializeObject(msg.getBody());
      cache.put(response.getCorrelationId() + ":proc", response);
    }, "historico.procedimento.response".getBytes());

    // Estoque
    conn.subscribe((msg, ch) -> {
      var response = (HistoricoEstoqueResponseEvent)
          deserializeObject(msg.getBody());
      cache.put(response.getCorrelationId() + ":est", response);
    }, "historico.estoque.response".getBytes());

    // Pagamentos
    conn.subscribe((msg, ch) -> {
      var response = (HistoricoPagamentoResponseEvent)
          deserializeObject(msg.getBody());
      cache.put(response.getCorrelationId() + ":pag", response);
    }, "historico.pagamento.response".getBytes());
  }

  private Object deserializeObject(byte[] bytes) {
    try (var bis = new ByteArrayInputStream(bytes); var ois = new ObjectInputStream(bis)) {
      return ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException("Falha ao desserializar objeto do Redis", e);
    }
  }
}
