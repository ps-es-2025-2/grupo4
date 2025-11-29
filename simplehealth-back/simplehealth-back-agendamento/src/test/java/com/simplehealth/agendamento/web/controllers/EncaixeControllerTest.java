package com.simplehealth.agendamento.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.agendamento.application.dtos.EncaixeDTO;
import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.usecases.SolicitarEncaixeUseCase;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
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

@WebMvcTest(EncaixeController.class)
class EncaixeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
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
    when(solicitarEncaixeUseCase.execute(any(EncaixeDTO.class))).thenReturn(consulta);

    mockMvc.perform(post("/encaixe")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(encaixeDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("cons123"))
        .andExpect(jsonPath("$.pacienteCpf").value("12345678900"))
        .andExpect(jsonPath("$.medicoCrm").value("CRM123456"))
        .andExpect(jsonPath("$.isEncaixe").value(true))
        .andExpect(jsonPath("$.motivoEncaixe").value("Urgência médica"))
        .andExpect(jsonPath("$.status").value("ATIVO"));

    verify(solicitarEncaixeUseCase, times(1)).execute(any(EncaixeDTO.class));
  }

  @Test
  void testSolicitarEncaixeRetornaConsulta() throws Exception {
    when(solicitarEncaixeUseCase.execute(any(EncaixeDTO.class))).thenReturn(consulta);

    mockMvc.perform(post("/encaixe")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(encaixeDTO)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").exists());

    verify(solicitarEncaixeUseCase).execute(any(EncaixeDTO.class));
  }

  @Test
  void testSolicitarEncaixeChamaUseCase() throws Exception {
    when(solicitarEncaixeUseCase.execute(any(EncaixeDTO.class))).thenReturn(consulta);

    mockMvc.perform(post("/encaixe")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(encaixeDTO)))
        .andExpect(status().isOk());

    verify(solicitarEncaixeUseCase, times(1)).execute(any(EncaixeDTO.class));
  }
}

