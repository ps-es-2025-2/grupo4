package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Paciente;
import com.simplehealth.cadastro.infrastructure.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

  @Mock
  private PacienteRepository pacienteRepository;

  @InjectMocks
  private PacienteService pacienteService;

  private Paciente paciente;

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
  }

  @Test
  void testSave_Success() {
    when(pacienteRepository.existsByCpf(anyString())).thenReturn(false);
    when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

    Paciente saved = pacienteService.save(paciente);

    assertNotNull(saved);
    assertEquals("João Silva", saved.getNomeCompleto());
    verify(pacienteRepository, times(1)).existsByCpf(anyString());
    verify(pacienteRepository, times(1)).save(any(Paciente.class));
  }

  @Test
  void testSave_CpfJaCadastrado() {
    when(pacienteRepository.existsByCpf(anyString())).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      pacienteService.save(paciente);
    });

    assertEquals("CPF já cadastrado", exception.getMessage());
    verify(pacienteRepository, times(1)).existsByCpf(anyString());
    verify(pacienteRepository, never()).save(any(Paciente.class));
  }

  @Test
  void testFindById_Success() {
    when(pacienteRepository.findById(anyLong())).thenReturn(Optional.of(paciente));

    Paciente found = pacienteService.findById(1L);

    assertNotNull(found);
    assertEquals(1L, found.getId());
    assertEquals("João Silva", found.getNomeCompleto());
    verify(pacienteRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindById_NotFound() {
    when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      pacienteService.findById(1L);
    });

    assertEquals("Paciente não encontrado", exception.getMessage());
    verify(pacienteRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindAll() {
    Paciente paciente2 = Paciente.builder()
        .id(2L)
        .nomeCompleto("Maria Santos")
        .cpf("98765432100")
        .build();

    when(pacienteRepository.findAll()).thenReturn(Arrays.asList(paciente, paciente2));

    List<Paciente> pacientes = pacienteService.findAll();

    assertNotNull(pacientes);
    assertEquals(2, pacientes.size());
    verify(pacienteRepository, times(1)).findAll();
  }

  @Test
  void testUpdate_Success() {
    Paciente updatedData = Paciente.builder()
        .nomeCompleto("João Silva Atualizado")
        .cpf("12345678901")
        .dataNascimento(LocalDate.of(1990, 1, 1))
        .telefone("11988888888")
        .email("joao.novo@email.com")
        .build();

    when(pacienteRepository.findById(anyLong())).thenReturn(Optional.of(paciente));
    when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

    Paciente updated = pacienteService.update(1L, updatedData);

    assertNotNull(updated);
    verify(pacienteRepository, times(1)).findById(anyLong());
    verify(pacienteRepository, times(1)).save(any(Paciente.class));
  }

  @Test
  void testUpdate_NotFound() {
    when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      pacienteService.update(1L, paciente);
    });

    assertEquals("Paciente não encontrado", exception.getMessage());
    verify(pacienteRepository, times(1)).findById(anyLong());
    verify(pacienteRepository, never()).save(any(Paciente.class));
  }

  @Test
  void testUpdate_CpfJaCadastradoPorOutroPaciente() {
    Paciente updatedData = Paciente.builder()
        .nomeCompleto("João Silva")
        .cpf("99999999999")
        .build();

    when(pacienteRepository.findById(anyLong())).thenReturn(Optional.of(paciente));
    when(pacienteRepository.existsByCpf("99999999999")).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      pacienteService.update(1L, updatedData);
    });

    assertEquals("CPF já cadastrado por outro paciente", exception.getMessage());
    verify(pacienteRepository, times(1)).findById(anyLong());
    verify(pacienteRepository, never()).save(any(Paciente.class));
  }

  @Test
  void testDelete_Success() {
    when(pacienteRepository.existsById(anyLong())).thenReturn(true);
    doNothing().when(pacienteRepository).deleteById(anyLong());

    pacienteService.delete(1L);

    verify(pacienteRepository, times(1)).existsById(anyLong());
    verify(pacienteRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void testDelete_NotFound() {
    when(pacienteRepository.existsById(anyLong())).thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      pacienteService.delete(1L);
    });

    assertEquals("Paciente não encontrado", exception.getMessage());
    verify(pacienteRepository, times(1)).existsById(anyLong());
    verify(pacienteRepository, never()).deleteById(anyLong());
  }

  @Test
  void testExistsByCpf_True() {
    when(pacienteRepository.existsByCpf(anyString())).thenReturn(true);

    boolean exists = pacienteService.existsByCpf("12345678901");

    assertTrue(exists);
    verify(pacienteRepository, times(1)).existsByCpf(anyString());
  }

  @Test
  void testExistsByCpf_False() {
    when(pacienteRepository.existsByCpf(anyString())).thenReturn(false);

    boolean exists = pacienteService.existsByCpf("12345678901");

    assertFalse(exists);
    verify(pacienteRepository, times(1)).existsByCpf(anyString());
  }
}

