package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgendamentoTest {

  @Test
  void testAgendamentoCreationWithConsulta() {
    Consulta consulta = new Consulta();
    consulta.setId("123");
    consulta.setDataHoraInicio(LocalDateTime.of(2025, 12, 1, 10, 0));
    consulta.setDataHoraFim(LocalDateTime.of(2025, 12, 1, 11, 0));
    consulta.setIsEncaixe(false);
    consulta.setModalidade(ModalidadeEnum.PRESENCIAL);
    consulta.setObservacoes("Consulta de rotina");
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setConvenioNome("Unimed");
    consulta.setUsuarioCriadorLogin("admin");

    assertNotNull(consulta);
    assertEquals("123", consulta.getId());
    assertEquals(LocalDateTime.of(2025, 12, 1, 10, 0), consulta.getDataHoraInicio());
    assertEquals(LocalDateTime.of(2025, 12, 1, 11, 0), consulta.getDataHoraFim());
    assertFalse(consulta.getIsEncaixe());
    assertEquals(ModalidadeEnum.PRESENCIAL, consulta.getModalidade());
    assertEquals("Consulta de rotina", consulta.getObservacoes());
    assertEquals(StatusAgendamentoEnum.ATIVO, consulta.getStatus());
    assertEquals("12345678900", consulta.getPacienteCpf());
    assertEquals("CRM123456", consulta.getMedicoCrm());
    assertEquals("Unimed", consulta.getConvenioNome());
    assertEquals("admin", consulta.getUsuarioCriadorLogin());
  }

  @Test
  void testAgendamentoWithEncaixe() {
    Consulta consulta = new Consulta();
    consulta.setIsEncaixe(true);
    consulta.setMotivoEncaixe("Urgência");

    assertTrue(consulta.getIsEncaixe());
    assertEquals("Urgência", consulta.getMotivoEncaixe());
  }

  @Test
  void testAgendamentoCancelamento() {
    Consulta consulta = new Consulta();
    consulta.setStatus(StatusAgendamentoEnum.CANCELADO);
    consulta.setMotivoCancelamento("Paciente desistiu");
    consulta.setDataCancelamento(LocalDateTime.of(2025, 11, 30, 15, 0));
    consulta.setUsuarioCanceladorLogin("admin");

    assertEquals(StatusAgendamentoEnum.CANCELADO, consulta.getStatus());
    assertEquals("Paciente desistiu", consulta.getMotivoCancelamento());
    assertEquals(LocalDateTime.of(2025, 11, 30, 15, 0), consulta.getDataCancelamento());
    assertEquals("admin", consulta.getUsuarioCanceladorLogin());
  }

  @Test
  void testAgendamentoDefaultValues() {
    Consulta consulta = new Consulta();

    assertFalse(consulta.getIsEncaixe());
    assertEquals(StatusAgendamentoEnum.ATIVO, consulta.getStatus());
  }

  @Test
  void testAgendamentoModalidades() {
    Consulta presencial = new Consulta();
    presencial.setModalidade(ModalidadeEnum.PRESENCIAL);

    Consulta remota = new Consulta();
    remota.setModalidade(ModalidadeEnum.REMOTA);

    assertEquals(ModalidadeEnum.PRESENCIAL, presencial.getModalidade());
    assertEquals(ModalidadeEnum.REMOTA, remota.getModalidade());
  }

  @Test
  void testAgendamentoAllStatus() {
    Consulta ativo = new Consulta();
    ativo.setStatus(StatusAgendamentoEnum.ATIVO);

    Consulta realizado = new Consulta();
    realizado.setStatus(StatusAgendamentoEnum.REALIZADO);

    Consulta cancelado = new Consulta();
    cancelado.setStatus(StatusAgendamentoEnum.CANCELADO);

    Consulta naoCompareceu = new Consulta();
    naoCompareceu.setStatus(StatusAgendamentoEnum.NAO_COMPARECEU);

    assertEquals(StatusAgendamentoEnum.ATIVO, ativo.getStatus());
    assertEquals(StatusAgendamentoEnum.REALIZADO, realizado.getStatus());
    assertEquals(StatusAgendamentoEnum.CANCELADO, cancelado.getStatus());
    assertEquals(StatusAgendamentoEnum.NAO_COMPARECEU, naoCompareceu.getStatus());
  }
}

