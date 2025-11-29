package com.simplehealth.agendamento.application.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgendamentoDTOTest {

  private AgendamentoDTO dto;

  @BeforeEach
  void setUp() {
    dto = new AgendamentoDTO();
  }

  @Test
  void testCriarDTOVazio() {
    assertNotNull(dto);
    assertNull(dto.getId());
    assertNull(dto.getDataHoraInicio());
    assertNull(dto.getDataHoraFim());
  }

  @Test
  void testSettersAndGetters() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 11, 0);
    LocalDateTime dataCancelamento = LocalDateTime.now();

    dto.setId("agend123");
    dto.setDataHoraInicio(inicio);
    dto.setDataHoraFim(fim);
    dto.setIsEncaixe(true);
    dto.setModalidade("PRESENCIAL");
    dto.setMotivoEncaixe("Urgência");
    dto.setObservacoes("Paciente com febre");
    dto.setStatus("ATIVO");
    dto.setMotivoCancelamento(null);
    dto.setDataCancelamento(dataCancelamento);
    dto.setPacienteCpf("12345678900");
    dto.setMedicoCrm("CRM123456");
    dto.setConvenioNome("Unimed");
    dto.setUsuarioCriadorLogin("admin");
    dto.setUsuarioCanceladorLogin(null);

    assertEquals("agend123", dto.getId());
    assertEquals(inicio, dto.getDataHoraInicio());
    assertEquals(fim, dto.getDataHoraFim());
    assertTrue(dto.getIsEncaixe());
    assertEquals("PRESENCIAL", dto.getModalidade());
    assertEquals("Urgência", dto.getMotivoEncaixe());
    assertEquals("Paciente com febre", dto.getObservacoes());
    assertEquals("ATIVO", dto.getStatus());
    assertNull(dto.getMotivoCancelamento());
    assertEquals(dataCancelamento, dto.getDataCancelamento());
    assertEquals("12345678900", dto.getPacienteCpf());
    assertEquals("CRM123456", dto.getMedicoCrm());
    assertEquals("Unimed", dto.getConvenioNome());
    assertEquals("admin", dto.getUsuarioCriadorLogin());
    assertNull(dto.getUsuarioCanceladorLogin());
  }

  @Test
  void testCriarDTOComBuilder() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 14, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 15, 0);

    AgendamentoDTO dtoBuilder = AgendamentoDTO.builder()
        .id("agend456")
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .pacienteCpf("98765432100")
        .medicoCrm("CRM789012")
        .status("REALIZADO")
        .isEncaixe(false)
        .modalidade("REMOTA")
        .build();

    assertEquals("agend456", dtoBuilder.getId());
    assertEquals(inicio, dtoBuilder.getDataHoraInicio());
    assertEquals(fim, dtoBuilder.getDataHoraFim());
    assertEquals("98765432100", dtoBuilder.getPacienteCpf());
    assertEquals("CRM789012", dtoBuilder.getMedicoCrm());
    assertEquals("REALIZADO", dtoBuilder.getStatus());
    assertFalse(dtoBuilder.getIsEncaixe());
    assertEquals("REMOTA", dtoBuilder.getModalidade());
  }

  @Test
  void testDTOComEncaixe() {
    dto.setIsEncaixe(true);
    dto.setMotivoEncaixe("Paciente em situação de emergência");

    assertTrue(dto.getIsEncaixe());
    assertEquals("Paciente em situação de emergência", dto.getMotivoEncaixe());
  }

  @Test
  void testDTOSemEncaixe() {
    dto.setIsEncaixe(false);
    dto.setMotivoEncaixe(null);

    assertFalse(dto.getIsEncaixe());
    assertNull(dto.getMotivoEncaixe());
  }

  @Test
  void testDTOCancelado() {
    LocalDateTime dataCancelamento = LocalDateTime.now();

    dto.setStatus("CANCELADO");
    dto.setMotivoCancelamento("Paciente desistiu");
    dto.setDataCancelamento(dataCancelamento);
    dto.setUsuarioCanceladorLogin("recepcionista");

    assertEquals("CANCELADO", dto.getStatus());
    assertEquals("Paciente desistiu", dto.getMotivoCancelamento());
    assertEquals(dataCancelamento, dto.getDataCancelamento());
    assertEquals("recepcionista", dto.getUsuarioCanceladorLogin());
  }

  @Test
  void testCriarDTOComConstructor() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 16, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 17, 0);

    AgendamentoDTO dtoConstructor = new AgendamentoDTO(
        "agend789", inicio, fim, true, "PRESENCIAL",
        "Encaixe de emergência", "Paciente idoso", "ATIVO",
        null, null, "11111111111", "CRM111111",
        "Bradesco", "medico1", null
    );

    assertEquals("agend789", dtoConstructor.getId());
    assertEquals(inicio, dtoConstructor.getDataHoraInicio());
    assertEquals(fim, dtoConstructor.getDataHoraFim());
    assertTrue(dtoConstructor.getIsEncaixe());
    assertEquals("11111111111", dtoConstructor.getPacienteCpf());
  }
}

