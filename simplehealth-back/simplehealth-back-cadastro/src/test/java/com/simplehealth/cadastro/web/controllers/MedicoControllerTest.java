package com.simplehealth.cadastro.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.cadastro.application.dto.MedicoDTO;
import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.application.service.MedicoService;
import com.simplehealth.cadastro.application.config.SecurityConfig;
import com.simplehealth.cadastro.domain.entity.Medico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicoController.class)
@Import(SecurityConfig.class)
class MedicoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MedicoService medicoService;

  private Medico medico;
  private MedicoDTO medicoDTO;

  @BeforeEach
  void setUp() {
    medico = Medico.builder()
        .id(1L)
        .nomeCompleto("Dr. Carlos")
        .crm("123456")
        .especialidade("Cardiologia")
        .telefone("11999999999")
        .email("carlos@email.com")
        .build();

    medicoDTO = MedicoDTO.builder()
        .id(1L)
        .nomeCompleto("Dr. Carlos")
        .crm("123456")
        .especialidade("Cardiologia")
        .telefone("11999999999")
        .email("carlos@email.com")
        .build();
  }

  @Test
  void testCreate_Success() throws Exception {
    when(medicoService.create(any(Medico.class))).thenReturn(medico);

    mockMvc.perform(post("/api/cadastro/medicos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(medicoDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nomeCompleto").value("Dr. Carlos"))
        .andExpect(jsonPath("$.crm").value("123456"))
        .andExpect(jsonPath("$.especialidade").value("Cardiologia"));

    verify(medicoService, times(1)).create(any(Medico.class));
  }

  @Test
  @WithMockUser
  void testGetById_Success() throws Exception {
    when(medicoService.findById(anyLong())).thenReturn(medico);

    mockMvc.perform(get("/api/cadastro/medicos/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nomeCompleto").value("Dr. Carlos"))
        .andExpect(jsonPath("$.crm").value("123456"));

    verify(medicoService, times(1)).findById(anyLong());
  }

  @Test
  @WithMockUser
  void testGetById_NotFound() throws Exception {
    when(medicoService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Médico não encontrado"));

    mockMvc.perform(get("/api/cadastro/medicos/999"))
        .andExpect(status().isNotFound());

    verify(medicoService, times(1)).findById(anyLong());
  }

  @Test
  @WithMockUser
  void testList_Success() throws Exception {
    Medico medico2 = Medico.builder()
        .id(2L)
        .nomeCompleto("Dra. Ana")
        .crm("654321")
        .especialidade("Ortopedia")
        .build();

    List<Medico> medicos = Arrays.asList(medico, medico2);
    when(medicoService.findAll()).thenReturn(medicos);

    mockMvc.perform(get("/api/cadastro/medicos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[1].id").value(2L));

    verify(medicoService, times(1)).findAll();
  }

  @Test
  @WithMockUser
  void testUpdate_Success() throws Exception {
    when(medicoService.update(anyLong(), any(Medico.class))).thenReturn(medico);

    mockMvc.perform(put("/api/cadastro/medicos/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(medicoDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nomeCompleto").value("Dr. Carlos"));

    verify(medicoService, times(1)).update(anyLong(), any(Medico.class));
  }

  @Test
  @WithMockUser
  void testDelete_Success() throws Exception {
    doNothing().when(medicoService).delete(anyLong());

    mockMvc.perform(delete("/api/cadastro/medicos/1")
            .with(csrf()))
        .andExpect(status().isNoContent());

    verify(medicoService, times(1)).delete(anyLong());
  }
}

