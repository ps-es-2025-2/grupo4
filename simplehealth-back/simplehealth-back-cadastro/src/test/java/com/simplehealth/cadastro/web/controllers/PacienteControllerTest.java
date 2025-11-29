package com.simplehealth.cadastro.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.cadastro.application.dto.HistoricoPacienteDTO;
import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.application.service.PacienteService;
import com.simplehealth.cadastro.application.usecases.CadastrarNovoPacienteUseCase;
import com.simplehealth.cadastro.application.usecases.ConsultarHistoricoPacienteUseCase;
import com.simplehealth.cadastro.domain.entity.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
class PacienteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PacienteService pacienteService;

  @MockBean
  private CadastrarNovoPacienteUseCase cadastrarNovoPacienteUseCase;

  @MockBean
  private ConsultarHistoricoPacienteUseCase consultarHistoricoPacienteUseCase;

  private Paciente paciente;
  private PacienteDTO pacienteDTO;

  @BeforeEach
  void setUp() {
    paciente = Paciente.builder()
        .id(1L)
        .nomeCompleto("João Silva")
        .cpf("12345678901")
        .dataNascimento(LocalDate.of(1990, 1, 1))
        .telefone("11999999999")
        .email("joao@email.com")
        .build();

    pacienteDTO = PacienteDTO.builder()
        .id(1L)
        .nomeCompleto("João Silva")
        .cpf("12345678901")
        .dataNascimento(LocalDate.of(1990, 1, 1))
        .telefone("11999999999")
        .email("joao@email.com")
        .build();
  }

  @Test
  @WithMockUser
  void testCreate_Success() throws Exception {
    when(cadastrarNovoPacienteUseCase.execute(any(PacienteDTO.class))).thenReturn(pacienteDTO);

    mockMvc.perform(post("/pacientes")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pacienteDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nomeCompleto").value("João Silva"))
        .andExpect(jsonPath("$.cpf").value("12345678901"));

    verify(cadastrarNovoPacienteUseCase, times(1)).execute(any(PacienteDTO.class));
  }

  @Test
  @WithMockUser
  void testFindById_Success() throws Exception {
    when(pacienteService.findById(anyLong())).thenReturn(paciente);

    mockMvc.perform(get("/pacientes/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nomeCompleto").value("João Silva"))
        .andExpect(jsonPath("$.cpf").value("12345678901"));

    verify(pacienteService, times(1)).findById(anyLong());
  }

  @Test
  @WithMockUser
  void testFindById_NotFound() throws Exception {
    when(pacienteService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Paciente não encontrado"));

    mockMvc.perform(get("/pacientes/999"))
        .andExpect(status().isNotFound());

    verify(pacienteService, times(1)).findById(anyLong());
  }

  @Test
  @WithMockUser
  void testFindAll_Success() throws Exception {
    Paciente paciente2 = Paciente.builder()
        .id(2L)
        .nomeCompleto("Maria Santos")
        .cpf("98765432100")
        .dataNascimento(LocalDate.of(1985, 5, 15))
        .build();

    List<Paciente> pacientes = Arrays.asList(paciente, paciente2);
    when(pacienteService.findAll()).thenReturn(pacientes);

    mockMvc.perform(get("/pacientes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[1].id").value(2L));

    verify(pacienteService, times(1)).findAll();
  }

  @Test
  @WithMockUser
  void testConsultarHistorico_Success() throws Exception {
    HistoricoPacienteDTO historico = HistoricoPacienteDTO.builder()
        .dadosCadastrais(pacienteDTO)
        .agendamentos(Collections.emptyList())
        .procedimentos(Collections.emptyList())
        .itensBaixados(Collections.emptyList())
        .pagamentos(Collections.emptyList())
        .build();

    when(consultarHistoricoPacienteUseCase.execute(anyString())).thenReturn(historico);

    mockMvc.perform(get("/pacientes/historico/12345678901"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.dadosCadastrais").exists())
        .andExpect(jsonPath("$.agendamentos").isArray())
        .andExpect(jsonPath("$.procedimentos").isArray());

    verify(consultarHistoricoPacienteUseCase, times(1)).execute(anyString());
  }

  @Test
  @WithMockUser
  void testUpdate_Success() throws Exception {
    when(pacienteService.update(anyLong(), any(Paciente.class))).thenReturn(paciente);

    mockMvc.perform(put("/pacientes/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pacienteDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.nomeCompleto").value("João Silva"));

    verify(pacienteService, times(1)).update(anyLong(), any(Paciente.class));
  }

  @Test
  @WithMockUser
  void testDelete_Success() throws Exception {
    doNothing().when(pacienteService).delete(anyLong());

    mockMvc.perform(delete("/pacientes/1")
            .with(csrf()))
        .andExpect(status().isNoContent());

    verify(pacienteService, times(1)).delete(anyLong());
  }
}

