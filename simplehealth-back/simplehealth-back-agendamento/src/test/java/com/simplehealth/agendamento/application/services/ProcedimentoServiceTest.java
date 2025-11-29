package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
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
class ProcedimentoServiceTest {

  @Mock
  private ProcedimentoRepository procedimentoRepository;

  @InjectMocks
  private ProcedimentoService procedimentoService;

  private Procedimento procedimento;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 10, 14, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 10, 16, 0);

    procedimento = Procedimento.builder()
        .descricaoProcedimento("Cirurgia de Apendicite")
        .salaEquipamentoNecessario("Sala Cirúrgica 2")
        .nivelRisco("Alto")
        .build();

    procedimento.setId("proc123");
    procedimento.setDataHoraInicio(inicio);
    procedimento.setDataHoraFim(fim);
    procedimento.setModalidade(ModalidadeEnum.PRESENCIAL);
    procedimento.setStatus(StatusAgendamentoEnum.ATIVO);
    procedimento.setPacienteCpf("11122233344");
    procedimento.setMedicoCrm("CRM456789");
  }

  @Test
  void testSalvarProcedimentoComSucesso() {
    when(procedimentoRepository.save(any(Procedimento.class))).thenReturn(procedimento);

    Procedimento resultado = procedimentoService.salvar(procedimento);

    assertNotNull(resultado);
    assertEquals("proc123", resultado.getId());
    assertEquals("Cirurgia de Apendicite", resultado.getDescricaoProcedimento());
    assertEquals("Sala Cirúrgica 2", resultado.getSalaEquipamentoNecessario());
    assertEquals("Alto", resultado.getNivelRisco());
    assertEquals("11122233344", resultado.getPacienteCpf());
    assertEquals("CRM456789", resultado.getMedicoCrm());

    verify(procedimentoRepository, times(1)).save(procedimento);
  }

  @Test
  void testSalvarProcedimentoRiscoBaixo() {
    Procedimento procSimples = Procedimento.builder()
        .descricaoProcedimento("Aplicação de Injeção")
        .salaEquipamentoNecessario("Consultório 1")
        .nivelRisco("Baixo")
        .build();
    procSimples.setId("proc456");

    when(procedimentoRepository.save(any(Procedimento.class))).thenReturn(procSimples);

    Procedimento resultado = procedimentoService.salvar(procSimples);

    assertNotNull(resultado);
    assertEquals("proc456", resultado.getId());
    assertEquals("Aplicação de Injeção", resultado.getDescricaoProcedimento());
    assertEquals("Baixo", resultado.getNivelRisco());

    verify(procedimentoRepository).save(procSimples);
  }

  @Test
  void testSalvarProcedimentoChamaRepository() {
    when(procedimentoRepository.save(any(Procedimento.class))).thenReturn(procedimento);

    procedimentoService.salvar(procedimento);

    verify(procedimentoRepository, times(1)).save(procedimento);
  }

  @Test
  void testSalvarProcedimentoComDadosCompletos() {
    when(procedimentoRepository.save(any(Procedimento.class))).thenReturn(procedimento);

    Procedimento resultado = procedimentoService.salvar(procedimento);

    assertNotNull(resultado.getId());
    assertNotNull(resultado.getDescricaoProcedimento());
    assertNotNull(resultado.getSalaEquipamentoNecessario());
    assertNotNull(resultado.getNivelRisco());
    assertNotNull(resultado.getDataHoraInicio());
    assertNotNull(resultado.getDataHoraFim());

    verify(procedimentoRepository).save(procedimento);
  }

  @Test
  void testSalvarDiferentesTiposDeProcedimento() {
    Procedimento endoscopia = Procedimento.builder()
        .descricaoProcedimento("Endoscopia")
        .salaEquipamentoNecessario("Sala de Endoscopia")
        .nivelRisco("Médio")
        .build();

    when(procedimentoRepository.save(any(Procedimento.class))).thenReturn(endoscopia);

    Procedimento resultado = procedimentoService.salvar(endoscopia);

    assertEquals("Endoscopia", resultado.getDescricaoProcedimento());
    assertEquals("Médio", resultado.getNivelRisco());
    verify(procedimentoRepository).save(endoscopia);
  }

  @Test
  void testSalvarProcedimentoComDiferentesNiveisRisco() {
    Procedimento baixo = Procedimento.builder()
        .nivelRisco("Baixo")
        .build();
    Procedimento medio = Procedimento.builder()
        .nivelRisco("Médio")
        .build();
    Procedimento alto = Procedimento.builder()
        .nivelRisco("Alto")
        .build();

    when(procedimentoRepository.save(any(Procedimento.class)))
        .thenReturn(baixo)
        .thenReturn(medio)
        .thenReturn(alto);

    Procedimento resultBaixo = procedimentoService.salvar(baixo);
    Procedimento resultMedio = procedimentoService.salvar(medio);
    Procedimento resultAlto = procedimentoService.salvar(alto);

    assertEquals("Baixo", resultBaixo.getNivelRisco());
    assertEquals("Médio", resultMedio.getNivelRisco());
    assertEquals("Alto", resultAlto.getNivelRisco());

    verify(procedimentoRepository, times(3)).save(any(Procedimento.class));
  }
}

