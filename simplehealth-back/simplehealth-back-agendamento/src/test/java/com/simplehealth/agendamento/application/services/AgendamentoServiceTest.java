package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.AgendamentoRepository;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

  @Mock
  private AgendamentoRepository agendamentoRepository;

  @Mock
  private BloqueioAgendaRepository bloqueioRepository;

  @InjectMocks
  private AgendamentoService agendamentoService;

  private Consulta consulta;
  private BloqueioAgenda bloqueio;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 11, 0);

    consulta = new Consulta();
    consulta.setId("cons123");
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setDataHoraInicio(inicio);
    consulta.setDataHoraFim(fim);
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);

    bloqueio = BloqueioAgenda.builder()
        .id("bloq123")
        .medicoCrm("CRM123456")
        .dataInicio(inicio)
        .dataFim(fim)
        .motivo("Férias")
        .ativo(true)
        .build();
  }

  @Test
  void testSalvarAgendamento() {
    when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(consulta);

    Agendamento resultado = agendamentoService.salvar(consulta);

    assertNotNull(resultado);
    assertEquals("cons123", resultado.getId());
    verify(agendamentoRepository, times(1)).save(consulta);
  }

  @Test
  void testBuscarPorIdExistente() {
    when(agendamentoRepository.findById("cons123")).thenReturn(Optional.of(consulta));

    Optional<Agendamento> resultado = agendamentoService.buscarPorId("cons123");

    assertTrue(resultado.isPresent());
    assertEquals("cons123", resultado.get().getId());
    verify(agendamentoRepository, times(1)).findById("cons123");
  }

  @Test
  void testBuscarPorIdNaoExistente() {
    when(agendamentoRepository.findById("inexistente")).thenReturn(Optional.empty());

    Optional<Agendamento> resultado = agendamentoService.buscarPorId("inexistente");

    assertFalse(resultado.isPresent());
    verify(agendamentoRepository, times(1)).findById("inexistente");
  }

  @Test
  void testVerificarDisponibilidadeHorarioDisponivel() {
    when(agendamentoRepository.findByMedicoCrmAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqualAndStatus(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class), any(StatusAgendamentoEnum.class)))
        .thenReturn(new ArrayList<>());
    when(bloqueioRepository.findByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(new ArrayList<>());

    assertDoesNotThrow(() -> agendamentoService.verificarDisponibilidade(
        "CRM123456",
        LocalDateTime.of(2025, 12, 1, 14, 0),
        LocalDateTime.of(2025, 12, 1, 15, 0)
    ));
  }

  @Test
  void testVerificarDisponibilidadeHorarioIndisponivel() {
    List<Agendamento> agendamentos = List.of(consulta);
    when(agendamentoRepository.findByMedicoCrmAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqualAndStatus(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class), any(StatusAgendamentoEnum.class)))
        .thenReturn(agendamentos);

    Exception exception = assertThrows(Exception.class, () ->
        agendamentoService.verificarDisponibilidade(
            "CRM123456",
            LocalDateTime.of(2025, 12, 1, 10, 30),
            LocalDateTime.of(2025, 12, 1, 10, 45)
        )
    );

    assertEquals("Horário indisponível.", exception.getMessage());
  }

  @Test
  void testVerificarDisponibilidadeHorarioBloqueado() {
    List<BloqueioAgenda> bloqueios = List.of(bloqueio);
    when(agendamentoRepository.findByMedicoCrmAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqualAndStatus(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class), any(StatusAgendamentoEnum.class)))
        .thenReturn(new ArrayList<>());
    when(bloqueioRepository.findByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(bloqueios);

    Exception exception = assertThrows(Exception.class, () ->
        agendamentoService.verificarDisponibilidade(
            "CRM123456",
            LocalDateTime.of(2025, 12, 1, 10, 30),
            LocalDateTime.of(2025, 12, 1, 10, 45)
        )
    );

    assertEquals("Horário bloqueado.", exception.getMessage());
  }

  @Test
  void testBuscarHistorico() {
    List<Agendamento> historico = List.of(consulta);
    when(agendamentoRepository.findByPacienteCpfOrderByDataHoraInicioDesc("12345678900"))
        .thenReturn(historico);

    List<Agendamento> resultado = agendamentoService.buscarHistorico("12345678900");

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals("cons123", resultado.get(0).getId());
    verify(agendamentoRepository, times(1)).findByPacienteCpfOrderByDataHoraInicioDesc("12345678900");
  }

  @Test
  void testValidarCancelamentoValido() {
    consulta.setDataHoraInicio(LocalDateTime.now().plusDays(1));

    assertDoesNotThrow(() ->
        agendamentoService.validarCancelamento(consulta, "Paciente desistiu")
    );
  }

  @Test
  void testValidarCancelamentoJaCancelado() {
    consulta.setStatus(StatusAgendamentoEnum.CANCELADO);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
        agendamentoService.validarCancelamento(consulta, "Motivo qualquer")
    );

    assertEquals("O agendamento já está cancelado.", exception.getMessage());
  }

  @Test
  void testValidarCancelamentoMotivoNulo() {
    consulta.setDataHoraInicio(LocalDateTime.now().plusDays(1));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        agendamentoService.validarCancelamento(consulta, null)
    );

    assertEquals("O motivo do cancelamento é obrigatório.", exception.getMessage());
  }

  @Test
  void testValidarCancelamentoMotivoVazio() {
    consulta.setDataHoraInicio(LocalDateTime.now().plusDays(1));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        agendamentoService.validarCancelamento(consulta, "   ")
    );

    assertEquals("O motivo do cancelamento é obrigatório.", exception.getMessage());
  }

  @Test
  void testValidarCancelamentoAgendamentoPassado() {
    consulta.setDataHoraInicio(LocalDateTime.now().minusDays(1));

    IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
        agendamentoService.validarCancelamento(consulta, "Motivo qualquer")
    );

    assertEquals("Não é possível cancelar um agendamento que já ocorreu.", exception.getMessage());
  }
}

