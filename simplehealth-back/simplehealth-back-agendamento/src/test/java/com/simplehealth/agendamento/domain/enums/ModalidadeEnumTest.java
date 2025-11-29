package com.simplehealth.agendamento.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModalidadeEnumTest {

  @Test
  void testEnumValues() {
    assertEquals(2, ModalidadeEnum.values().length);
  }

  @Test
  void testPresencial() {
    ModalidadeEnum modalidade = ModalidadeEnum.PRESENCIAL;
    assertEquals("PRESENCIAL", modalidade.name());
  }

  @Test
  void testRemota() {
    ModalidadeEnum modalidade = ModalidadeEnum.REMOTA;
    assertEquals("REMOTA", modalidade.name());
  }

  @Test
  void testValueOf() {
    assertEquals(ModalidadeEnum.PRESENCIAL, ModalidadeEnum.valueOf("PRESENCIAL"));
    assertEquals(ModalidadeEnum.REMOTA, ModalidadeEnum.valueOf("REMOTA"));
  }

  @Test
  void testValueOfInvalido() {
    assertThrows(IllegalArgumentException.class, () -> {
      ModalidadeEnum.valueOf("HIBRIDA");
    });
  }

  @Test
  void testEnumComparacao() {
    assertNotEquals(ModalidadeEnum.PRESENCIAL, ModalidadeEnum.REMOTA);
    assertEquals(ModalidadeEnum.PRESENCIAL, ModalidadeEnum.PRESENCIAL);
  }

  @Test
  void testEnumOrdem() {
    ModalidadeEnum[] values = ModalidadeEnum.values();
    assertEquals(ModalidadeEnum.PRESENCIAL, values[0]);
    assertEquals(ModalidadeEnum.REMOTA, values[1]);
  }

  @Test
  void testToString() {
    assertEquals("PRESENCIAL", ModalidadeEnum.PRESENCIAL.toString());
    assertEquals("REMOTA", ModalidadeEnum.REMOTA.toString());
  }
}

