package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Convenio;
import com.simplehealth.cadastro.infrastructure.repositories.ConvenioRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConvenioServiceTest {

  @Mock
  private ConvenioRepository convenioRepository;

  @InjectMocks
  private ConvenioService convenioService;

  private Convenio convenio;

  @BeforeEach
  void setUp() {
    convenio = Convenio.builder()
        .id(1L)
        .nome("Unimed")
        .plano("Básico")
        .ativo(true)
        .build();
  }

  @Test
  void testCreate_Success() {
    when(convenioRepository.save(any(Convenio.class))).thenReturn(convenio);

    Convenio created = convenioService.create(convenio);

    assertNotNull(created);
    assertEquals("Unimed", created.getNome());
    assertEquals("Básico", created.getPlano());
    assertTrue(created.getAtivo());
    verify(convenioRepository, times(1)).save(any(Convenio.class));
  }

  @Test
  void testFindById_Success() {
    when(convenioRepository.findById(anyLong())).thenReturn(Optional.of(convenio));

    Convenio found = convenioService.findById(1L);

    assertNotNull(found);
    assertEquals(1L, found.getId());
    assertEquals("Unimed", found.getNome());
    verify(convenioRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindById_NotFound() {
    when(convenioRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      convenioService.findById(1L);
    });

    assertEquals("Convênio não encontrado", exception.getMessage());
    verify(convenioRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindAll() {
    Convenio convenio2 = Convenio.builder()
        .id(2L)
        .nome("Bradesco Saúde")
        .plano("Premium")
        .ativo(true)
        .build();

    when(convenioRepository.findAll()).thenReturn(Arrays.asList(convenio, convenio2));

    List<Convenio> convenios = convenioService.findAll();

    assertNotNull(convenios);
    assertEquals(2, convenios.size());
    verify(convenioRepository, times(1)).findAll();
  }

  @Test
  void testUpdate_Success() {
    Convenio updatedData = Convenio.builder()
        .nome("Unimed Atualizado")
        .plano("Premium")
        .ativo(false)
        .build();

    when(convenioRepository.findById(anyLong())).thenReturn(Optional.of(convenio));
    when(convenioRepository.save(any(Convenio.class))).thenReturn(convenio);

    Convenio updated = convenioService.update(1L, updatedData);

    assertNotNull(updated);
    verify(convenioRepository, times(1)).findById(anyLong());
    verify(convenioRepository, times(1)).save(any(Convenio.class));
  }

  @Test
  void testUpdate_NotFound() {
    when(convenioRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      convenioService.update(1L, convenio);
    });

    assertEquals("Convênio não encontrado", exception.getMessage());
    verify(convenioRepository, times(1)).findById(anyLong());
    verify(convenioRepository, never()).save(any(Convenio.class));
  }

  @Test
  void testDelete_Success() {
    when(convenioRepository.existsById(anyLong())).thenReturn(true);
    doNothing().when(convenioRepository).deleteById(anyLong());

    convenioService.delete(1L);

    verify(convenioRepository, times(1)).existsById(anyLong());
    verify(convenioRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void testDelete_NotFound() {
    when(convenioRepository.existsById(anyLong())).thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      convenioService.delete(1L);
    });

    assertEquals("Convênio não encontrado", exception.getMessage());
    verify(convenioRepository, times(1)).existsById(anyLong());
    verify(convenioRepository, never()).deleteById(anyLong());
  }
}

