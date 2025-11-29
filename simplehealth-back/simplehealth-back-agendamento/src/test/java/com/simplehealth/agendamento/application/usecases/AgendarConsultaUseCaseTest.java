package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.AgendarConsultaDTO;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.application.services.ConsultaService;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendarConsultaUseCaseTest {

  @Mock
  private AgendamentoService agendamentoService;

  @Mock
  private ConsultaService consultaService;

  @InjectMocks
  private AgendarConsultaUseCase agendarConsultaUseCase;

  private AgendarConsultaDTO dto;
  private Consulta consulta;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 11, 0);

    dto = AgendarConsultaDTO.builder()
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .tipoConsulta(TipoConsultaEnum.PRIMEIRA)
        .especialidade("Cardiologia")
        .convenioNome("Unimed")
        .usuarioCriadorLogin("admin")
        .build();

    consulta = new Consulta();
    consulta.setId("cons123");
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setDataHoraInicio(inicio);
    consulta.setDataHoraFim(fim);
    consulta.setTipoConsulta(TipoConsultaEnum.PRIMEIRA);
    consulta.setEspecialidade("Cardiologia");
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
  }

  @Test
  void testExecuteComSucesso() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    when(consultaService.salvar(any(Consulta.class))).thenReturn(consulta);

    Consulta resultado = agendarConsultaUseCase.execute(dto);

    assertNotNull(resultado);
    assertEquals("cons123", resultado.getId());
    assertEquals("12345678900", resultado.getPacienteCpf());
    assertEquals("CRM123456", resultado.getMedicoCrm());
    assertEquals(StatusAgendamentoEnum.ATIVO, resultado.getStatus());

    verify(agendamentoService, times(1)).verificarDisponibilidade(
        dto.getMedicoCrm(), dto.getDataHoraInicio(), dto.getDataHoraFim());
    verify(consultaService, times(1)).salvar(any(Consulta.class));
  }

  @Test
  void testExecuteComHorarioIndisponivel() throws Exception {
    doThrow(new Exception("Horário indisponível."))
        .when(agendamentoService).verificarDisponibilidade(
            anyString(), any(LocalDateTime.class), any(LocalDateTime.class));

    Exception exception = assertThrows(Exception.class, () ->
        agendarConsultaUseCase.execute(dto)
    );

    assertEquals("Horário indisponível.", exception.getMessage());
    verify(agendamentoService, times(1)).verificarDisponibilidade(
        dto.getMedicoCrm(), dto.getDataHoraInicio(), dto.getDataHoraFim());
    verify(consultaService, never()).salvar(any(Consulta.class));
  }

  @Test
  void testExecuteVerificaDisponibilidadeAntesDeSalvar() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    when(consultaService.salvar(any(Consulta.class))).thenReturn(consulta);

    agendarConsultaUseCase.execute(dto);

    verify(agendamentoService).verificarDisponibilidade(
        dto.getMedicoCrm(), dto.getDataHoraInicio(), dto.getDataHoraFim());
    verify(consultaService).salvar(any(Consulta.class));
  }

  @Test
  void testExecuteDefineStatusAtivo() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    when(consultaService.salvar(any(Consulta.class))).thenAnswer(invocation -> {
      Consulta c = invocation.getArgument(0);
      assertEquals(StatusAgendamentoEnum.ATIVO, c.getStatus());
      return consulta;
    });

    agendarConsultaUseCase.execute(dto);

    verify(consultaService).salvar(any(Consulta.class));
  }
}

