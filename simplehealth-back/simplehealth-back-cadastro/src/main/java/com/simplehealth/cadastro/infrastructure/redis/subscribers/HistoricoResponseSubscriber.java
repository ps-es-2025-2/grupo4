package com.simplehealth.cadastro.infrastructure.redis.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.cadastro.domain.events.HistoricoAgendamentoResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoConsultaResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoExameResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoEstoqueResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoPagamentoResponseEvent;
import com.simplehealth.cadastro.domain.events.HistoricoProcedimentoResponseEvent;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricoResponseSubscriber implements MessageListener {

  private final ObjectMapper mapper;
  private final ConcurrentHashMap<String, Object> cache;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String channel = new String(message.getChannel());
      String payload = new String(message.getBody());
      System.out.println("Mensagem recebida: " + payload);

      switch (channel) {
        case "historico.consulta.response" -> {
          HistoricoConsultaResponseEvent event = mapper.readValue(payload,
              HistoricoConsultaResponseEvent.class);
          if (event != null && event.getCorrelationId() != null) {
            cache.put(event.getCorrelationId() + ":cons", event);
          }
        }
        case "historico.exame.response" -> {
          HistoricoExameResponseEvent event = mapper.readValue(payload,
              HistoricoExameResponseEvent.class);
          if (event != null && event.getCorrelationId() != null) {
            cache.put(event.getCorrelationId() + ":exam", event);
          }
        }
        case "historico.agendamento.response" -> {
          HistoricoAgendamentoResponseEvent responseEvent = mapper.readValue(
              payload,
              HistoricoAgendamentoResponseEvent.class);

          if (responseEvent != null && responseEvent.getCorrelationId() != null) {
            cache.put(responseEvent.getCorrelationId() + ":ag", responseEvent);
          }
        }
        case "historico.procedimento.response" -> {
          HistoricoProcedimentoResponseEvent event = mapper.readValue(payload,
              HistoricoProcedimentoResponseEvent.class);
          cache.put(event.getCorrelationId() + ":proc", event);
        }
        case "historico.estoque.response" -> {
          HistoricoEstoqueResponseEvent event = mapper.readValue(payload, HistoricoEstoqueResponseEvent.class);
          cache.put(event.getCorrelationId() + ":est", event);
        }
        case "historico.pagamento.response" -> {
          HistoricoPagamentoResponseEvent event = mapper.readValue(payload, HistoricoPagamentoResponseEvent.class);
          cache.put(event.getCorrelationId() + ":pag", event);
        }
        case "estoque.alerta.response" -> {
          com.simplehealth.cadastro.domain.events.EstoqueAlertaResponseEvent event = mapper.readValue(
              payload,
              com.simplehealth.cadastro.domain.events.EstoqueAlertaResponseEvent.class);
          cache.put(event.getCorrelationId() + ":alerta", event);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Erro ao processar mensagem do Redis", e);
    }
  }
}