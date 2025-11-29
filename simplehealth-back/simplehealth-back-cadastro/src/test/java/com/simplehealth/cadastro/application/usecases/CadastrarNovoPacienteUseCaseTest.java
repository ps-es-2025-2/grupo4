package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.service.PacienteService;
import com.simplehealth.cadastro.domain.entity.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarNovoPacienteUseCaseTest {

  @Mock
  private PacienteService pacienteService;

  @InjectMocks
  private CadastrarNovoPacienteUseCase cadastrarNovoPacienteUseCase;

  private PacienteDTO pacienteDTO;
  private Paciente paciente;

  @BeforeEach
  void setUp() {
    pacienteDTO = PacienteDTO.builder()
        .nomeCompleto("João Silva")
        .cpf("12345678901")
        .dataNascimento(LocalDate.of(1990, 1, 1))
        .telefone("11999999999")
        .email("joao@email.com")
        .build();

    paciente = Paciente.builder()
        .id(1L)
        .nomeCompleto("João Silva")
        .cpf("12345678901")
        .dataNascimento(LocalDate.of(1990, 1, 1))
        .telefone("11999999999")
        .email("joao@email.com")
        .build();
  }

  @Test
  void testExecute_Success() throws Exception {
    when(pacienteService.existsByCpf(anyString())).thenReturn(false);
    when(pacienteService.save(any(Paciente.class))).thenReturn(paciente);

    PacienteDTO result = cadastrarNovoPacienteUseCase.execute(pacienteDTO);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("João Silva", result.getNomeCompleto());
    assertEquals("12345678901", result.getCpf());
    verify(pacienteService, times(1)).existsByCpf(anyString());
    verify(pacienteService, times(1)).save(any(Paciente.class));
  }

  @Test
  void testExecute_CpfJaCadastrado() {
    when(pacienteService.existsByCpf(anyString())).thenReturn(true);

    Exception exception = assertThrows(Exception.class, () -> {
      cadastrarNovoPacienteUseCase.execute(pacienteDTO);
    });

    assertEquals("CPF já cadastrado no sistema.", exception.getMessage());
    verify(pacienteService, times(1)).existsByCpf(anyString());
    verify(pacienteService, never()).save(any(Paciente.class));
  }

  @Test
  void testExecute_ComTodosOsCampos() throws Exception {
    PacienteDTO dtoCompleto = PacienteDTO.builder()
        .nomeCompleto("Maria Santos")
        .cpf("98765432100")
        .dataNascimento(LocalDate.of(1985, 5, 15))
        .telefone("11988888888")
        .email("maria@email.com")
        .build();

    Paciente pacienteSalvo = Paciente.builder()
        .id(2L)
        .nomeCompleto("Maria Santos")
        .cpf("98765432100")
        .dataNascimento(LocalDate.of(1985, 5, 15))
        .telefone("11988888888")
        .email("maria@email.com")
        .build();

    when(pacienteService.existsByCpf(anyString())).thenReturn(false);
    when(pacienteService.save(any(Paciente.class))).thenReturn(pacienteSalvo);

    PacienteDTO result = cadastrarNovoPacienteUseCase.execute(dtoCompleto);

    assertNotNull(result);
    assertEquals(2L, result.getId());
    assertEquals("Maria Santos", result.getNomeCompleto());
    assertEquals("98765432100", result.getCpf());
    assertEquals(LocalDate.of(1985, 5, 15), result.getDataNascimento());
    assertEquals("11988888888", result.getTelefone());
    assertEquals("maria@email.com", result.getEmail());
  }
}

