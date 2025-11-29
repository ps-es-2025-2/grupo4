package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Medico;
import com.simplehealth.cadastro.infrastructure.repositories.MedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

  @Mock
  private MedicoRepository medicoRepository;

  @InjectMocks
  private MedicoService medicoService;

  private Medico medico;

  @BeforeEach
  void setUp() {
    medico = Medico.builder()
        .id(1L)
        .nomeCompleto("Dr. Carlos Mendes")
        .crm("123456")
        .especialidade("Cardiologia")
        .telefone("11977777777")
        .email("carlos@email.com")
        .build();
  }

  @Test
  void testCreate_Success() {
    when(medicoRepository.existsByCrm(anyString())).thenReturn(false);
    when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

    Medico created = medicoService.create(medico);

    assertNotNull(created);
    assertEquals("Dr. Carlos Mendes", created.getNomeCompleto());
    assertEquals("123456", created.getCrm());
    verify(medicoRepository, times(1)).existsByCrm(anyString());
    verify(medicoRepository, times(1)).save(any(Medico.class));
  }

  @Test
  void testCreate_CrmJaCadastrado() {
    when(medicoRepository.existsByCrm(anyString())).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      medicoService.create(medico);
    });

    assertEquals("CRM já cadastrado", exception.getMessage());
    verify(medicoRepository, times(1)).existsByCrm(anyString());
    verify(medicoRepository, never()).save(any(Medico.class));
  }

  @Test
  void testFindById_Success() {
    when(medicoRepository.findById(anyLong())).thenReturn(Optional.of(medico));

    Medico found = medicoService.findById(1L);

    assertNotNull(found);
    assertEquals(1L, found.getId());
    assertEquals("Dr. Carlos Mendes", found.getNomeCompleto());
    verify(medicoRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindById_NotFound() {
    when(medicoRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      medicoService.findById(1L);
    });

    assertEquals("Médico não encontrado", exception.getMessage());
    verify(medicoRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindAll() {
    Medico medico2 = Medico.builder()
        .id(2L)
        .nomeCompleto("Dra. Ana Paula")
        .crm("654321")
        .especialidade("Ortopedia")
        .build();

    when(medicoRepository.findAll()).thenReturn(Arrays.asList(medico, medico2));

    List<Medico> medicos = medicoService.findAll();

    assertNotNull(medicos);
    assertEquals(2, medicos.size());
    verify(medicoRepository, times(1)).findAll();
  }

  @Test
  void testUpdate_Success() {
    Medico updatedData = Medico.builder()
        .nomeCompleto("Dr. Carlos Mendes Atualizado")
        .crm("123456")
        .especialidade("Cardiologia Clínica")
        .telefone("11988888888")
        .email("carlos.novo@email.com")
        .build();

    when(medicoRepository.findById(anyLong())).thenReturn(Optional.of(medico));
    when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

    Medico updated = medicoService.update(1L, updatedData);

    assertNotNull(updated);
    verify(medicoRepository, times(1)).findById(anyLong());
    verify(medicoRepository, times(1)).save(any(Medico.class));
  }

  @Test
  void testUpdate_NotFound() {
    when(medicoRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      medicoService.update(1L, medico);
    });

    assertEquals("Médico não encontrado", exception.getMessage());
    verify(medicoRepository, times(1)).findById(anyLong());
    verify(medicoRepository, never()).save(any(Medico.class));
  }

  @Test
  void testUpdate_CrmJaExisteParaOutroMedico() {
    Medico updatedData = Medico.builder()
        .nomeCompleto("Dr. Carlos Mendes")
        .crm("999999")
        .especialidade("Cardiologia")
        .build();

    when(medicoRepository.findById(anyLong())).thenReturn(Optional.of(medico));
    when(medicoRepository.existsByCrm("999999")).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      medicoService.update(1L, updatedData);
    });

    assertEquals("CRM já existe para outro médico", exception.getMessage());
    verify(medicoRepository, times(1)).findById(anyLong());
    verify(medicoRepository, never()).save(any(Medico.class));
  }

  @Test
  void testDelete_Success() {
    when(medicoRepository.existsById(anyLong())).thenReturn(true);
    doNothing().when(medicoRepository).deleteById(anyLong());

    medicoService.delete(1L);

    verify(medicoRepository, times(1)).existsById(anyLong());
    verify(medicoRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void testDelete_NotFound() {
    when(medicoRepository.existsById(anyLong())).thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      medicoService.delete(1L);
    });

    assertEquals("Médico não encontrado", exception.getMessage());
    verify(medicoRepository, times(1)).existsById(anyLong());
    verify(medicoRepository, never()).deleteById(anyLong());
  }
}

