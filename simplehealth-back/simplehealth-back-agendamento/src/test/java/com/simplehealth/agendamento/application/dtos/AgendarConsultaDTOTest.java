package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgendarConsultaDTOTest {

  @Test
  void testCriarDTOComBuilder() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 11, 0);

    AgendarConsultaDTO dto = AgendarConsultaDTO.builder()
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .modalidade(ModalidadeEnum.PRESENCIAL)
        .especialidade("Cardiologia")
        .tipoConsulta(TipoConsultaEnum.PRIMEIRA)
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .convenioNome("Unimed")
        .usuarioCriadorLogin("admin")
        .observacoes("Paciente relatou dores")
        .build();

    assertNotNull(dto);
    assertEquals(inicio, dto.getDataHoraInicio());
    assertEquals(fim, dto.getDataHoraFim());
    assertEquals(ModalidadeEnum.PRESENCIAL, dto.getModalidade());
    assertEquals("Cardiologia", dto.getEspecialidade());
    assertEquals(TipoConsultaEnum.PRIMEIRA, dto.getTipoConsulta());
    assertEquals("12345678900", dto.getPacienteCpf());
    assertEquals("CRM123456", dto.getMedicoCrm());
    assertEquals("Unimed", dto.getConvenioNome());
    assertEquals("admin", dto.getUsuarioCriadorLogin());
    assertEquals("Paciente relatou dores", dto.getObservacoes());
  }

  @Test
  void testCriarDTOVazio() {
    AgendarConsultaDTO dto = new AgendarConsultaDTO();

    assertNotNull(dto);
    assertNull(dto.getDataHoraInicio());
    assertNull(dto.getDataHoraFim());
    assertNull(dto.getModalidade());
    assertNull(dto.getEspecialidade());
    assertNull(dto.getTipoConsulta());
    assertNull(dto.getPacienteCpf());
    assertNull(dto.getMedicoCrm());
  }

  @Test
  void testSettersAndGetters() {
    AgendarConsultaDTO dto = new AgendarConsultaDTO();
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 14, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 15, 0);

    dto.setDataHoraInicio(inicio);
    dto.setDataHoraFim(fim);
    dto.setModalidade(ModalidadeEnum.REMOTA);
    dto.setEspecialidade("Dermatologia");
    dto.setTipoConsulta(TipoConsultaEnum.RETORNO);
    dto.setPacienteCpf("98765432100");
    dto.setMedicoCrm("CRM789012");
    dto.setConvenioNome("Bradesco");
    dto.setUsuarioCriadorLogin("user");
    dto.setObservacoes("Retorno para avaliação");

    assertEquals(inicio, dto.getDataHoraInicio());
    assertEquals(fim, dto.getDataHoraFim());
    assertEquals(ModalidadeEnum.REMOTA, dto.getModalidade());
    assertEquals("Dermatologia", dto.getEspecialidade());
    assertEquals(TipoConsultaEnum.RETORNO, dto.getTipoConsulta());
    assertEquals("98765432100", dto.getPacienteCpf());
    assertEquals("CRM789012", dto.getMedicoCrm());
    assertEquals("Bradesco", dto.getConvenioNome());
    assertEquals("user", dto.getUsuarioCriadorLogin());
    assertEquals("Retorno para avaliação", dto.getObservacoes());
  }

  @Test
  void testCriarDTOComConstructor() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 16, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 17, 0);

    AgendarConsultaDTO dto = new AgendarConsultaDTO(
        inicio, fim, ModalidadeEnum.PRESENCIAL, "Ortopedia",
        TipoConsultaEnum.PRIMEIRA, "11111111111", "CRM111111",
        "Unimed", "medico1", "Observações gerais"
    );

    assertNotNull(dto);
    assertEquals(inicio, dto.getDataHoraInicio());
    assertEquals(fim, dto.getDataHoraFim());
    assertEquals("Ortopedia", dto.getEspecialidade());
    assertEquals("11111111111", dto.getPacienteCpf());
  }
}

