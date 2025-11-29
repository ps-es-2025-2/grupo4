package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExameServiceTest {

  @Mock
  private ExameRepository exameRepository;

  @InjectMocks
  private ExameService exameService;

  private Exame exame;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 5, 8, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 5, 9, 0);

    exame = Exame.builder()
        .nomeExame("Exame de Sangue")
        .requerPreparo(true)
        .instrucoesPreparo("Jejum de 8 horas")
        .build();

    exame.setId("exam123");
    exame.setDataHoraInicio(inicio);
    exame.setDataHoraFim(fim);
    exame.setModalidade(ModalidadeEnum.PRESENCIAL);
    exame.setStatus(StatusAgendamentoEnum.ATIVO);
    exame.setPacienteCpf("12345678900");
    exame.setMedicoCrm("CRM123456");
  }

  @Test
  void testSalvarExameComSucesso() {
    when(exameRepository.save(any(Exame.class))).thenReturn(exame);

    Exame resultado = exameService.salvar(exame);

    assertNotNull(resultado);
    assertEquals("exam123", resultado.getId());
    assertEquals("Exame de Sangue", resultado.getNomeExame());
    assertTrue(resultado.getRequerPreparo());
    assertEquals("Jejum de 8 horas", resultado.getInstrucoesPreparo());
    assertEquals("12345678900", resultado.getPacienteCpf());
    assertEquals("CRM123456", resultado.getMedicoCrm());

    verify(exameRepository, times(1)).save(exame);
  }

  @Test
  void testSalvarExameSemPreparo() {
    Exame exameSimples = Exame.builder()
        .nomeExame("Raio-X")
        .requerPreparo(false)
        .build();
    exameSimples.setId("exam456");

    when(exameRepository.save(any(Exame.class))).thenReturn(exameSimples);

    Exame resultado = exameService.salvar(exameSimples);

    assertNotNull(resultado);
    assertEquals("exam456", resultado.getId());
    assertEquals("Raio-X", resultado.getNomeExame());
    assertFalse(resultado.getRequerPreparo());

    verify(exameRepository).save(exameSimples);
  }

  @Test
  void testSalvarExameChamaRepository() {
    when(exameRepository.save(any(Exame.class))).thenReturn(exame);

    exameService.salvar(exame);

    verify(exameRepository, times(1)).save(exame);
  }

  @Test
  void testSalvarExameComDadosCompletos() {
    when(exameRepository.save(any(Exame.class))).thenReturn(exame);

    Exame resultado = exameService.salvar(exame);

    assertNotNull(resultado.getId());
    assertNotNull(resultado.getNomeExame());
    assertNotNull(resultado.getDataHoraInicio());
    assertNotNull(resultado.getDataHoraFim());
    assertNotNull(resultado.getModalidade());
    assertNotNull(resultado.getStatus());

    verify(exameRepository).save(exame);
  }

  @Test
  void testSalvarDiferentesTiposDeExame() {
    Exame tomografia = Exame.builder()
        .nomeExame("Tomografia")
        .requerPreparo(true)
        .instrucoesPreparo("Retirar objetos metálicos")
        .build();

    when(exameRepository.save(any(Exame.class))).thenReturn(tomografia);

    Exame resultado = exameService.salvar(tomografia);

    assertEquals("Tomografia", resultado.getNomeExame());
    verify(exameRepository).save(tomografia);
  }
}

