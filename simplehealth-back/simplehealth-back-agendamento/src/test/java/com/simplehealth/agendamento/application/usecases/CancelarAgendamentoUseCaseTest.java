package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarAgendamentoUseCaseTest {

  @Mock
  private AgendamentoService agendamentoService;

  @Mock
  private ConsultaService consultaService;

  @InjectMocks
  private CancelarAgendamentoUseCase cancelarAgendamentoUseCase;

  private CancelarAgendamentoDTO dto;
  private Consulta consulta;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 11, 0);
    LocalDateTime dataCancelamento = LocalDateTime.now();

    dto = CancelarAgendamentoDTO.builder()
        .id("cons123")
        .motivo("Paciente desistiu")
        .usuarioLogin("admin")
        .dataHoraCancelamento(dataCancelamento)
        .build();

    consulta = new Consulta();
    consulta.setId("cons123");
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setDataHoraInicio(inicio);
    consulta.setDataHoraFim(fim);
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
  }

  @Test
  void testExecuteComSucesso() throws Exception {
    when(consultaService.buscarPorId("cons123")).thenReturn(Optional.of(consulta));
    doNothing().when(agendamentoService).validarCancelamento(any(Consulta.class), anyString());
    when(consultaService.salvar(any(Consulta.class))).thenAnswer(invocation -> {
      Consulta c = invocation.getArgument(0);
      c.setId("cons123");
      return c;
    });

    Consulta resultado = cancelarAgendamentoUseCase.execute(dto);

    assertNotNull(resultado);
    assertEquals(StatusAgendamentoEnum.CANCELADO, resultado.getStatus());
    assertEquals("Paciente desistiu", resultado.getMotivoCancelamento());
    assertEquals("admin", resultado.getUsuarioCanceladorLogin());

    verify(consultaService, times(1)).buscarPorId("cons123");
    verify(agendamentoService, times(1)).validarCancelamento(consulta, dto.getMotivo());
    verify(consultaService, times(1)).salvar(any(Consulta.class));
  }

  @Test
  void testExecuteConsultaNaoEncontrada() {
    when(consultaService.buscarPorId("cons123")).thenReturn(Optional.empty());

    Exception exception = assertThrows(Exception.class, () ->
        cancelarAgendamentoUseCase.execute(dto)
    );

    assertEquals("Consulta não encontrada", exception.getMessage());
    verify(consultaService, times(1)).buscarPorId("cons123");
    verify(agendamentoService, never()).validarCancelamento(any(Consulta.class), anyString());
    verify(consultaService, times(1)).buscarPorId(anyString());
  }

  @Test
  void testExecuteValidaCancelamentoAntesDeProcessar() throws Exception {
    when(consultaService.buscarPorId("cons123")).thenReturn(Optional.of(consulta));
    doNothing().when(agendamentoService).validarCancelamento(any(Consulta.class), anyString());
    when(consultaService.salvar(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

    cancelarAgendamentoUseCase.execute(dto);

    verify(agendamentoService).validarCancelamento(consulta, dto.getMotivo());
    verify(consultaService).salvar(any(Consulta.class));
  }

  @Test
  void testExecuteDefineStatusCancelado() throws Exception {
    when(consultaService.buscarPorId("cons123")).thenReturn(Optional.of(consulta));
    doNothing().when(agendamentoService).validarCancelamento(any(Consulta.class), anyString());
    when(consultaService.salvar(any(Consulta.class))).thenAnswer(invocation -> {
      Consulta c = invocation.getArgument(0);
      assertEquals(StatusAgendamentoEnum.CANCELADO, c.getStatus());
      assertEquals("Paciente desistiu", c.getMotivoCancelamento());
      assertEquals("admin", c.getUsuarioCanceladorLogin());
      return c;
    });

    cancelarAgendamentoUseCase.execute(dto);

    verify(consultaService).salvar(any(Consulta.class));
  }

  @Test
  void testExecuteComValidacaoFalhando() throws Exception {
    when(consultaService.buscarPorId("cons123")).thenReturn(Optional.of(consulta));
    doThrow(new IllegalStateException("O agendamento já está cancelado."))
        .when(agendamentoService).validarCancelamento(any(Consulta.class), anyString());

    assertThrows(IllegalStateException.class, () ->
        cancelarAgendamentoUseCase.execute(dto)
    );

    verify(consultaService, times(1)).buscarPorId("cons123");
    verify(agendamentoService, times(1)).validarCancelamento(consulta, dto.getMotivo());
    verify(consultaService, never()).salvar(any(Consulta.class));
  }
}

