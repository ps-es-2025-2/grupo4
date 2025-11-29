package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConsultaTest {

  private Consulta consulta;

  @BeforeEach
  void setUp() {
    consulta = new Consulta();
  }

  @Test
  void testCriarConsultaVazia() {
    assertNotNull(consulta);
    assertNull(consulta.getId());
    assertNull(consulta.getEspecialidade());
    assertNull(consulta.getTipoConsulta());
  }

  @Test
  void testSettersAndGetters() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 11, 0);

    consulta.setId("cons123");
    consulta.setDataHoraInicio(inicio);
    consulta.setDataHoraFim(fim);
    consulta.setEspecialidade("Cardiologia");
    consulta.setTipoConsulta(TipoConsultaEnum.PRIMEIRA);
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
    consulta.setModalidade(ModalidadeEnum.PRESENCIAL);

    assertEquals("cons123", consulta.getId());
    assertEquals(inicio, consulta.getDataHoraInicio());
    assertEquals(fim, consulta.getDataHoraFim());
    assertEquals("Cardiologia", consulta.getEspecialidade());
    assertEquals(TipoConsultaEnum.PRIMEIRA, consulta.getTipoConsulta());
    assertEquals("12345678900", consulta.getPacienteCpf());
    assertEquals("CRM123456", consulta.getMedicoCrm());
    assertEquals(StatusAgendamentoEnum.ATIVO, consulta.getStatus());
    assertEquals(ModalidadeEnum.PRESENCIAL, consulta.getModalidade());
  }

  @Test
  void testConsultaComBuilder() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 14, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 15, 0);

    Consulta consultaBuilder = Consulta.builder()
        .especialidade("Dermatologia")
        .tipoConsulta(TipoConsultaEnum.RETORNO)
        .build();

    consultaBuilder.setId("cons456");
    consultaBuilder.setDataHoraInicio(inicio);
    consultaBuilder.setDataHoraFim(fim);

    assertEquals("cons456", consultaBuilder.getId());
    assertEquals("Dermatologia", consultaBuilder.getEspecialidade());
    assertEquals(TipoConsultaEnum.RETORNO, consultaBuilder.getTipoConsulta());
  }

  @Test
  void testConsultaHerdaDeAgendamento() {
    assertTrue(consulta instanceof Agendamento);
  }

  @Test
  void testConsultaComEncaixe() {
    consulta.setIsEncaixe(true);
    consulta.setMotivoEncaixe("Urgência médica");

    assertTrue(consulta.getIsEncaixe());
    assertEquals("Urgência médica", consulta.getMotivoEncaixe());
  }

  @Test
  void testConsultaCancelada() {
    LocalDateTime dataCancelamento = LocalDateTime.now();

    consulta.setStatus(StatusAgendamentoEnum.CANCELADO);
    consulta.setMotivoCancelamento("Paciente desistiu");
    consulta.setDataCancelamento(dataCancelamento);
    consulta.setUsuarioCanceladorLogin("admin");

    assertEquals(StatusAgendamentoEnum.CANCELADO, consulta.getStatus());
    assertEquals("Paciente desistiu", consulta.getMotivoCancelamento());
    assertEquals(dataCancelamento, consulta.getDataCancelamento());
    assertEquals("admin", consulta.getUsuarioCanceladorLogin());
  }

  @Test
  void testConsultaComConvenio() {
    consulta.setConvenioNome("Unimed");
    assertEquals("Unimed", consulta.getConvenioNome());
  }

  @Test
  void testConsultaComObservacoes() {
    consulta.setObservacoes("Paciente relatou dores no peito");
    assertEquals("Paciente relatou dores no peito", consulta.getObservacoes());
  }

  @Test
  void testTiposDeConsulta() {
    consulta.setTipoConsulta(TipoConsultaEnum.PRIMEIRA);
    assertEquals(TipoConsultaEnum.PRIMEIRA, consulta.getTipoConsulta());

    consulta.setTipoConsulta(TipoConsultaEnum.RETORNO);
    assertEquals(TipoConsultaEnum.RETORNO, consulta.getTipoConsulta());

    consulta.setTipoConsulta(TipoConsultaEnum.ROTINA);
    assertEquals(TipoConsultaEnum.ROTINA, consulta.getTipoConsulta());
  }
}

