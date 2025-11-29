package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExameTest {

  @Test
  void testExameCreationWithBuilder() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 5, 8, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 5, 9, 0);

    Exame exame = Exame.builder()
        .nomeExame("Exame de Sangue")
        .requerPreparo(true)
        .instrucoesPreparo("Jejum de 8 horas")
        .build();

    exame.setDataHoraInicio(inicio);
    exame.setDataHoraFim(fim);
    exame.setModalidade(ModalidadeEnum.PRESENCIAL);
    exame.setStatus(StatusAgendamentoEnum.ATIVO);
    exame.setPacienteCpf("12345678900");

    assertNotNull(exame);
    assertEquals("Exame de Sangue", exame.getNomeExame());
    assertTrue(exame.getRequerPreparo());
    assertEquals("Jejum de 8 horas", exame.getInstrucoesPreparo());
    assertEquals(inicio, exame.getDataHoraInicio());
    assertEquals(fim, exame.getDataHoraFim());
    assertEquals(ModalidadeEnum.PRESENCIAL, exame.getModalidade());
    assertEquals(StatusAgendamentoEnum.ATIVO, exame.getStatus());
    assertEquals("12345678900", exame.getPacienteCpf());
  }

  @Test
  void testExameSemPreparo() {
    Exame exame = Exame.builder()
        .nomeExame("Raio-X")
        .requerPreparo(false)
        .build();

    assertEquals("Raio-X", exame.getNomeExame());
    assertFalse(exame.getRequerPreparo());
    assertNull(exame.getInstrucoesPreparo());
  }

  @Test
  void testExameComPreparo() {
    Exame exame = Exame.builder()
        .nomeExame("Ultrassom Abdominal")
        .requerPreparo(true)
        .instrucoesPreparo("Beber 1 litro de água 1 hora antes do exame")
        .build();

    assertEquals("Ultrassom Abdominal", exame.getNomeExame());
    assertTrue(exame.getRequerPreparo());
    assertEquals("Beber 1 litro de água 1 hora antes do exame", exame.getInstrucoesPreparo());
  }

  @Test
  void testExameSettersAndGetters() {
    Exame exame = new Exame();
    exame.setNomeExame("Tomografia");
    exame.setRequerPreparo(true);
    exame.setInstrucoesPreparo("Retirar objetos metálicos");

    assertEquals("Tomografia", exame.getNomeExame());
    assertTrue(exame.getRequerPreparo());
    assertEquals("Retirar objetos metálicos", exame.getInstrucoesPreparo());
  }

  @Test
  void testExameHerdaAgendamento() {
    Exame exame = new Exame();
    exame.setId("exam123");
    exame.setNomeExame("Ressonância Magnética");
    exame.setStatus(StatusAgendamentoEnum.ATIVO);
    exame.setModalidade(ModalidadeEnum.PRESENCIAL);
    exame.setPacienteCpf("98765432100");
    exame.setMedicoCrm("CRM789012");

    assertEquals("exam123", exame.getId());
    assertEquals("Ressonância Magnética", exame.getNomeExame());
    assertEquals(StatusAgendamentoEnum.ATIVO, exame.getStatus());
    assertEquals(ModalidadeEnum.PRESENCIAL, exame.getModalidade());
    assertEquals("98765432100", exame.getPacienteCpf());
    assertEquals("CRM789012", exame.getMedicoCrm());
  }

  @Test
  void testExameAllArgsConstructor() {
    Exame exame = new Exame("Eletrocardiograma", false, null);

    assertEquals("Eletrocardiograma", exame.getNomeExame());
    assertFalse(exame.getRequerPreparo());
    assertNull(exame.getInstrucoesPreparo());
  }
}

