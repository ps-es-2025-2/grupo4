package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.EncaixeDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitarEncaixeUseCaseTest {

  @Mock
  private ConsultaService consultaService;

  @Mock
  private AgendamentoService agendamentoService;

  @InjectMocks
  private SolicitarEncaixeUseCase solicitarEncaixeUseCase;

  private EncaixeDTO encaixeDTO;
  private Consulta consulta;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 15, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 16, 0);

    encaixeDTO = EncaixeDTO.builder()
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .motivoEncaixe("Urgência médica")
        .observacoes("Paciente com dor aguda")
        .usuarioCriadorLogin("admin")
        .build();

    consulta = new Consulta();
    consulta.setId("cons123");
    consulta.setPacienteCpf("12345678900");
    consulta.setMedicoCrm("CRM123456");
    consulta.setDataHoraInicio(inicio);
    consulta.setDataHoraFim(fim);
    consulta.setIsEncaixe(true);
    consulta.setMotivoEncaixe("Urgência médica");
    consulta.setObservacoes("Paciente com dor aguda");
    consulta.setStatus(StatusAgendamentoEnum.ATIVO);
    consulta.setUsuarioCriadorLogin("admin");
  }

  @Test
  void testSolicitarEncaixeComSucesso() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
    );
    when(consultaService.salvar(any(Consulta.class))).thenReturn(consulta);

    Consulta resultado = solicitarEncaixeUseCase.execute(encaixeDTO);

    assertNotNull(resultado);
    assertEquals("cons123", resultado.getId());
    assertEquals("12345678900", resultado.getPacienteCpf());
    assertEquals("CRM123456", resultado.getMedicoCrm());
    assertTrue(resultado.getIsEncaixe());
    assertEquals("Urgência médica", resultado.getMotivoEncaixe());
    assertEquals("Paciente com dor aguda", resultado.getObservacoes());
    assertEquals(StatusAgendamentoEnum.ATIVO, resultado.getStatus());
    assertEquals("admin", resultado.getUsuarioCriadorLogin());

    verify(agendamentoService, times(1)).verificarDisponibilidade(
        eq("CRM123456"),
        eq(encaixeDTO.getDataHoraInicio()),
        eq(encaixeDTO.getDataHoraFim())
    );
    verify(consultaService, times(1)).salvar(any(Consulta.class));
  }

  @Test
  void testSolicitarEncaixeVerificaDisponibilidade() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
    );
    when(consultaService.salvar(any(Consulta.class))).thenReturn(consulta);

    solicitarEncaixeUseCase.execute(encaixeDTO);

    verify(agendamentoService).verificarDisponibilidade(
        eq("CRM123456"),
        eq(encaixeDTO.getDataHoraInicio()),
        eq(encaixeDTO.getDataHoraFim())
    );
  }

  @Test
  void testSolicitarEncaixeLancaExcecaoQuandoIndisponivel() throws Exception {
    doThrow(new AgendamentoException("Horário indisponível"))
        .when(agendamentoService).verificarDisponibilidade(
            anyString(),
            any(LocalDateTime.class),
            any(LocalDateTime.class)
        );

    assertThrows(AgendamentoException.class, () -> {
      solicitarEncaixeUseCase.execute(encaixeDTO);
    });

    verify(agendamentoService).verificarDisponibilidade(
        eq("CRM123456"),
        eq(encaixeDTO.getDataHoraInicio()),
        eq(encaixeDTO.getDataHoraFim())
    );
    verify(consultaService, never()).salvar(any(Consulta.class));
  }

  @Test
  void testSolicitarEncaixeMarcaComoEncaixe() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
    );
    when(consultaService.salvar(any(Consulta.class))).thenReturn(consulta);

    Consulta resultado = solicitarEncaixeUseCase.execute(encaixeDTO);

    assertTrue(resultado.getIsEncaixe());
    assertNotNull(resultado.getMotivoEncaixe());
    verify(consultaService).salvar(any(Consulta.class));
  }

  @Test
  void testSolicitarEncaixeComStatusAtivo() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
    );
    when(consultaService.salvar(any(Consulta.class))).thenReturn(consulta);

    Consulta resultado = solicitarEncaixeUseCase.execute(encaixeDTO);

    assertEquals(StatusAgendamentoEnum.ATIVO, resultado.getStatus());
    verify(consultaService).salvar(any(Consulta.class));
  }

  @Test
  void testSolicitarEncaixeComDadosCompletos() throws Exception {
    doNothing().when(agendamentoService).verificarDisponibilidade(
        anyString(),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
    );
    when(consultaService.salvar(any(Consulta.class))).thenReturn(consulta);

    Consulta resultado = solicitarEncaixeUseCase.execute(encaixeDTO);

    assertNotNull(resultado.getPacienteCpf());
    assertNotNull(resultado.getMedicoCrm());
    assertNotNull(resultado.getDataHoraInicio());
    assertNotNull(resultado.getDataHoraFim());
    assertNotNull(resultado.getMotivoEncaixe());
    assertNotNull(resultado.getUsuarioCriadorLogin());
    verify(consultaService).salvar(any(Consulta.class));
  }
}

