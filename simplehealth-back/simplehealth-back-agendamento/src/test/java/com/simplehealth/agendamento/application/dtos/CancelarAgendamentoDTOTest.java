package com.simplehealth.agendamento.application.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CancelarAgendamentoDTOTest {

  private CancelarAgendamentoDTO dto;

  @BeforeEach
  void setUp() {
    dto = new CancelarAgendamentoDTO();
  }

  @Test
  void testCriarDTOVazio() {
    assertNotNull(dto);
    assertNull(dto.getId());
    assertNull(dto.getMotivo());
    assertNull(dto.getUsuarioLogin());
    assertNull(dto.getDataHoraCancelamento());
  }

  @Test
  void testSettersAndGetters() {
    LocalDateTime dataCancelamento = LocalDateTime.of(2025, 11, 29, 10, 30);

    dto.setId("agend123");
    dto.setMotivo("Paciente desistiu da consulta");
    dto.setUsuarioLogin("admin");
    dto.setDataHoraCancelamento(dataCancelamento);

    assertEquals("agend123", dto.getId());
    assertEquals("Paciente desistiu da consulta", dto.getMotivo());
    assertEquals("admin", dto.getUsuarioLogin());
    assertEquals(dataCancelamento, dto.getDataHoraCancelamento());
  }

  @Test
  void testCriarDTOComBuilder() {
    LocalDateTime dataCancelamento = LocalDateTime.of(2025, 12, 1, 14, 0);

    CancelarAgendamentoDTO dtoBuilder = CancelarAgendamentoDTO.builder()
        .id("agend456")
        .motivo("Médico cancelou")
        .usuarioLogin("recepcionista")
        .dataHoraCancelamento(dataCancelamento)
        .build();

    assertEquals("agend456", dtoBuilder.getId());
    assertEquals("Médico cancelou", dtoBuilder.getMotivo());
    assertEquals("recepcionista", dtoBuilder.getUsuarioLogin());
    assertEquals(dataCancelamento, dtoBuilder.getDataHoraCancelamento());
  }

  @Test
  void testCriarDTOComConstructor() {
    LocalDateTime dataCancelamento = LocalDateTime.now();

    CancelarAgendamentoDTO dtoConstructor = new CancelarAgendamentoDTO(
        "agend789",
        "Conflito de horário",
        "secretaria",
        dataCancelamento
    );

    assertEquals("agend789", dtoConstructor.getId());
    assertEquals("Conflito de horário", dtoConstructor.getMotivo());
    assertEquals("secretaria", dtoConstructor.getUsuarioLogin());
    assertEquals(dataCancelamento, dtoConstructor.getDataHoraCancelamento());
  }

  @Test
  void testMotivosCancelamento() {
    dto.setMotivo("Paciente não compareceu");
    assertEquals("Paciente não compareceu", dto.getMotivo());

    dto.setMotivo("Emergência hospitalar");
    assertEquals("Emergência hospitalar", dto.getMotivo());

    dto.setMotivo("Remarcação solicitada");
    assertEquals("Remarcação solicitada", dto.getMotivo());
  }

  @Test
  void testDiferentesUsuarios() {
    dto.setUsuarioLogin("medico.silva");
    assertEquals("medico.silva", dto.getUsuarioLogin());

    dto.setUsuarioLogin("recep.maria");
    assertEquals("recep.maria", dto.getUsuarioLogin());

    dto.setUsuarioLogin("admin.sistema");
    assertEquals("admin.sistema", dto.getUsuarioLogin());
  }

  @Test
  void testDataHoraCancelamento() {
    LocalDateTime agora = LocalDateTime.now();
    dto.setDataHoraCancelamento(agora);

    assertEquals(agora, dto.getDataHoraCancelamento());
    assertTrue(dto.getDataHoraCancelamento().isBefore(LocalDateTime.now().plusSeconds(1)));
  }
}

