package com.simplehealth.agendamento.application.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BloqueioAgendaDTOTest {

  private BloqueioAgendaDTO dto;

  @BeforeEach
  void setUp() {
    dto = new BloqueioAgendaDTO();
  }

  @Test
  void testCriarDTOVazio() {
    assertNotNull(dto);
    assertNull(dto.getDataInicio());
    assertNull(dto.getDataFim());
    assertNull(dto.getMotivo());
    assertNull(dto.getAntecedenciaMinima());
    assertNull(dto.getMedicoCrm());
    assertNull(dto.getUsuarioCriadorLogin());
  }

  @Test
  void testSettersAndGetters() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 8, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 18, 0);

    dto.setDataInicio(inicio);
    dto.setDataFim(fim);
    dto.setMotivo("Férias");
    dto.setAntecedenciaMinima(24);
    dto.setMedicoCrm("CRM123456");
    dto.setUsuarioCriadorLogin("admin");

    assertEquals(inicio, dto.getDataInicio());
    assertEquals(fim, dto.getDataFim());
    assertEquals("Férias", dto.getMotivo());
    assertEquals(24, dto.getAntecedenciaMinima());
    assertEquals("CRM123456", dto.getMedicoCrm());
    assertEquals("admin", dto.getUsuarioCriadorLogin());
  }

  @Test
  void testCriarDTOComBuilder() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 10, 9, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 10, 12, 0);

    BloqueioAgendaDTO dtoBuilder = BloqueioAgendaDTO.builder()
        .dataInicio(inicio)
        .dataFim(fim)
        .motivo("Congresso médico")
        .antecedenciaMinima(48)
        .medicoCrm("CRM789012")
        .usuarioCriadorLogin("secretaria")
        .build();

    assertEquals(inicio, dtoBuilder.getDataInicio());
    assertEquals(fim, dtoBuilder.getDataFim());
    assertEquals("Congresso médico", dtoBuilder.getMotivo());
    assertEquals(48, dtoBuilder.getAntecedenciaMinima());
    assertEquals("CRM789012", dtoBuilder.getMedicoCrm());
    assertEquals("secretaria", dtoBuilder.getUsuarioCriadorLogin());
  }

  @Test
  void testCriarDTOComConstructor() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 15, 14, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 15, 17, 0);

    BloqueioAgendaDTO dtoConstructor = new BloqueioAgendaDTO(
        inicio, fim, "Reunião administrativa", 12,
        "CRM111111", "medico.silva"
    );

    assertEquals(inicio, dtoConstructor.getDataInicio());
    assertEquals(fim, dtoConstructor.getDataFim());
    assertEquals("Reunião administrativa", dtoConstructor.getMotivo());
    assertEquals(12, dtoConstructor.getAntecedenciaMinima());
    assertEquals("CRM111111", dtoConstructor.getMedicoCrm());
    assertEquals("medico.silva", dtoConstructor.getUsuarioCriadorLogin());
  }

  @Test
  void testMotivosBloqueio() {
    dto.setMotivo("Férias");
    assertEquals("Férias", dto.getMotivo());

    dto.setMotivo("Licença médica");
    assertEquals("Licença médica", dto.getMotivo());

    dto.setMotivo("Treinamento");
    assertEquals("Treinamento", dto.getMotivo());

    dto.setMotivo("Evento científico");
    assertEquals("Evento científico", dto.getMotivo());
  }

  @Test
  void testAntecedenciaMinima() {
    dto.setAntecedenciaMinima(24);
    assertEquals(24, dto.getAntecedenciaMinima());

    dto.setAntecedenciaMinima(48);
    assertEquals(48, dto.getAntecedenciaMinima());

    dto.setAntecedenciaMinima(72);
    assertEquals(72, dto.getAntecedenciaMinima());
  }

  @Test
  void testPeriodoBloqueio() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 20, 8, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 25, 18, 0);

    dto.setDataInicio(inicio);
    dto.setDataFim(fim);

    assertTrue(dto.getDataFim().isAfter(dto.getDataInicio()));
    assertEquals(5, dto.getDataFim().getDayOfMonth() - dto.getDataInicio().getDayOfMonth());
  }

  @Test
  void testBloqueioComDiferentesMedicos() {
    dto.setMedicoCrm("CRM123456");
    assertEquals("CRM123456", dto.getMedicoCrm());

    dto.setMedicoCrm("CRM789012");
    assertEquals("CRM789012", dto.getMedicoCrm());
  }

  @Test
  void testBloqueioComDiferentesUsuarios() {
    dto.setUsuarioCriadorLogin("admin");
    assertEquals("admin", dto.getUsuarioCriadorLogin());

    dto.setUsuarioCriadorLogin("secretaria.principal");
    assertEquals("secretaria.principal", dto.getUsuarioCriadorLogin());
  }
}

