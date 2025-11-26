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
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricoResponseSubscriber implements MessageListener {

  private final ConcurrentHashMap<String, Object> cache;
  private final RedisConnectionFactory redisConnectionFactory;

  @PostConstruct
  public void startSubscriberListener() {

    new Thread(() -> {
      var conn = redisConnectionFactory.getConnection();

      conn.subscribe((msg, ch) -> {
            var channel = new String(ch);

            switch (channel) {
              case "historico.agendamento.response" -> {
                var event = (HistoricoAgendamentoResponseEvent) deserializeObject(msg.getBody());
                cache.put(event.getCorrelationId() + ":ag", event);
              }

              case "historico.procedimento.response" -> {
                var event = (HistoricoProcedimentoResponseEvent) deserializeObject(msg.getBody());
                cache.put(event.getCorrelationId() + ":proc", event);
              }

              case "historico.estoque.response" -> {
                var event = (HistoricoEstoqueResponseEvent) deserializeObject(msg.getBody());
                cache.put(event.getCorrelationId() + ":est", event);
              }

              case "historico.pagamento.response" -> {
                var event = (HistoricoPagamentoResponseEvent) deserializeObject(msg.getBody());
                cache.put(event.getCorrelationId() + ":pag", event);
              }
            }

          },
          "historico.agendamento.response".getBytes(),
          "historico.procedimento.response".getBytes(),
          "historico.estoque.response".getBytes(),
          "historico.pagamento.response".getBytes()
      );

    }).start();
  }

  private Object deserializeObject(byte[] bytes) {
    try (var bis = new ByteArrayInputStream(bytes);
        var ois = new ObjectInputStream(bis)) {
      return ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException("Falha ao desserializar objeto do Redis", e);
    }
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {

  }
}
