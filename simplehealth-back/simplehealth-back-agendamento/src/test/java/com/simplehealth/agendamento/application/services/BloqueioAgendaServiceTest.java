package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BloqueioAgendaServiceTest {

  @Mock
  private BloqueioAgendaRepository bloqueioRepository;

  @InjectMocks
  private BloqueioAgendaService bloqueioAgendaService;

  private BloqueioAgenda bloqueioAgenda;

  @BeforeEach
  void setUp() {
    bloqueioAgenda = BloqueioAgenda.builder()
        .id("bloq123")
        .medicoCrm("CRM123456")
        .dataInicio(LocalDateTime.of(2025, 12, 1, 10, 0))
        .dataFim(LocalDateTime.of(2025, 12, 1, 12, 0))
        .motivo("Férias")
        .ativo(true)
        .build();
  }

  @Test
  void testExistemAgendamentosAtivosRetornaTrue() {
    when(bloqueioRepository.existsByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(true);

    boolean resultado = bloqueioAgendaService.existemAgendamentosAtivos(
        "CRM123456",
        LocalDateTime.of(2025, 12, 1, 10, 30),
        LocalDateTime.of(2025, 12, 1, 11, 0)
    );

    assertTrue(resultado);
    verify(bloqueioRepository, times(1))
        .existsByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
            anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
  }

  @Test
  void testExistemAgendamentosAtivosRetornaFalse() {
    when(bloqueioRepository.existsByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(false);

    boolean resultado = bloqueioAgendaService.existemAgendamentosAtivos(
        "CRM123456",
        LocalDateTime.of(2025, 12, 1, 14, 0),
        LocalDateTime.of(2025, 12, 1, 15, 0)
    );

    assertFalse(resultado);
    verify(bloqueioRepository, times(1))
        .existsByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
            anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
  }

  @Test
  void testSalvarBloqueioAgenda() {
    when(bloqueioRepository.save(any(BloqueioAgenda.class))).thenReturn(bloqueioAgenda);

    BloqueioAgenda resultado = bloqueioAgendaService.salvar(bloqueioAgenda);

    assertNotNull(resultado);
    assertEquals("bloq123", resultado.getId());
    assertEquals("CRM123456", resultado.getMedicoCrm());
    verify(bloqueioRepository, times(1)).save(bloqueioAgenda);
  }

  @Test
  void testSalvarBloqueioAgendaRetornaBloqueioSalvo() {
    when(bloqueioRepository.save(any(BloqueioAgenda.class))).thenReturn(bloqueioAgenda);

    BloqueioAgenda resultado = bloqueioAgendaService.salvar(bloqueioAgenda);

    assertNotNull(resultado);
    assertSame(bloqueioAgenda, resultado);
    verify(bloqueioRepository).save(bloqueioAgenda);
  }
}

