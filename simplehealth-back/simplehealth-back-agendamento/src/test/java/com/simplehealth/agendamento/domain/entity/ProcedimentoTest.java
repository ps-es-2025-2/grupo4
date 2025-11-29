package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProcedimentoTest {

  @Test
  void testProcedimentoCreationWithBuilder() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 10, 14, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 10, 16, 0);

    Procedimento procedimento = Procedimento.builder()
        .descricaoProcedimento("Cirurgia de Apendicite")
        .salaEquipamentoNecessario("Sala Cirúrgica 2")
        .nivelRisco("Alto")
        .build();

    procedimento.setDataHoraInicio(inicio);
    procedimento.setDataHoraFim(fim);
    procedimento.setModalidade(ModalidadeEnum.PRESENCIAL);
    procedimento.setStatus(StatusAgendamentoEnum.ATIVO);
    procedimento.setPacienteCpf("11122233344");
    procedimento.setMedicoCrm("CRM456789");

    assertNotNull(procedimento);
    assertEquals("Cirurgia de Apendicite", procedimento.getDescricaoProcedimento());
    assertEquals("Sala Cirúrgica 2", procedimento.getSalaEquipamentoNecessario());
    assertEquals("Alto", procedimento.getNivelRisco());
    assertEquals(inicio, procedimento.getDataHoraInicio());
    assertEquals(fim, procedimento.getDataHoraFim());
    assertEquals(ModalidadeEnum.PRESENCIAL, procedimento.getModalidade());
    assertEquals(StatusAgendamentoEnum.ATIVO, procedimento.getStatus());
  }

  @Test
  void testProcedimentoRiscoBaixo() {
    Procedimento procedimento = Procedimento.builder()
        .descricaoProcedimento("Aplicação de Injeção")
        .salaEquipamentoNecessario("Consultório 1")
        .nivelRisco("Baixo")
        .build();

    assertEquals("Aplicação de Injeção", procedimento.getDescricaoProcedimento());
    assertEquals("Consultório 1", procedimento.getSalaEquipamentoNecessario());
    assertEquals("Baixo", procedimento.getNivelRisco());
  }

  @Test
  void testProcedimentoRiscoMedio() {
    Procedimento procedimento = Procedimento.builder()
        .descricaoProcedimento("Endoscopia")
        .salaEquipamentoNecessario("Sala de Endoscopia")
        .nivelRisco("Médio")
        .build();

    assertEquals("Endoscopia", procedimento.getDescricaoProcedimento());
    assertEquals("Sala de Endoscopia", procedimento.getSalaEquipamentoNecessario());
    assertEquals("Médio", procedimento.getNivelRisco());
  }

  @Test
  void testProcedimentoSettersAndGetters() {
    Procedimento procedimento = new Procedimento();
    procedimento.setDescricaoProcedimento("Biópsia");
    procedimento.setSalaEquipamentoNecessario("Laboratório");
    procedimento.setNivelRisco("Médio");

    assertEquals("Biópsia", procedimento.getDescricaoProcedimento());
    assertEquals("Laboratório", procedimento.getSalaEquipamentoNecessario());
    assertEquals("Médio", procedimento.getNivelRisco());
  }

  @Test
  void testProcedimentoHerdaAgendamento() {
    Procedimento procedimento = new Procedimento();
    procedimento.setId("proc123");
    procedimento.setDescricaoProcedimento("Cirurgia Cardíaca");
    procedimento.setStatus(StatusAgendamentoEnum.ATIVO);
    procedimento.setModalidade(ModalidadeEnum.PRESENCIAL);
    procedimento.setPacienteCpf("55566677788");
    procedimento.setMedicoCrm("CRM123123");
    procedimento.setObservacoes("Procedimento complexo");

    assertEquals("proc123", procedimento.getId());
    assertEquals("Cirurgia Cardíaca", procedimento.getDescricaoProcedimento());
    assertEquals(StatusAgendamentoEnum.ATIVO, procedimento.getStatus());
    assertEquals(ModalidadeEnum.PRESENCIAL, procedimento.getModalidade());
    assertEquals("55566677788", procedimento.getPacienteCpf());
    assertEquals("CRM123123", procedimento.getMedicoCrm());
    assertEquals("Procedimento complexo", procedimento.getObservacoes());
  }

  @Test
  void testProcedimentoAllArgsConstructor() {
    Procedimento procedimento = new Procedimento(
        "Pequena Cirurgia",
        "Sala 3",
        "Baixo"
    );

    assertEquals("Pequena Cirurgia", procedimento.getDescricaoProcedimento());
    assertEquals("Sala 3", procedimento.getSalaEquipamentoNecessario());
    assertEquals("Baixo", procedimento.getNivelRisco());
  }

  @Test
  void testProcedimentoNoArgsConstructor() {
    Procedimento procedimento = new Procedimento();

    assertNull(procedimento.getDescricaoProcedimento());
    assertNull(procedimento.getSalaEquipamentoNecessario());
    assertNull(procedimento.getNivelRisco());
  }
}

