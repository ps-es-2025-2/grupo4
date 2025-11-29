package com.simplehealth.agendamento.domain.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoRequestEventTest {

  @Test
  void testCriarEventoComConstructor() {
    HistoricoRequestEvent event = new HistoricoRequestEvent("corr-123", "12345678900");

    assertNotNull(event);
    assertEquals("corr-123", event.getCorrelationId());
    assertEquals("12345678900", event.getCpf());
  }

  @Test
  void testCriarEventoVazio() {
    HistoricoRequestEvent event = new HistoricoRequestEvent();

    assertNotNull(event);
    assertNull(event.getCorrelationId());
    assertNull(event.getCpf());
  }

  @Test
  void testSettersAndGetters() {
    HistoricoRequestEvent event = new HistoricoRequestEvent();

    event.setCorrelationId("corr-456");
    event.setCpf("98765432100");

    assertEquals("corr-456", event.getCorrelationId());
    assertEquals("98765432100", event.getCpf());
  }

  @Test
  void testAlterarValores() {
    HistoricoRequestEvent event = new HistoricoRequestEvent("corr-789", "11111111111");

    event.setCorrelationId("novo-corr");
    event.setCpf("22222222222");

    assertEquals("novo-corr", event.getCorrelationId());
    assertEquals("22222222222", event.getCpf());
  }

  @Test
  void testEventoComValoresNulos() {
    HistoricoRequestEvent event = new HistoricoRequestEvent(null, null);

    assertNull(event.getCorrelationId());
    assertNull(event.getCpf());
  }
}

