package com.simplehealth.cadastro.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvenioTest {

  @Test
  void testConvenioCreation() {
    Convenio convenio = new Convenio();
    convenio.setId(1L);
    convenio.setNome("Unimed");
    convenio.setPlano("Básico");
    convenio.setAtivo(true);

    assertEquals(1L, convenio.getId());
    assertEquals("Unimed", convenio.getNome());
    assertEquals("Básico", convenio.getPlano());
    assertTrue(convenio.getAtivo());
  }

  @Test
  void testConvenioBuilder() {
    Convenio convenio = Convenio.builder()
        .id(2L)
        .nome("Bradesco Saúde")
        .plano("Premium")
        .ativo(true)
        .build();

    assertNotNull(convenio);
    assertEquals(2L, convenio.getId());
    assertEquals("Bradesco Saúde", convenio.getNome());
    assertEquals("Premium", convenio.getPlano());
    assertTrue(convenio.getAtivo());
  }

  @Test
  void testConvenioNoArgsConstructor() {
    Convenio convenio = new Convenio();
    assertNotNull(convenio);
    assertNull(convenio.getId());
    assertNull(convenio.getNome());
    assertNull(convenio.getPlano());
    assertNull(convenio.getAtivo());
  }

  @Test
  void testConvenioAllArgsConstructor() {
    Convenio convenio = new Convenio(3L, "SulAmérica", "Top", false);

    assertNotNull(convenio);
    assertEquals(3L, convenio.getId());
    assertEquals("SulAmérica", convenio.getNome());
    assertEquals("Top", convenio.getPlano());
    assertFalse(convenio.getAtivo());
  }

  @Test
  void testConvenioInativo() {
    Convenio convenio = Convenio.builder()
        .nome("Convênio Teste")
        .plano("Teste")
        .ativo(false)
        .build();

    assertFalse(convenio.getAtivo());
  }
}

