package com.simplehealth.agendamento.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.agendamento.application.dtos.BloqueioAgendaDTO;
import com.simplehealth.agendamento.application.usecases.RegistrarBloqueioAgendaUseCase;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
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

@WebMvcTest(BloqueioAgendaController.class)
class BloqueioAgendaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private RegistrarBloqueioAgendaUseCase registrarBloqueioAgendaUseCase;

  private BloqueioAgendaDTO bloqueioAgendaDTO;
  private BloqueioAgenda bloqueioAgenda;

  @BeforeEach
  void setUp() {
    LocalDateTime inicio = LocalDateTime.of(2025, 12, 1, 10, 0);
    LocalDateTime fim = LocalDateTime.of(2025, 12, 1, 12, 0);

    bloqueioAgendaDTO = BloqueioAgendaDTO.builder()
        .medicoCrm("CRM123456")
        .dataInicio(inicio)
        .dataFim(fim)
        .motivo("Férias")
        .antecedenciaMinima(24)
        .usuarioCriadorLogin("admin")
        .build();

    bloqueioAgenda = BloqueioAgenda.builder()
        .id("bloq123")
        .medicoCrm("CRM123456")
        .dataInicio(inicio)
        .dataFim(fim)
        .motivo("Férias")
        .antecedenciaMinima(24)
        .usuarioCriadorLogin("admin")
        .ativo(true)
        .dataCriacao(LocalDateTime.now())
        .build();
  }

  @Test
  void testCriarBloqueioComSucesso() throws Exception {
    when(registrarBloqueioAgendaUseCase.registrar(any(BloqueioAgendaDTO.class)))
        .thenReturn(bloqueioAgenda);

    mockMvc.perform(post("/bloqueio-agenda")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bloqueioAgendaDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("bloq123"))
        .andExpect(jsonPath("$.medicoCrm").value("CRM123456"))
        .andExpect(jsonPath("$.motivo").value("Férias"))
        .andExpect(jsonPath("$.ativo").value(true))
        .andExpect(jsonPath("$.antecedenciaMinima").value(24));

    verify(registrarBloqueioAgendaUseCase, times(1)).registrar(any(BloqueioAgendaDTO.class));
  }

  @Test
  void testCriarBloqueioRetornaBloqueioAgenda() throws Exception {
    when(registrarBloqueioAgendaUseCase.registrar(any(BloqueioAgendaDTO.class)))
        .thenReturn(bloqueioAgenda);

    mockMvc.perform(post("/bloqueio-agenda")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bloqueioAgendaDTO)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").exists());

    verify(registrarBloqueioAgendaUseCase).registrar(any(BloqueioAgendaDTO.class));
  }

  @Test
  void testCriarBloqueioChamaUseCase() throws Exception {
    when(registrarBloqueioAgendaUseCase.registrar(any(BloqueioAgendaDTO.class)))
        .thenReturn(bloqueioAgenda);

    mockMvc.perform(post("/bloqueio-agenda")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bloqueioAgendaDTO)))
        .andExpect(status().isOk());

    verify(registrarBloqueioAgendaUseCase, times(1)).registrar(any(BloqueioAgendaDTO.class));
  }
}

