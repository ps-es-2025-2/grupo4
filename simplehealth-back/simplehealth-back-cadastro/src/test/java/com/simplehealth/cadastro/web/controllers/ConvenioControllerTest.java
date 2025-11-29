package com.simplehealth.cadastro.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.cadastro.application.dto.ConvenioDTO;
import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.application.service.ConvenioService;
import com.simplehealth.cadastro.domain.entity.Convenio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(ConvenioController.class)
class ConvenioControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ConvenioService convenioService;

  private Convenio convenio;
  private ConvenioDTO convenioDTO;

  @BeforeEach
  void setUp() {
    convenio = Convenio.builder()
        .id(1L)
        .nome("Unimed")
        .plano("Básico")
        .ativo(true)
        .build();

    convenioDTO = ConvenioDTO.builder()
        .id(1L)
        .nome("Unimed")
        .plano("Básico")
        .ativo(true)
        .build();
  }

  @Test
  @WithMockUser
  void testCreate_Success() throws Exception {
    when(convenioService.create(any(Convenio.class))).thenReturn(convenio);

    mockMvc.perform(post("/api/cadastro/convenios")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(convenioDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nome").value("Unimed"))
        .andExpect(jsonPath("$.plano").value("Básico"))
        .andExpect(jsonPath("$.ativo").value(true));

    verify(convenioService, times(1)).create(any(Convenio.class));
  }

  @Test
  @WithMockUser
  void testGetById_Success() throws Exception {
    when(convenioService.findById(anyLong())).thenReturn(convenio);

    mockMvc.perform(get("/api/cadastro/convenios/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nome").value("Unimed"));

    verify(convenioService, times(1)).findById(anyLong());
  }

  @Test
  @WithMockUser
  void testGetById_NotFound() throws Exception {
    when(convenioService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Convênio não encontrado"));

    mockMvc.perform(get("/api/cadastro/convenios/999"))
        .andExpect(status().isNotFound());

    verify(convenioService, times(1)).findById(anyLong());
  }

  @Test
  @WithMockUser
  void testList_Success() throws Exception {
    Convenio convenio2 = Convenio.builder()
        .id(2L)
        .nome("Bradesco")
        .plano("Premium")
        .ativo(true)
        .build();

    List<Convenio> convenios = Arrays.asList(convenio, convenio2);
    when(convenioService.findAll()).thenReturn(convenios);

    mockMvc.perform(get("/api/cadastro/convenios"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[1].id").value(2L));

    verify(convenioService, times(1)).findAll();
  }

  @Test
  @WithMockUser
  void testUpdate_Success() throws Exception {
    when(convenioService.update(anyLong(), any(Convenio.class))).thenReturn(convenio);

    mockMvc.perform(put("/api/cadastro/convenios/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(convenioDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nome").value("Unimed"));

    verify(convenioService, times(1)).update(anyLong(), any(Convenio.class));
  }

  @Test
  @WithMockUser
  void testDelete_Success() throws Exception {
    doNothing().when(convenioService).delete(anyLong());

    mockMvc.perform(delete("/api/cadastro/convenios/1")
            .with(csrf()))
        .andExpect(status().isNoContent());

    verify(convenioService, times(1)).delete(anyLong());
  }
}

