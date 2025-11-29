package com.simplehealth.agendamento.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusAgendamentoEnumTest {

  @Test
  void testEnumValues() {
    assertEquals(4, StatusAgendamentoEnum.values().length);
  }

  @Test
  void testAtivo() {
    StatusAgendamentoEnum status = StatusAgendamentoEnum.ATIVO;
    assertEquals("ATIVO", status.name());
  }

  @Test
  void testRealizado() {
    StatusAgendamentoEnum status = StatusAgendamentoEnum.REALIZADO;
    assertEquals("REALIZADO", status.name());
  }

  @Test
  void testCancelado() {
    StatusAgendamentoEnum status = StatusAgendamentoEnum.CANCELADO;
    assertEquals("CANCELADO", status.name());
  }

  @Test
  void testNaoCompareceu() {
    StatusAgendamentoEnum status = StatusAgendamentoEnum.NAO_COMPARECEU;
    assertEquals("NAO_COMPARECEU", status.name());
  }

  @Test
  void testValueOf() {
    assertEquals(StatusAgendamentoEnum.ATIVO, StatusAgendamentoEnum.valueOf("ATIVO"));
    assertEquals(StatusAgendamentoEnum.REALIZADO, StatusAgendamentoEnum.valueOf("REALIZADO"));
    assertEquals(StatusAgendamentoEnum.CANCELADO, StatusAgendamentoEnum.valueOf("CANCELADO"));
    assertEquals(StatusAgendamentoEnum.NAO_COMPARECEU, StatusAgendamentoEnum.valueOf("NAO_COMPARECEU"));
  }

  @Test
  void testValueOfInvalido() {
    assertThrows(IllegalArgumentException.class, () -> {
      StatusAgendamentoEnum.valueOf("INVALIDO");
    });
  }

  @Test
  void testEnumComparacao() {
    assertNotEquals(StatusAgendamentoEnum.ATIVO, StatusAgendamentoEnum.CANCELADO);
    assertEquals(StatusAgendamentoEnum.ATIVO, StatusAgendamentoEnum.ATIVO);
  }

  @Test
  void testEnumOrdem() {
    StatusAgendamentoEnum[] values = StatusAgendamentoEnum.values();
    assertEquals(StatusAgendamentoEnum.ATIVO, values[0]);
    assertEquals(StatusAgendamentoEnum.REALIZADO, values[1]);
    assertEquals(StatusAgendamentoEnum.CANCELADO, values[2]);
    assertEquals(StatusAgendamentoEnum.NAO_COMPARECEU, values[3]);
  }
}

