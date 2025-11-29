package com.simplehealth.agendamento.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoConsultaEnumTest {

  @Test
  void testEnumValues() {
    TipoConsultaEnum[] values = TipoConsultaEnum.values();

    assertEquals(3, values.length);
    assertTrue(containsValue(values, TipoConsultaEnum.PRIMEIRA));
    assertTrue(containsValue(values, TipoConsultaEnum.RETORNO));
    assertTrue(containsValue(values, TipoConsultaEnum.ROTINA));
  }

  @Test
  void testEnumValueOf() {
    assertEquals(TipoConsultaEnum.PRIMEIRA, TipoConsultaEnum.valueOf("PRIMEIRA"));
    assertEquals(TipoConsultaEnum.RETORNO, TipoConsultaEnum.valueOf("RETORNO"));
    assertEquals(TipoConsultaEnum.ROTINA, TipoConsultaEnum.valueOf("ROTINA"));
  }

  @Test
  void testEnumName() {
    assertEquals("PRIMEIRA", TipoConsultaEnum.PRIMEIRA.name());
    assertEquals("RETORNO", TipoConsultaEnum.RETORNO.name());
    assertEquals("ROTINA", TipoConsultaEnum.ROTINA.name());
  }

  private boolean containsValue(TipoConsultaEnum[] values, TipoConsultaEnum value) {
    for (TipoConsultaEnum v : values) {
      if (v == value) {
        return true;
      }
    }
    return false;
  }
}

