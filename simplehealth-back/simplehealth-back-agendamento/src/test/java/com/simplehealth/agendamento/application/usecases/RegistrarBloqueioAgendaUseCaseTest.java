package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.dtos.BloqueioAgendaDTO;
import com.simplehealth.agendamento.application.services.BloqueioAgendaService;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
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
class RegistrarBloqueioAgendaUseCaseTest {

  @Mock
  private BloqueioAgendaService bloqueioAgendaService;

  @InjectMocks
  private RegistrarBloqueioAgendaUseCase registrarBloqueioAgendaUseCase;

  private BloqueioAgendaDTO bloqueioAgendaDTO;
  private BloqueioAgenda bloqueioAgenda;

  @BeforeEach
  void setUp() {
    LocalDateTime dataInicio = LocalDateTime.of(2025, 12, 24, 0, 0);
    LocalDateTime dataFim = LocalDateTime.of(2025, 12, 31, 23, 59);

    bloqueioAgendaDTO = BloqueioAgendaDTO.builder()
        .dataInicio(dataInicio)
        .dataFim(dataFim)
        .motivo("Férias de fim de ano")
        .antecedenciaMinima(48)
        .medicoCrm("CRM123456")
        .usuarioCriadorLogin("admin")
        .build();

    bloqueioAgenda = new BloqueioAgenda();
    bloqueioAgenda.setId("bloq123");
    bloqueioAgenda.setDataInicio(dataInicio);
    bloqueioAgenda.setDataFim(dataFim);
    bloqueioAgenda.setMotivo("Férias de fim de ano");
    bloqueioAgenda.setAntecedenciaMinima(48);
    bloqueioAgenda.setMedicoCrm("CRM123456");
    bloqueioAgenda.setUsuarioCriadorLogin("admin");
  }

  @Test
  void testRegistrarBloqueioComSucesso() {
    when(bloqueioAgendaService.salvar(any(BloqueioAgenda.class))).thenReturn(bloqueioAgenda);

    BloqueioAgenda resultado = registrarBloqueioAgendaUseCase.registrar(bloqueioAgendaDTO);

    assertNotNull(resultado);
    assertEquals("bloq123", resultado.getId());
    assertEquals(bloqueioAgendaDTO.getDataInicio(), resultado.getDataInicio());
    assertEquals(bloqueioAgendaDTO.getDataFim(), resultado.getDataFim());
    assertEquals(bloqueioAgendaDTO.getMotivo(), resultado.getMotivo());
    assertEquals(bloqueioAgendaDTO.getAntecedenciaMinima(), resultado.getAntecedenciaMinima());
    assertEquals(bloqueioAgendaDTO.getMedicoCrm(), resultado.getMedicoCrm());
    assertEquals(bloqueioAgendaDTO.getUsuarioCriadorLogin(), resultado.getUsuarioCriadorLogin());

    verify(bloqueioAgendaService, times(1)).salvar(any(BloqueioAgenda.class));
  }

  @Test
  void testRegistrarBloqueioComDadosCompletos() {
    when(bloqueioAgendaService.salvar(any(BloqueioAgenda.class))).thenReturn(bloqueioAgenda);

    BloqueioAgenda resultado = registrarBloqueioAgendaUseCase.registrar(bloqueioAgendaDTO);

    assertNotNull(resultado);
    assertNotNull(resultado.getId());
    assertNotNull(resultado.getDataInicio());
    assertNotNull(resultado.getDataFim());
    assertNotNull(resultado.getMotivo());
    assertNotNull(resultado.getMedicoCrm());

    verify(bloqueioAgendaService).salvar(any(BloqueioAgenda.class));
  }

  @Test
  void testRegistrarBloqueioChamaBloqueioAgendaService() {
    when(bloqueioAgendaService.salvar(any(BloqueioAgenda.class))).thenReturn(bloqueioAgenda);

    registrarBloqueioAgendaUseCase.registrar(bloqueioAgendaDTO);

    verify(bloqueioAgendaService, times(1)).salvar(any(BloqueioAgenda.class));
  }

  @Test
  void testRegistrarBloqueioComAntecedenciaMinima() {
    bloqueioAgendaDTO.setAntecedenciaMinima(72);
    bloqueioAgenda.setAntecedenciaMinima(72);

    when(bloqueioAgendaService.salvar(any(BloqueioAgenda.class))).thenReturn(bloqueioAgenda);

    BloqueioAgenda resultado = registrarBloqueioAgendaUseCase.registrar(bloqueioAgendaDTO);

    assertEquals(72, resultado.getAntecedenciaMinima());
    verify(bloqueioAgendaService).salvar(any(BloqueioAgenda.class));
  }

  @Test
  void testRegistrarBloqueioParaDiferentesMedicos() {
    bloqueioAgendaDTO.setMedicoCrm("CRM789012");
    bloqueioAgenda.setMedicoCrm("CRM789012");

    when(bloqueioAgendaService.salvar(any(BloqueioAgenda.class))).thenReturn(bloqueioAgenda);

    BloqueioAgenda resultado = registrarBloqueioAgendaUseCase.registrar(bloqueioAgendaDTO);

    assertEquals("CRM789012", resultado.getMedicoCrm());
    verify(bloqueioAgendaService).salvar(any(BloqueioAgenda.class));
  }
}

