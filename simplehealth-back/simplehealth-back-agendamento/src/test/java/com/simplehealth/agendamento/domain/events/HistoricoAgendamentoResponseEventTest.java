package com.simplehealth.agendamento.domain.events;

import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoAgendamentoResponseEventTest {

  @Test
  void testCriarEventoComBuilder() {
    List<AgendamentoDTO> agendamentos = new ArrayList<>();

    HistoricoAgendamentoResponseEvent event = HistoricoAgendamentoResponseEvent.builder()
        .correlationId("corr-123")
        .agendamentos(agendamentos)
        .build();

    assertNotNull(event);
    assertEquals("corr-123", event.getCorrelationId());
    assertEquals(agendamentos, event.getAgendamentos());
  }

  @Test
  void testCriarEventoComConstructor() {
    List<AgendamentoDTO> agendamentos = new ArrayList<>();

    HistoricoAgendamentoResponseEvent event = new HistoricoAgendamentoResponseEvent(
        "corr-456", agendamentos);

    assertNotNull(event);
    assertEquals("corr-456", event.getCorrelationId());
    assertEquals(agendamentos, event.getAgendamentos());
  }

  @Test
  void testCriarEventoVazio() {
    HistoricoAgendamentoResponseEvent event = new HistoricoAgendamentoResponseEvent();

    assertNotNull(event);
    assertNull(event.getCorrelationId());
    assertNull(event.getAgendamentos());
  }

  @Test
  void testSettersAndGetters() {
    HistoricoAgendamentoResponseEvent event = new HistoricoAgendamentoResponseEvent();
    List<AgendamentoDTO> agendamentos = new ArrayList<>();

    event.setCorrelationId("corr-789");
    event.setAgendamentos(agendamentos);

    assertEquals("corr-789", event.getCorrelationId());
    assertEquals(agendamentos, event.getAgendamentos());
  }

  @Test
  void testEventoComListaPreenchida() {
    AgendamentoDTO dto = new AgendamentoDTO();
    List<AgendamentoDTO> agendamentos = List.of(dto);

    HistoricoAgendamentoResponseEvent event = HistoricoAgendamentoResponseEvent.builder()
        .correlationId("corr-999")
        .agendamentos(agendamentos)
        .build();

    assertEquals(1, event.getAgendamentos().size());
    assertSame(dto, event.getAgendamentos().get(0));
  }
}

