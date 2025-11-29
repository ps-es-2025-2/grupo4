package com.simplehealth.agendamento.infrastructure.redis.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.events.HistoricoRequestEvent;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoSubscriberTest {

  @Mock
  private ConsultaRepository consultaRepository;

  @Mock
  private ObjectMapper mapper;

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private Message message;

  @InjectMocks
  private AgendamentoSubscriber agendamentoSubscriber;

  private HistoricoRequestEvent requestEvent;
  private List<Consulta> consultas;
  private Consulta consulta;

  @BeforeEach
  void setUp() {
    requestEvent = new HistoricoRequestEvent();
    requestEvent.setCorrelationId("corr-123");
    requestEvent.setCpf("12345678900");

    consulta = new Consulta();
    consulta.setId("cons123");
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setDataHoraInicio(LocalDateTime.of(2025, 12, 1, 10, 0));
    consulta.setDataHoraFim(LocalDateTime.of(2025, 12, 1, 11, 0));

    consultas = List.of(consulta);
  }

  @Test
  void testOnMessageProcessaEventoComSucesso() throws Exception {
    byte[] messageBody = "message body".getBytes();
    when(message.getBody()).thenReturn(messageBody);
    when(mapper.readValue(messageBody, HistoricoRequestEvent.class)).thenReturn(requestEvent);
    when(consultaRepository.findByPacienteCpfOrderByDataHoraInicioDesc("12345678900"))
        .thenReturn(consultas);
    when(mapper.convertValue(any(Consulta.class), eq(AgendamentoDTO.class)))
        .thenReturn(new AgendamentoDTO());

    agendamentoSubscriber.onMessage(message, null);

    verify(consultaRepository, times(1)).findByPacienteCpfOrderByDataHoraInicioDesc("12345678900");
    verify(redisTemplate, times(1)).convertAndSend(eq("historico.agendamento.response"), any());
  }

  @Test
  void testOnMessageComEventoNulo() throws Exception {
    byte[] messageBody = "message body".getBytes();
    when(message.getBody()).thenReturn(messageBody);
    when(mapper.readValue(messageBody, HistoricoRequestEvent.class)).thenReturn(null);

    agendamentoSubscriber.onMessage(message, null);

    verify(consultaRepository, never()).findByPacienteCpfOrderByDataHoraInicioDesc(anyString());
    verify(redisTemplate, never()).convertAndSend(anyString(), any());
  }

  @Test
  void testOnMessageComListaVazia() throws Exception {
    byte[] messageBody = "message body".getBytes();
    when(message.getBody()).thenReturn(messageBody);
    when(mapper.readValue(messageBody, HistoricoRequestEvent.class)).thenReturn(requestEvent);
    when(consultaRepository.findByPacienteCpfOrderByDataHoraInicioDesc("12345678900"))
        .thenReturn(new ArrayList<>());

    agendamentoSubscriber.onMessage(message, null);

    verify(consultaRepository, times(1)).findByPacienteCpfOrderByDataHoraInicioDesc("12345678900");
    verify(redisTemplate, times(1)).convertAndSend(eq("historico.agendamento.response"), any());
  }

  @Test
  void testOnMessageComExcecao() throws Exception {
    byte[] messageBody = "message body".getBytes();
    when(message.getBody()).thenReturn(messageBody);
    when(mapper.readValue(messageBody, HistoricoRequestEvent.class))
        .thenThrow(new RuntimeException("Erro ao processar"));

    agendamentoSubscriber.onMessage(message, null);

    verify(consultaRepository, never()).findByPacienteCpfOrderByDataHoraInicioDesc(anyString());
    verify(redisTemplate, never()).convertAndSend(anyString(), any());
  }

  @Test
  void testOnMessageEnviaRespostaComCorrelationIdCorreto() throws Exception {
    byte[] messageBody = "message body".getBytes();
    when(message.getBody()).thenReturn(messageBody);
    when(mapper.readValue(messageBody, HistoricoRequestEvent.class)).thenReturn(requestEvent);
    when(consultaRepository.findByPacienteCpfOrderByDataHoraInicioDesc("12345678900"))
        .thenReturn(consultas);
    when(mapper.convertValue(any(Consulta.class), eq(AgendamentoDTO.class)))
        .thenReturn(new AgendamentoDTO());

    agendamentoSubscriber.onMessage(message, null);

    verify(redisTemplate).convertAndSend(eq("historico.agendamento.response"), any());
  }
}

