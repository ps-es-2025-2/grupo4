package com.simplehealth.agendamento.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.agendamento.application.dtos.AgendarConsultaDTO;
import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.usecases.AgendarConsultaUseCase;
import com.simplehealth.agendamento.application.usecases.CancelarAgendamentoUseCase;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendamentoController.class)
class AgendamentoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AgendarConsultaUseCase agendarConsultaUseCase;

  @MockBean
  private CancelarAgendamentoUseCase cancelarAgendamentoUseCase;

  private AgendarConsultaDTO agendarConsultaDTO;
  private CancelarAgendamentoDTO cancelarAgendamentoDTO;
  private Consulta consulta;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 11, 0);

    agendarConsultaDTO = AgendarConsultaDTO.builder()
        .pacienteCpf("12345678900")
        .medicoCrm("CRM123456")
        .dataHoraInicio(inicio)
        .dataHoraFim(fim)
        .tipoConsulta(TipoConsultaEnum.PRIMEIRA)
        .especialidade("Cardiologia")
        .convenioNome("Unimed")
        .usuarioCriadorLogin("admin")
        .build();

    cancelarAgendamentoDTO = CancelarAgendamentoDTO.builder()
        .id("cons123")
        .motivo("Paciente desistiu")
        .usuarioLogin("admin")
        .dataHoraCancelamento(LocalDateTime.now())
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
  void testAgendarConsultaComSucesso() throws Exception {
    when(agendarConsultaUseCase.execute(any(AgendarConsultaDTO.class))).thenReturn(consulta);

    mockMvc.perform(post("/agendamentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(agendarConsultaDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("cons123"))
        .andExpect(jsonPath("$.pacienteCpf").value("12345678900"))
        .andExpect(jsonPath("$.medicoCrm").value("CRM123456"))
        .andExpect(jsonPath("$.especialidade").value("Cardiologia"))
        .andExpect(jsonPath("$.status").value("ATIVO"));

    verify(agendarConsultaUseCase, times(1)).execute(any(AgendarConsultaDTO.class));
  }

  @Test
  void testCancelarConsultaComSucesso() throws Exception {
    consulta.setStatus(StatusAgendamentoEnum.CANCELADO);
    consulta.setMotivoCancelamento("Paciente desistiu");

    when(cancelarAgendamentoUseCase.execute(any(CancelarAgendamentoDTO.class))).thenReturn(consulta);

    mockMvc.perform(post("/agendamentos/cancelar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cancelarAgendamentoDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("cons123"))
        .andExpect(jsonPath("$.status").value("CANCELADO"))
        .andExpect(jsonPath("$.motivoCancelamento").value("Paciente desistiu"));

    verify(cancelarAgendamentoUseCase, times(1)).execute(any(CancelarAgendamentoDTO.class));
  }

  @Test
  void testAgendarConsultaRetornaConsulta() throws Exception {
    when(agendarConsultaUseCase.execute(any(AgendarConsultaDTO.class))).thenReturn(consulta);

    mockMvc.perform(post("/agendamentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(agendarConsultaDTO)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").exists());

    verify(agendarConsultaUseCase).execute(any(AgendarConsultaDTO.class));
  }
}

