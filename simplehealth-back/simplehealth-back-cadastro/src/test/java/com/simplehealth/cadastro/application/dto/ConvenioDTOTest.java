package com.simplehealth.cadastro.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvenioDTOTest {

  @Test
  void testConvenioDTOCreation() {
    ConvenioDTO dto = new ConvenioDTO();
    dto.setId(1L);
    dto.setNome("Unimed");
    dto.setPlano("Básico");
    dto.setAtivo(true);

    assertEquals(1L, dto.getId());
    assertEquals("Unimed", dto.getNome());
    assertEquals("Básico", dto.getPlano());
    assertTrue(dto.getAtivo());
  }

  @Test
  void testConvenioDTOBuilder() {
    ConvenioDTO dto = ConvenioDTO.builder()
        .id(2L)
        .nome("Bradesco Saúde")
        .plano("Premium")
        .ativo(true)
        .build();

    assertNotNull(dto);
    assertEquals(2L, dto.getId());
    assertEquals("Bradesco Saúde", dto.getNome());
    assertEquals("Premium", dto.getPlano());
    assertTrue(dto.getAtivo());
  }

  @Test
  void testConvenioDTONoArgsConstructor() {
    ConvenioDTO dto = new ConvenioDTO();
    assertNotNull(dto);
    assertNull(dto.getId());
    assertNull(dto.getNome());
    assertNull(dto.getPlano());
    assertNull(dto.getAtivo());
  }

  @Test
  void testConvenioDTOAllArgsConstructor() {
    ConvenioDTO dto = new ConvenioDTO(
        3L,
        "SulAmérica",
        "Top",
        false
    );

    assertEquals(3L, dto.getId());
    assertEquals("SulAmérica", dto.getNome());
    assertEquals("Top", dto.getPlano());
    assertFalse(dto.getAtivo());
  }

  @Test
  void testConvenioDTOInativo() {
    ConvenioDTO dto = ConvenioDTO.builder()
        .nome("Convênio Teste")
        .plano("Teste")
        .ativo(false)
        .build();

    assertFalse(dto.getAtivo());
  }
}

